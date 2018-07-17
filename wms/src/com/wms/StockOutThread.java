package com.wms;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.RetrievalOrderStatus;
import com.asrs.business.consts.StationMode;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.MCar;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

/**
 * seq2此货位距离出库升降机的距离，越小越近
 * 各个组seq2最小值降序排列，将所剩货物最小的组，先出库，尽快腾出空间。
 * 将本组seq2升序排列，由近到远依次出库。
 */
public class StockOutThread {

  public static void main(String[] args) {
      while (true){
          try {
              Transaction.begin();
              Session session = HibernateUtil.getCurrentSession();
              Query query = HibernateUtil.getCurrentSession().createQuery("from RetrievalOrder r " +
                      "where r.status=:status");
              query.setParameter("status", RetrievalOrderStatus.WAITING);
              List<RetrievalOrder> rolList = query.list();

              for (RetrievalOrder rol : rolList) {
                  String toStation= rol.getToStation();

                  for(RetrievalOrderDetail rolDetail : rol.getRetrievalOrderDetailSet()){

                      String skuCode = rolDetail.getItemCode();//出库商品代码
                      String lotNum = rolDetail.getBatch();//出库商品批次
                      int qty = rolDetail.getQty().intValue();//获取订单数量
                      //获取出库巷道
                      String position=findPosition("0", skuCode,lotNum ,qty,rol,toStation );
                      List<String> positionList = new ArrayList<>();
                      if("0".equals(position)){
                          positionList.add("1");
                          positionList.add("2");
                      }else{
                          positionList.add(position);
                      }

                      Query query2 = HibernateUtil.getCurrentSession().createQuery("from Inventory i where " +
                              "i.skuCode = :skuCode  and i.lotNum = :lotNum and i.container.reserved = false and " +
                              "i.container.location.retrievalRestricted = false and i.container.location.abnormal = false " +
                              "and i.container.location.position in (:positions) " +
                              "and not exists (select 1 from Location l where l.bay=i.container.location.bay and " +
                              "l.actualArea=i.container.location.actualArea and l.level =i.container.location.level and " +
                              "l.position=i.container.location.position and  l.seq > i.container.location.seq and " +
                              "l.reserved = true )");
                      query2.setParameter("skuCode",skuCode);
                      query2.setParameterList("positions",positionList);
                      query2.setString("lotNum",lotNum);
                      List<Inventory> inventoryList = query2.list();
                      if(inventoryList.size()!=0 && inventoryList!=null){
                          //所有单元格货品位置的集合，四坐标为值
                          Map<String,Map<Integer,Inventory>> map = new HashMap<>();
                          //四坐标为key，每个四坐标的最小seq2为值的map
                          Map<String,Integer> sortMap = new HashMap<>();

                          for (int i = 0; i < inventoryList.size(); i++) {
                              Inventory inventory = inventoryList.get(i);
                              Container container = inventory.getContainer();
                              Location location = container.getLocation();
                              String key = location.getBay() + "," + location.getLevel() + "," + location.getPosition() + "," + location.getActualArea();
                              Map<Integer,Inventory> map2 = map.get(key);
                              if(map2 == null){
                                  map2 = new HashMap<>();
                              }
                              //存放seq2，位置类
                              map2.put(location.getSeq(),inventory);
                              map.put(key,map2);
                              Integer seq = sortMap.get(key);
                              if(seq == null ||seq < location.getSeq()){
                                  sortMap.put(key,location.getSeq());
                              }

                          }
                          //将获取到的最大值seq放入set集合
                          Set<Integer> seqSet = new HashSet<>();
                          for(Map.Entry<String,Integer> entry:sortMap.entrySet()){
                              seqSet.add(entry.getValue());
                          }
                          //将set里的值排序，放入list中
                          List<Integer> seqList = new ArrayList<>();
                          Collections.addAll(seqList,seqSet.toArray(new Integer[seqSet.size()]));
                          Collections.sort(seqList, new Comparator<Integer>() {
                              @Override
                              public int compare(Integer o1, Integer o2) {
                                  return o1 - o2;//升序
                              }
                          });

                          List<String> list = new ArrayList<>();
                          //将排序好的seq 的值和原本sortmap中的值比较，将其放入list
                          for(int seq:seqList){
                              for(Map.Entry<String,Integer> entry:sortMap.entrySet()){
                                  //若有重复的就add两次
                                  if(seq == entry.getValue()){
                                      list.add(entry.getKey());
                                  }
                              }
                          }
                          //将各个组排好序（降序），将组内排序
                          //                  OUT:
                          for(String key :list){
                              Map<Integer,Inventory> map2 = map.get(key);
                              List<Integer> sortMap2List = new ArrayList<>();
                              //将组内单元格按照seq排序，降序
                              Collections.addAll(sortMap2List,map2.keySet().toArray(new Integer[map2.keySet().size()]));
                              Collections.sort(sortMap2List, new Comparator<Integer>() {
                                  @Override
                                  public int compare(Integer o1, Integer o2) {
                                      return o2 - o1;//降序
                                  }
                              });
                              //将组内单元格循环，生成任务发送给设备，并将已完成数量+
                              /**
                               * 在发送任务前需不需要将货位锁定？出库：retrievalRestricted，入库：reserved
                               * inventory里的货品数量是哪个字段？qty
                               * 接下来保存任务到数据库，供另一个线程将任务发动给设备
                               */
                              for(int seq2 : sortMap2List){
                                  Inventory inventory = map2.get(seq2);
                                  Location location = inventory.getContainer().getLocation();
                                  int qty2 = inventory.getQty().intValue();//货品数量
                                  qty = qty - qty2;

                                  JobDetail jobDetail = new JobDetail();
                                  Job job = new Job();
                                  //session准备存入job，commit时才会执行sql
                                  session.save(job);
                                  session.save(jobDetail);
                                  //数据准备

                                  String mckey = Mckey.getNext();
                                 /* String toStation = position.equals("1") ? "1201" : "1301";//到达站台*/
                                  MCar toMCar =MCar.getMCarByPosition(location.getPosition(),location.getLevel());//出库母车
                                  String fromStation =toMCar.getBlockNo();//出发地点
                                  String type = AsrsJobType.RETRIEVAL; //出库
                                  //存入jobDetail
                                  jobDetail.setInventory(inventory);
                                  jobDetail.setQty(inventory.getQty());
                                  //存入job
                                  job.setContainer(inventory.getContainer().getBarcode());
                                  job.setFromStation(fromStation);
                                  job.setMcKey(mckey);
                                  job.setOrderNo(rol.getOrderNo());
                                  job.setSendReport(false);
                                  job.setStatus("1");
                                  job.setToStation(toStation);
                                  job.setType(type);
                                  job.addJobDetail(jobDetail);
                                  job.setCreateDate(new Date());
                                  job.setFromLocation(inventory.getContainer().getLocation());
                                  //修改订单表中的数据
                                  int wancheng = rolDetail.getCreateJobNum();
                                  rolDetail.setCreateJobNum(wancheng+1);
                                  session.saveOrUpdate(rolDetail);
                                  //修改此托盘
                                  Container container = inventory.getContainer();
                                  container.setReserved(true);
                                  session.saveOrUpdate(container);

                                  if(qty<=0){
                                      break;
                                  }
                              }
                              if(qty<=0){
                                  break;
                              }
                          }
                      }
                  }
                  rol.setStatus(RetrievalOrderStatus.ACCPET);
              }
              Transaction.commit();
          }catch (Exception e){
              Transaction.rollback();
              e.printStackTrace();
          }

          try {
              Thread.sleep(3000);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
  }

  public static String findPosition (String position,String skuCode,String lotNum,int qty,RetrievalOrder rol,String toStation){
      Station station1303 = Station.getStation("1303");
      long onecount = Inventory.getNumsBySkuCodeAndLotNumAndPosition(skuCode,lotNum,"1" );//1巷道货物库存
      long twocount = Inventory.getNumsBySkuCodeAndLotNumAndPosition(skuCode,lotNum,"2" );//2巷道货物库存

      //根据1，2巷道拥有的货物数量判断先出哪一巷道的货物
      if((twocount<qty && onecount<qty)||(twocount>qty && onecount>qty)){
          position="0";//两者皆可
      }else if(twocount>qty && onecount<qty){
          position="2";//2巷道
      }else if(twocount<qty && onecount>qty){
          position="1";//1巷道
      }
      if(StationMode.PUTAWAY.equals(station1303.getDirection())){
          //入库状态，2巷道的货可以出到所有地方
          if(twocount<qty && "1202".equals(toStation)){
              //2巷道的货不满足出库需要，并且需要出库到1号口
              Transaction.rollback();
              try {
                  Transaction.begin();
                  rol.setError("出库数量大于2巷道仓库存储数量，不能出到1号口，请更换出库站台或至出入库状态切换页面切换负责区域！");
                  rol.setStatus(RetrievalOrderStatus.ABNORMAL);
                  Transaction.commit();
              }catch (Exception e){
                  Transaction.rollback();
                  e.printStackTrace();
              }
          }
          if(twocount>=qty && "1202".equals(toStation)){
              position="2";
          }
      }else if(StationMode.RETRIEVAL2.equals(station1303.getDirection())){
          //入库状态，2巷道的货可以出到所有地方
          if(onecount<qty && ("1204".equals(toStation)||"1206".equals(toStation))){
              //2巷道的货不满足出库需要，并且需要出库到1号口
              Transaction.rollback();
              try {
                  Transaction.begin();
                  rol.setError("出库数量大于1巷道仓库存储数量，不能出到2,3号口，请更换出库站台或至出入库状态切换页面切换负责区域！");
                  rol.setStatus(RetrievalOrderStatus.ABNORMAL);
                  Transaction.commit();
              }catch (Exception e){
                  Transaction.rollback();
                  e.printStackTrace();
              }
          }
          if(onecount>=qty && ("1204".equals(toStation)||"1206".equals(toStation))){
              position="1";
          }
      }
      return position;
  }

}

