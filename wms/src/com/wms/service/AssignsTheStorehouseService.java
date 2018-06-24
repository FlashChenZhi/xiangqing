package com.wms.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.common.Const;
import com.util.common.LogMessage;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.Block;
import com.wms.domain.blocks.MCar;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: ed_chen
 * @Date: Create in 15:20 2018/4/8
 * @Description:
 * @Modified By:
 */
@Service
public class AssignsTheStorehouseService {

    /*
     * @author：ed_chen
     * @date：2018/4/27 15:13
     * @description：获取商品代码
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<List<Map<String, String>>> getSkuCode(){
        ReturnObj<List<Map<String, String>>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select skuCode,skuName from Sku");
            List<Object[]> retList = query.list();
            List<Map<String,String>> mapList = new ArrayList<>();
            for (Object[] objects: retList) {
                Map<String, String> map = new HashMap();
                map.put("skuCode", objects[0].toString() );
                map.put("skuName",objects[1].toString() );
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
     * @date：2018/4/10 16:16
     * @description：初始化map
     * @param productId
     * @param tier
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> getStorageLocationData(String productId, String tier){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            String bank ="bay";
            String bay ="bank";
            String database ="xiangqing.dbo";
            //查询总货位
            Query query1 = session.createQuery("select convert(varchar,"+bank+")+'_'+convert(varchar,"+bay+") as coordinate from Location where " +
                    "putawayRestricted = false and retrievalRestricted = false and level=:level  order by "+bank+" , "+bay+" ");
            query1.setString("level", tier);
            List<String> list = query1.list();
            int bankCount = Const.bankCount;
            int bayCount = Const.bayCount;
            List<String> LocationList = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            for(int i = 1;i<=bayCount;i++){
                StringBuffer sb = new StringBuffer();
                for(int j =1; j<=bankCount;j++){
                    if(list.contains(i+"_"+j)){
                        sb.append("a");
                    }else{
                        sb.append("_");
                    }
                }
                LocationList.add(sb.toString());
            }

            //查询空货位
            Query query2 = session.createQuery("select convert(varchar,a."+bank+")+'_'+convert(varchar,a."+bay+") as coordinate from Location a where " +
                    "a.empty = true and a.level = :level ");
            query2.setString("level", tier);

            //判断可以定点出库的货位
            StringBuffer sb = new StringBuffer(
                    " select convert(varchar,e."+bank+")+'_'+convert(varchar,e."+bay+") as coordinate from "+database+".Inventory i , "+database+".CONTAINER d " +
                    ",( " +
                        "select a.id as id ,a.BANK,a.BAY from "+database+".location a , " +
                            "(select max(seq) as seq,bay,POSITION,AREA " +
                            "from "+database+".LOCATION where empty=0 and lev=:level   and PUTAWAYRESTRICTED=0 and " +
                            "RETRIEVALRESTRICTED=0 and ABNORMAL =0 group by bay,POSITION,AREA " +
                            ") b " +
                        "where a.seq=b.seq and a.bay = b.bay and " +
                        "a.position = b.position and a.area = b.area and a.lev=:level and PUTAWAYRESTRICTED=0 and " +
                        "RETRIEVALRESTRICTED=0 and ABNORMAL =0 and  not exists( " +
                            "select 1 from "+database+".Location l where l.bay=a.bay and " +
                            "l.area=a.area and l.lev =a.lev " +
                            "and  l.position=a.position  and " +
                            " l.seq > a.seq and l.reserved = 1 " +
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
            Query query4 = session.createQuery("select convert(varchar,c.location."+bank+")+'_'+convert(varchar,c.location."+bay+") as coordinate from Container c where " +
                    " c.location.level = :level and c.reserved = true  ");
            query4.setString("level", tier);

            //查询已经有入库任务的货位
            Query query5 = session.createQuery("select convert(varchar,l."+bank+")+'_'+convert(varchar,l."+bay+") as coordinate from Location l where " +
                    " l.level = :level and l.reserved = true  ");
            query5.setString("level", tier);

            map.put("map", LocationList); //总货位
            map.put("emptyList", query2.list()); //空货位
            map.put("availableList", query3.list()); //可被选择的货位
            map.put("reservedOutList", query4.list()); //已经有任务的货位
            map.put("reservedInList", query5.list()); //已经有任务的货位
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
     * @author：ed_chen
     * @date：2018/4/10 16:16
     * @description：查询货位货物信息
     * @param productId
     * @param tier
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> getLocationInfo(String bank, String bay, String level){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询货位
            Query query = session.createQuery("select i.skuCode as skuCode,i.skuName as skuName, " +
                    "i.lotNum as lotNum,i.qty as qty,i.container.barcode as barcode, " +
                    "i.container.location.bank as bank,i.container.location.bay as bay, " +
                    "i.container.location.level as level from Inventory i where i.container.location.bank " +
                    "= :bank and i.container.location.bay = :bay and i.container.location.level = :level").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setInteger("bank", Integer.parseInt(bank));
            query.setInteger("bay", Integer.parseInt(bay));
            query.setInteger("level", Integer.parseInt(level));

            List<Map<String,Object>> mapList = query.list();
            Map<String,Object> map = new HashMap<>();
            if(mapList.size()==0){
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
     * @author：ed_chen
     * @date：2018/4/10 18:27
     * @description：查询下一个可选货位信息
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> getNextAvailableLocation(String bank, String bay, String level){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询货位
            Query query = session.createQuery("select convert(varchar,a.bank)+'_'" +
                    "+convert(varchar,a.bay) as coordinate from Location a " +
                    "where a.position=(select position from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.actualArea = (select actualArea from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.seq2=(select seq2+1 from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
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
     * @author：ed_chen
     * @date：2018/4/10 19:27
     * @description：获取里面的货位代码
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> getAgoUnavailableLocation(String bank, String bay, String level){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询货位
            Query query = session.createQuery("select convert(varchar,a.bank)+'_'" +
                    "+convert(varchar,a.bay) as coordinate from Location a " +
                    "where a.position=(select position from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.actualArea = (select actualArea from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.seq2>(select seq2 from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
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
     * @author：ed_chen
     * @date：2018/4/11 10:28
     * @description：设定出库任务
     * @param selectLocation
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> assignsTheStorehouse(String selectLocation){

        ReturnObj<Map<String, Object>> s = new ReturnObj();
        JSONArray jsonArray = JSONArray.fromObject(selectLocation);
        List<String> list = (List<String>) JSONArray.toCollection(jsonArray,String.class);
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            boolean flag = true;
            String location="";
            for(int i =0;i<list.size();i++){
                location = list.get(i);
                //按照Container中的reserved判断
                Query query = session.createQuery(" from Container c where reserved = false and c.location.locationNo=:locationNo");

                query.setString("locationNo", location);
                Container container =(Container) query.uniqueResult();

                if(container!=null){
                    outKu(session,location);
                }else{
                    flag=false;
                    break;
                }
            }
            if(flag){
                s.setMsg("设定出库成功");
                s.setSuccess(true);
                Transaction.commit();
            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("货位："+location+"前已有入库任务");
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

    public void outKu(Session session, String locationNo){
        Query query2 = session.createQuery("from Inventory i where i.container.location.locationNo = :locationNo");
        query2.setString("locationNo",locationNo);
        List<Inventory> inventoryList = query2.list();
        Inventory inventory =inventoryList.get(0);
        int qty = inventory.getQty().intValue();//货品数量

        Location fromLocation =inventory.getContainer().getLocation();
        String position = fromLocation.getOutPosition();

        JobDetail jobDetail = new JobDetail();
        Job job = new Job();
        //session准备存入job，commit时才会执行sql
        session.save(job);
        session.save(jobDetail);
        //数据准备
        //查找所出货物的所在层的母车
        MCar mCar = MCar.getMCarByPosition(fromLocation.getPosition(), fromLocation.getLevel());
        String mckey = Mckey.getNext();
        //查询1303状态
        Station station = Station.getStation("1303");
        //未完善
        /*if(station.get){

        }*/
        String toStation = position.equals("1") ? "1201" : "1301";//到达站台
        String fromStation = mCar.getBlockNo();//出发地点
        String type = AsrsJobType.RETRIEVAL; //出库
        //存入jobDetail
        jobDetail.setInventory(inventory);
        jobDetail.setQty(inventory.getQty());
        //存入job
        job.setContainer(inventory.getContainer().getBarcode());
        job.setFromStation(fromStation);
        job.setMcKey(mckey);
        job.setOrderNo("4200026559");
        //job.setSendReport(false);
        job.setStatus("1");
        job.setToStation(toStation);
        job.setType(type);
        job.addJobDetail(jobDetail);
        job.setCreateDate(new Date());
        job.setFromLocation(inventory.getContainer().getLocation());

        //修改此托盘
        Container container = inventory.getContainer();
        container.setReserved(true);
        session.saveOrUpdate(container);
    }
}
