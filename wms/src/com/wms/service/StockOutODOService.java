package com.wms.service;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.RetrievalOrderStatus;
import com.asrs.business.consts.StationMode;
import com.util.common.LogMessage;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Inventory;
import com.wms.domain.RetrievalOrder;
import com.wms.domain.RetrievalOrderDetail;
import com.wms.domain.Station;
import net.sf.json.JSONArray;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

/**
 * @Author: ed_chen
 * @Date: Create in 18:10 2018/7/13
 * @Description:
 * @Modified By:
 */
@Service
public class StockOutODOService {

    /*
     * @author：ed_chen
     * @date：2018/7/14 20:54
     * @description：查询批次号
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<List<Map<String,String>>> getLotNums(){
        ReturnObj<List<Map<String, String>>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select lotNum from Inventory i group by lotNum");
            List<String> retList = query.list();
            List<Map<String,String>> mapList = new ArrayList<>();
            for (String object: retList) {
                Map<String, String> map = new HashMap();
                map.put("lotNum", object );
                mapList.add(map);
            }
            s.setSuccess(true);
            s.setRes(mapList);
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
     * @author：ed_chen
     * @date：2018/7/14 20:54
     * @description：根据批次号和货品skucode查询在各个巷道各有几板货
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<List<Map<String,Object>>> findNumBySkuAndLotNum(String skuCode,String lotNum){
        ReturnObj<List<Map<String,Object>>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select sum(i.qty) as count,i.container.location.position as position " +
                    "from Inventory i where i.lotNum=:lotNum and i.skuCode=:skuCode and i.container.reserved = false " +
                    "and not exists (select 1 from Location l where l.bay=i.container.location.bay and " +
                    "l.actualArea=i.container.location.actualArea and l.level =i.container.location.level and " +
                    "l.position=i.container.location.position and  l.seq > i.container.location.seq and " +
                    "l.reserved = true ) group by i.container.location.position ").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setParameter("skuCode", skuCode);
            query.setParameter("lotNum", lotNum);
            List<Map<String,Object>> retList = query.list();

            s.setSuccess(true);
            s.setRes(retList);
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
     * @author：ed_chen
     * @date：2018/7/14 20:54
     * @description：添加出库单
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<String> addOrder(String driver,String orderNo,
                 String createPerson,String placeOfArrival,String car,String zhantai,String data){
        ReturnObj<String> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String,String> stations = new HashMap<>();
            stations.put("1","1202");
            stations.put("2","1204");
            stations.put("3","1206");

            orderNo= URLDecoder.decode(orderNo,"utf-8"); //订单号
            driver= URLDecoder.decode(driver,"utf-8"); //驾驶员信息
            createPerson= URLDecoder.decode(createPerson,"utf-8"); //创建人
            placeOfArrival= URLDecoder.decode(placeOfArrival,"utf-8"); //到达地点
            car= URLDecoder.decode(car,"utf-8"); //车辆信息
            zhantai= URLDecoder.decode(zhantai,"utf-8"); //站台信息

            RetrievalOrder retrievalOrder1 = RetrievalOrder.getByOrderNo(orderNo);
            if(retrievalOrder1==null && !orderNo.equals("dingdian")){
                RetrievalOrder retrievalOrder = new RetrievalOrder();
                retrievalOrder.setOrderNo(orderNo);//订单号
                retrievalOrder.setJobType(AsrsJobType.RETRIEVAL);//订单类型
                retrievalOrder.setCreateDate(new Date());//创建时间
                retrievalOrder.setToLocation(placeOfArrival);//到达地点
                retrievalOrder.setCarrierName(driver);//驾驶人
                retrievalOrder.setCarrierCar(car);//车辆信息
                retrievalOrder.setToStation(stations.get(zhantai));//到达站台
                retrievalOrder.setStatus(RetrievalOrderStatus.WAITING);//定单状态，未完成
                retrievalOrder.setCoustomName(createPerson);//出库人
                session.save(retrievalOrder);
                JSONArray jsonArray = JSONArray.fromObject(data);
                List<Map<String,Object>> datalist = (List<Map<String,Object>>) JSONArray.toCollection(jsonArray,Map.class);
                for(Map<String,Object> map:datalist){
                    String skuCode = map.get("skuCode").toString();//出库商品代码
                    String lotNum = map.get("lotNum").toString();//出库商品批次
                    int qty = Integer.parseInt(map.get("qty").toString());//出库商品数量

                    int count = Inventory.getNumsBySkuCodeAndLotNum(skuCode,lotNum );//仓库拥有总库存
                    if(qty>count){
                        Transaction.rollback();
                        s.setSuccess(false);
                        s.setMsg("出库数量不能大于仓库存储数量！");
                        return s;
                    }
                    Station station1303 = Station.getStation("1303");

                    int onecount = Inventory.getNumsBySkuCodeAndLotNumAndPosition(skuCode,lotNum,"1" );//1巷道货物库存
                    int twocount = Inventory.getNumsBySkuCodeAndLotNumAndPosition(skuCode,lotNum,"2" );//2巷道货物库存
                    String position="0";
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
                        if(twocount<qty && "1".equals(zhantai)){
                            //2巷道的货不满足出库需要，并且需要出库到1号口
                            Transaction.rollback();
                            s.setSuccess(false);
                            s.setMsg("出库数量大于2巷道仓库存储数量，不能出到1号口，请更换出库站台或至出入库状态切换页面切换负责区域！");
                            return s;
                        }
                        if(twocount>=qty && "1".equals(zhantai)){
                            position="2";
                        }
                    }else if(StationMode.RETRIEVAL2.equals(station1303.getDirection())){
                        //入库状态，2巷道的货可以出到所有地方
                        if(onecount<qty && ("2".equals(zhantai)||"3".equals(zhantai))){
                            //2巷道的货不满足出库需要，并且需要出库到1号口
                            Transaction.rollback();
                            s.setSuccess(false);
                            s.setMsg("出库数量大于1巷道仓库存储数量，不能出到2,3号口，请更换出库站台或至出入库状态切换页面切换负责区域！");
                            return s;
                        }
                        if(onecount>=qty && ("2".equals(zhantai)||"3".equals(zhantai))){
                            position="1";
                        }
                    }

                    RetrievalOrderDetail retrievalOrderDetail = new RetrievalOrderDetail();
                    retrievalOrderDetail.setBatch(lotNum);
                    retrievalOrderDetail.setItemCode(skuCode);
                    retrievalOrderDetail.setQty(BigDecimal.valueOf(qty));
                    retrievalOrderDetail.setPosition(position);
                    retrievalOrderDetail.setRetrievalOrder(retrievalOrder);
                    session.save(retrievalOrderDetail);
                }

            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("已存在此订单号!");
                return s;
            }
            s.setSuccess(true);
            s.setMsg("设定出库单成功！");
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
}
