package com.wms.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.StationMode;
import com.util.common.Const;
import com.util.common.LogMessage;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.Block;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.junit.Test;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AssignsTheStorehouseService {
    /*
     * @description：初始化map
     */
    public ReturnObj<Map<String, Object>> getStorageLocationData(String productId,String tier){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询总货位
//            Query query1 = session.createQuery("select convert(varchar,bank)+'_'+convert(varchar,bay) as coordinate from Location where " +
//                    "putawayRestricted = false and retrievalRestricted = false and level=:level  order by bank , bay ");
//            query1.setString("level", tier);
            Query query1 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate " +
                    "from Location l where not exists (select 1 from Location lo where l.id=lo.id and " +
                    "lo.putawayRestricted = true and lo.retrievalRestricted = true) and l.level=:level   " +
//                    " and l.bank!=26 and not exists (select 1 from Location ll where l.id=ll.id and ( (ll.bay in (:bays) and ll.bank=:bank) or " +
//                    "(ll.locationNo = :locationNo)))" +
                    "order by l.bank , l.bay ");
            query1.setString("level", tier);
//            query1.setParameterList("bays", bayslist);
//            query1.setParameter("bank", 12);
//            query1.setParameter("locationNo", "001013001");
            List<String> list = query1.list();
            int bankCount = Const.bankCount;
            int bayCount = Const.bayCount;
            List<String> LocationList = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            //外层循环
            for(int i =1;i<=bayCount;i++){
                StringBuffer sb = new StringBuffer();
                //内层循环
                for(int j =1; j<=bankCount;j++){
                    if(list.contains(j+"_"+i)){
                        sb.append("a");
                    }else{
                        sb.append("_");
                    }
                }
                LocationList.add(sb.toString());
            }
            //查询空货位
            Query query2 = session.createQuery("select convert(varchar,a.bank)+'_'+convert(varchar,a.bay) as coordinate from Location a where " +
                    "a.empty = true and a.level = :level ");
            query2.setString("level", tier);
            //判断可以定点出库的货位
            StringBuffer sb = new StringBuffer(
                    " select convert(varchar,e.bank)+'_'+convert(varchar,e.bay) as coordinate from Inventory i , CONTAINER d " +
                            ",( " +
                            "select a.id as id ,a.BANK,a.BAY from Location a , " +
                            "(select max(seq) as seq,bay,POSITION,AREA " +
                            "from LOCATION where empty=0 and lev=:level   and " +
                            "RETRIEVALRESTRICTED=0 and ABNORMAL =0 group by bay,POSITION,AREA " +
                            ") b " +
                            "where a.seq=b.seq and a.bay = b.bay and " +
                            "a.position = b.position and a.area = b.area and a.lev=:level and  " +
                            "RETRIEVALRESTRICTED=0 and ABNORMAL =0 and  not exists( " +
                            "select 1 from Location l where l.bay=a.bay and " +
                            "l.area=a.area and l.lev =a.lev " +
                            "and  l.position=a.position and l.seq < a.seq  and l.reserved = 1 " +
                            "  ) " +
                            ") e  where i.CONTAINERID=d.ID and e.ID = d.LOCATIONID and d.RESERVED=0 ");
            if(StringUtils.isNotBlank(productId)){
                sb.append(" and i.skucode=:skucode ");
            }
            Query query3 = session.createSQLQuery(sb.toString());
            if(StringUtils.isNotBlank(productId)){
                query3.setString("skucode", productId);
            }
            query3.setString("level", tier);
            //查询已经有出库任务的货位
            Query query4 = session.createQuery("select convert(varchar,c.location.bank)+'_'+convert(varchar,c.location.bay) as coordinate from Container c where " +
                    " c.location.level = :level and c.reserved = true  ");
            query4.setString("level", tier);
            //查询已经有入库任务的货位
            Query query5 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate from Location l where " +
                    " l.level = :level and l.reserved = true  ");
            query5.setString("level", tier);
            map.put("map", LocationList); //总货位
            map.put("emptyList", query2.list()); //空货位
            map.put("availableList", query3.list()); //可被选择的货位
            map.put("reservedOutList", query4.list()); //已有出库任务货位
            map.put("reservedInList", query5.list()); //已有入库任务货位
            map.put("unavailableList", list);
            s.setRes(map);
            s.setSuccess(true);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());
        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
            return s;
    }
    /*
     * @description：查询货位货物信息
     */
    public ReturnObj<Map<String, Object>> getLocationInfo(String bank,String bay,String level){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询货位
            Query query = session.createQuery("select i.skuCode as skuCode,i.skuName as skuName, " +
                    "i.lotNum as lotNum,i.qty as qty,i.container.barcode as barcode, " +
                    "i.container.location.bank as bank,i.container.location.bay as bay, " +
                    "i.container.location.level as level,i.container.location.position as position from Inventory i where i.container.location.bank " +
                    "= :bank and i.container.location.bay = :bay and i.container.location.level = :level").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setInteger("bank", Integer.parseInt(bank));
            query.setInteger("bay", Integer.parseInt(bay));
            query.setInteger("level", Integer.parseInt(level));
            List<Map<String,Object>> mapList = query.list();
            Map<String,Object> map = new HashMap<>();
            if(mapList.size()==0){
                map.put("bank", bank);
                map.put("bay", bay);
                map.put("level", level);
                map.put("position", "2");
                map.put("status", false);
                map.put("msg", "空货位");
            }else{
                map = mapList.get(0);
                map.put("status", true);
                map.put("msg", "有货");
            }
            s.setRes(map);
            s.setSuccess(true);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());
        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return s;
    }

    /*
     * @description：查询下一个可选货位信息
     */
    public ReturnObj<Map<String, Object>> getNextAvailableLocation(String bank,String bay,String level){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询货位
            Query query = session.createQuery("select convert(varchar,a.bank)+'_'" +
                    "+convert(varchar,a.bay) as coordinate from Location a " +
                    "where a.position=(select position from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.actualArea = (select actualArea from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.seq=(select seq-1 from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.level = :level  and a.bay=:bay and a.empty = false");
            query.setString("bank", bank);
            query.setString("bay", bay);
            query.setString("level", level);
            List<String> list = query.list();
            Map<String,Object> map = new HashMap<>();
            if(list.size()==0){
                map.put("status", false);
            }else{
                map.put("status", true);
                map.put("location", list);
            }
            s.setRes(map);
            s.setSuccess(true);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());
        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return s;
    }

    /*
     * @description：获取里面的货位代码
     */
    public ReturnObj<Map<String, Object>> getAgoUnavailableLocation(String bank,String bay,String level){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询货位
            Query query = session.createQuery("select convert(varchar,a.bank)+'_'" +
                    "+convert(varchar,a.bay) as coordinate from Location a " +
                    "where a.position=(select position from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.actualArea = (select actualArea from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.seq<(select seq from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.level = :level  and a.bay=:bay and a.empty = false");
            query.setString("bank", bank);
            query.setString("bay", bay);
            query.setString("level", level);

            List<String> list = query.list();
            Map<String,Object> map = new HashMap<>();
            if(list.size()==0){
                map.put("status", false);
            }else{
                map.put("status", true);
                map.put("location", list);
            }
            s.setRes(map);
            s.setSuccess(true);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());
        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
         return s;
    }

    /*
     * @description：设定出库任务
     */
    public ReturnObj<Map<String, Object>> assignsTheStorehouse(String selectLocation,String stationNo){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        JSONArray jsonArray = JSONArray.fromObject(selectLocation);
        List<String> list = (List<String>) JSONArray.toCollection(jsonArray,String.class);
        try {
            Transaction.begin();
            List list1= new ArrayList<>();
            list1.add("1201");
            list1.add("1202");
            List list2= new ArrayList<>();
            list2.add("1203");
            list2.add("1204");
            List list3= new ArrayList<>();
            list3.add("1205");
            list3.add("1206");
            Map<String,List> stations = new HashMap<>();
            stations.put("1",list1);
            stations.put("2",list2);
            stations.put("3",list3);
            if(stations.get(stationNo)!=null){
                s=ku(list,s,stations.get(stationNo),stations);
            }
        } catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());
        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
            return s;
    }
    public static ReturnObj<Map<String, Object>> ku(List<String> list ,ReturnObj<Map<String, Object>> s,List<String> list1,Map<String,List> stations){
        boolean flag = true;
        String location="";
        Session session = HibernateUtil.getCurrentSession();
        for(int i =0;i<list.size();i++){
           /* Query query1 = session.createSQLQuery("(select count(*) from AsrsJob where toStation=(select blockNo from Block where stationNo=:stationNo))").setString("stationNo",list1.get(1)+"");
            List<Integer> list2 = query1.list();
            int count=20;
            if((list.size()>(count-(list2.get(0))))||list.size()>count){
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("该出库口没有足够存位");
                return s;
            }*/
            location = list.get(i);
            //按照Container中的reserved判断

            Query query4 = session.createQuery("from AsrsJob where toLocation in (select l.locationNo from Location l,Location ll where l.level=ll.level and l.bay=ll.bay and l.position=ll.position and l.actualArea=ll.actualArea and  ll.locationNo=:locationNo)");
            List<AsrsJob> asrsJobs = query4.setString("locationNo", location).list();
            if(asrsJobs.size()>0){
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("货位："+location+"前已有入库任务");
                return s;
            }
            Query query = session.createQuery(" from Container c where reserved = false and c.location.locationNo=:locationNo");
            query.setString("locationNo", location);
            Container container =(Container) query.uniqueResult();
            if(container!=null){
                String stationNo=list1.get(1);
                Location location1 = Location.getByLocationNo(location);
                Station station1303 = Station.getStation("1303");
                if("1".equals(location1.getPosition())){
                    if("1201".equals(stationNo) || "1202".equals(stationNo) ){
                        List listToStaions= new ArrayList<>();
                        listToStaions.add("1203");
                        listToStaions.add("1204");
                        listToStaions.add("1205");
                        listToStaions.add("1206");
                        Query query2 = session.createQuery("select count(*) as count from AsrsJob a ,Location l where " +
                                "a.toStation in (:toStations) and a.fromLocation=l.locationNo and l.position=:position and a.status !=:status");
                        query2.setParameterList("toStations",listToStaions);
                        query2.setParameter("position","2");
                        query2.setParameter("status", AsrsJobStatus.DONE);
                        long toStationCount = (long)query2.uniqueResult();
                        if(toStationCount==0){
                            if (!StationMode.RETRIEVAL2.equals(station1303.getDirection())){
                                Transaction.rollback();
                                s.setSuccess(false);
                                s.setMsg("货位："+location+"无法抵达"+"出库站台："+stationNo+",请切换"+stationNo+"负责巷道");
                                return s;
                            }
                        }else{
                            Transaction.rollback();
                            s.setSuccess(false);
                            s.setMsg("存在路径冲入不能出到"+stationNo);
                            return s;
                        }

                    }
                }else if("2".equals(location1.getPosition())){
                    if("1201".equals(stationNo) || "1202".equals(stationNo) ){

                    }else{
                        List listToStaions= new ArrayList<>();
                        listToStaions.add("1201");
                        listToStaions.add("1202");
                        Query query2 = session.createQuery("select count(*) as count from AsrsJob a ,Location l where " +
                                "a.toStation in (:toStations) and a.fromLocation=l.locationNo and l.position=:position and a.status !=:status");
                        query2.setParameterList("toStations",listToStaions);
                        query2.setParameter("position","1");
                        query2.setParameter("status", AsrsJobStatus.DONE);
                        long toStationCount = (long)query2.uniqueResult();
                        if(toStationCount==0) {
                            if (!StationMode.PUTAWAY.equals(station1303.getDirection())) {
                                Transaction.rollback();
                                s.setSuccess(false);
                                s.setMsg("货位：" + location + "无法抵达" + "出库站台：" + stationNo + ",请切换" + stationNo + "负责巷道");
                                return s;
                            }
                        }else{
                            Transaction.rollback();
                            s.setSuccess(false);
                            s.setMsg("存在路径冲入不能出到"+stationNo);
                            return s;
                        }
                    }
                }

                boolean b = outKu(session, location, stationNo);
                if(!b){
                    Transaction.rollback();
                    s.setSuccess(false);
                    s.setMsg("货位："+location+"无法抵达"+"出库站台："+stationNo);
                    return s;
                }else {
                    s.setMsg("设定出库成功");
                    s.setSuccess(true);
                }
            }else{
                flag=false;
                break;
            }
        }

        if(flag){
            Transaction.commit();
        }else{
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg("货位："+location+"前已有入库任务");
        }
        return s;
    }
    //生成出库任务
    public static boolean outKu(Session session,String locationNo,String stationNo){
        try {
            Query query = session.createQuery("from Inventory i where i.container.location.locationNo = :locationNo").setString("locationNo",locationNo);
            List<Inventory> inventoryList = query.list();
            Inventory inventory =inventoryList.get(0);
            int qty = inventory.getQty().intValue();//货品数量
            String position = inventory.getContainer().getLocation().getPosition();
            JobDetail jobDetail = new JobDetail();
            Job job = new Job();
            //session准备存入job，commit时才会执行sql
            session.save(job);
            session.save(jobDetail);
            //数据准备
            String mckey = Mckey.getNext();
            String type = AsrsJobType.RETRIEVAL; //出库
            String fromStation="MC0";
            Integer bank=Integer.parseInt(locationNo.substring(1,3));
            Integer bay=Integer.parseInt(locationNo.substring(4,6));
            Integer lev=Integer.parseInt(locationNo.substring(locationNo.length()-2,locationNo.length()));
            if(bank>=26){
                if(lev==1){         fromStation+=lev;   }
                else if(lev==2){    fromStation+=lev;   }
                else if(lev==3){    fromStation+=lev;   }
                else if(lev==4){    fromStation+=lev;   }
            }if(bank<26){            int i=4;
                if(lev==1){         fromStation+=lev+i; }
                else if(lev==2){    fromStation+=lev+i; }
                else if(lev==3){    fromStation+=lev+i; }
                else if(lev==4){    fromStation+=lev+i; }
            }
            job.setFromStation(fromStation);
//            String toStation=ToStation(position,stationNo,job);
            job.setToStation(stationNo);
            if(job.getToStation()==null ||
               job.getToStation().equals("")){
                return false;
            }
            //存入jobDetail
            jobDetail.setInventory(inventory);
            jobDetail.setQty(inventory.getQty());
            //存入job
            job.setContainer(inventory.getContainer().getBarcode());
            job.setMcKey(mckey);
            job.setOrderNo("4200026559");
            job.setSendReport(false);
            job.setStatus("1");
            job.setToStation(stationNo);
            job.setType(type);
            job.addJobDetail(jobDetail);
            job.setCreateDate(new Date());
            job.setFromLocation(inventory.getContainer().getLocation());
            //修改此托盘
            Container container = inventory.getContainer();
            container.setReserved(true);
            session.saveOrUpdate(container);
            } catch (JDBCConnectionException ex ) {
                ex.printStackTrace();
            }
            return true;
        }

    //获取站台集合
    public ReturnObj<List<Map<String,String>>> getStationNo(String stationNo){
        ReturnObj<List<Map<String,String>>> stationList=new ReturnObj<>();
        System.out.println("获取出库站台号方法");
        try {
           Transaction.begin();
           Session session=HibernateUtil.getCurrentSession();
           String hql="select stationNo , name from Station where type = '03' ";
           if(StringUtils.isNotBlank(stationNo)){
               hql+=" and stationNo= '"+stationNo+" '";
           }
           Query query=session.createQuery(hql);
           List<Object[]> stations=query.list();
           List<Map<String,String>> mapList=new ArrayList<>();
           for(Object[] obj:stations){
               Map<String,String> map=new HashMap<>();
               map.put("stationNo",obj[0].toString());
               map.put("name",obj[1].toString());
               mapList.add(map);
           }
            stationList.setSuccess(true);
            stationList.setRes(mapList);
           Transaction.commit();
        }catch (JDBCConnectionException je){
            stationList.setSuccess(false);
            stationList.setMsg(LogMessage.DB_DISCONNECTED.getName());
        }catch (Exception e) {
            Transaction.rollback();
            stationList.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
            e.printStackTrace();
        }
            return stationList;
    }

    //判断目标站台
//    public String  ToStation(String position,String stationNo,Job job) {
//        String toStation = "";
//        try {
//            Transaction.begin();
//            String[] footStation = {"1202", "1204", "1206"};
//            String[] topStation = {"1201", "1203", "1205"};
//            String[] leftStationNo = {"1201", "1202"};
//            String[] centerStationNo = {"1203", "1204"};
//            String[] rightStationNo = {"1205", "1206"};
//            Station station1303 = Station.getStation("1303");
//            List<String> topList = Arrays.asList(topStation);
//            List<String> footList = Arrays.asList(footStation);
//            List<String> leftList = Arrays.asList(leftStationNo);
//            List<String> centerList = Arrays.asList(centerStationNo);
//            List<String> rightList = Arrays.asList(rightStationNo);
//            Block stationBlock=Block.getByBlockNo(stationNo);
////            Station stationBlock = Station.getStation(stationNo);
//            if (station1303.is_direction() == true) {
//                if (position.equals("1") && !position.equals("2")) {
//                    if (footList.contains(stationNo)) {
//                        if (leftList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation = stationNo;
//                            } else {
//                                toStation = "";
//                            }
//                        } else if (centerList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation = stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else if (rightList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation = stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        }
//                    } else if (topList.contains(stationNo)) {
//                        if (leftList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else if (centerList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else if (rightList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        }
//                    }
//                } else if (position.equals("2") && !position.equals("1")) {
//                    if (footList.contains(stationNo)) {
//                        if (leftList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else {
//                            toStation="";
//                        }
//                    } else if (topList.contains(stationNo)) {
//                        if (leftList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else {
//                                toStation="";
//                        }
//                    }
//                }
//            } else if (station1303.is_direction() == false) {
//                if (position.equals("2") && !position.equals("1")) {
//                    if (footList.contains(stationNo)) {
//                        if (leftList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else if (centerList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else if (rightList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        }
//                    } else if (topList.contains(stationNo)) {
//                        if (leftList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else if (centerList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else if (rightList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        }
//                    }
//                } else if (position.equals("1") && !position.equals("2")) {
//                    if (footList.contains(stationNo)) {
//                        if (centerList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else if (rightList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else {
//                            toStation="";
//                        }
//                    } else if (topList.contains(stationNo)) {
//                        if (centerList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else if (rightList.contains(stationNo)) {
//                            if (stationBlock.isOutload() == false) {
//                                toStation=stationNo;
//                            } else {
//                                toStation="";
//                            }
//                        } else {
//                                toStation="";
//                        }
//                    }
//                }
//            } else {
//                if (stationNo == null || stationNo.equals("")) {
//                    toStation="";
//                }
//            }
//        } catch (JDBCConnectionException je) {
//            Transaction.rollback();
//            je.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//            return toStation;
//    }

}

