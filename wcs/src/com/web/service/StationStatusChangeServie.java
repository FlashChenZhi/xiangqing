package com.web.service;
import com.asrs.business.consts.StationMode;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.Station;

import com.asrs.message.Message03;
import com.asrs.message.Message40;
import com.thread.blocks.Conveyor;
import com.thread.blocks.StationBlock;
import com.util.common.Const;
import com.util.common.PagerReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.web.vo.ReturnObj;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StationStatusChangeServie {
    /**
     * 获取站台号
     * 1.根据站台号查询站台
     * 2.根据站台设定状态，并设定返回对象
     */
    public ReturnObj<String> findStatusChange(String stationNo){
        ReturnObj<String> stationReturnObj=new ReturnObj<>();
        try {
            Transaction.begin();
            System.out.println("开始查询站台号");
            Session session=HibernateUtil.getCurrentSession();
            Station station= (Station) session.createQuery("from Station s where s.stationNo = :stationNo").
                    setParameter("stationNo",stationNo).uniqueResult();
            if(station != null){
                stationReturnObj.setRes(station.isStatus()?"1":"0");
                stationReturnObj.setSuccess(true);
            }else{
                stationReturnObj.setSuccess(false);
            }

            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
            stationReturnObj.setSuccess(false);
        }
        return stationReturnObj;
    }

    /**
     * 更新站台状态
     * @return "0" 启用  "1" 禁用
     * 1.查询AsrsJob对象是否存在入库任务
     * 2.没有任务时可切换站台状态
     */
    public ReturnObj<String> updateStatusChange(String pattern, String stationNo) {
        ReturnObj<String> stationReturnObj = new ReturnObj();
        try {
            Transaction.begin();
            System.out.println("进入更新方法");
            Session session = HibernateUtil.getCurrentSession();
            String[] rightFromStationNames ={"MC01","MC02","MC03","MC04"};
            String[] leftFromStationNames  ={"MC05","MC06","MC07","MC08"};
            long asrsJobCount = (long) session.createQuery("select count(*) from AsrsJob aj where aj.type = :type ")
                    .setString("type", "01")
                    .uniqueResult();
            Station station = (Station) session.createQuery("from Station where stationNo = :stationNo")
                    .setString("stationNo", stationNo).uniqueResult();
            if(station!=null){
                if("0".equals(pattern)){
                    //禁用站台直接禁，需要在站台模式切换页面，去切换东西
                    station.setStatus(false);
                    stationReturnObj.setSuccess(true);
                    stationReturnObj.setRes(station.isStatus()?"1":"0");
                    stationReturnObj.setMsg("禁用"+stationNo+"成功！");
                }else{
                    //启用站台时
                    if(station.getStationNo().equals("1101")){
                        //1101站台，需要判断有没有通过1102站台到一号母车的任务若有不能切换
                        long count1=(long)session.createQuery("select count(*) from AsrsJob aj where aj.fromStation =:fromStation and " +
                                "aj.toStation in (:toStation)  and aj.type =:type ").
                                setString("type","01").
                                setString("fromStation","1102").
                                setParameterList("toStation",rightFromStationNames).uniqueResult();
                        if(count1>0){
                            //存在1102到1巷道的任务，不能启用
                            stationReturnObj.setSuccess(false);
                            stationReturnObj.setMsg("存在1102到1巷道的任务，不能启用"+stationNo+"！");
                        }else{
                            station.setStatus(true);
                            send40("1302",StationMode.RETRIEVAL2);
                            stationReturnObj.setSuccess(true);
                            stationReturnObj.setMsg("启用成功，已发送1302切换模式命令！");
                        }
                    }else if(station.getStationNo().equals("1102")){
                        //1102站台，需要判断有没有通过1101站台到五号母车的任务若有不能切换
                        long count1=(long)session.createQuery("select count(*) from AsrsJob aj where aj.fromStation =:fromStation and " +
                                "aj.toStation in (:toStation)  and aj.type =:type ").
                                setString("type","01").
                                setString("fromStation","1101").
                                setParameterList("toStation",leftFromStationNames).uniqueResult();
                        if(count1>0){
                            //存在1101到2巷道的任务，不能启用
                            stationReturnObj.setSuccess(false);
                            stationReturnObj.setMsg("存在1101到2巷道的任务，不能启用"+stationNo+"！");
                        }else{
                            station.setStatus(true);
                            send40("1301",StationMode.PUTAWAY);
                            stationReturnObj.setSuccess(true);
                            stationReturnObj.setMsg("启用成功，已发送1301切换模式命令！");
                        }
                    }
                }
            }else{
                stationReturnObj.setSuccess(false);
                stationReturnObj.setMsg("站台不存在！");
            }
            Transaction.commit();
      } catch (Exception e) {
            stationReturnObj.setSuccess(false);
            stationReturnObj.setMsg("系统错误！");
            Transaction.rollback();
            e.printStackTrace();
      }

      return stationReturnObj;

    }
    /*
     * @author：ed_chen
     * @date：2018/7/7 23:33
     * @description： 发送40
     * @param stationNo
     * @param mode
     * @return：void
     */
    public void  send40(String stationNo,String mode) throws Exception{
        Conveyor conveyor = Conveyor.getByStationNo(stationNo);
        Message40 m40 = new Message40();
        m40.setPlcName(conveyor.getPlcName());
        m40.Station = conveyor.getStationNo();
        m40.Mode = mode; //转向1巷道(5号母车方向)
        MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
        _wcsproxy.addSndMsg(m40);
    }



    /*
     * @author：ed_chen
     * @date：2018/3/4 17:49
     * @description： 查询站台状态
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public PagerReturnObj<List<Map<String,Object>>> findStationStatus(int startIndex, int defaultPageSize) {
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Map<String,String> swerveMap = new HashMap<>();
            swerveMap.put("01", "1巷道");
            swerveMap.put("02", "2巷道");

            Session session = HibernateUtil.getCurrentSession();
            Query query1 = session.createQuery("select s.stationNo as stationNo,case s.mode when '01' then " +
                    "'入库' when '03' then '出库' else '' end as mode,case s.status when true then '启用' " +
                    "when false then '禁用' end as status " +
                    "from Station s where s.type!='99' order by stationNo ").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

            Query query2 = session.createQuery("select count(*) as count from Station s where s.type!='99' ");

            query1.setFirstResult(startIndex);
            query1.setMaxResults(defaultPageSize);

            List<Map<String,Object>> jobList = query1.list();
            Long count = (Long) query2.uniqueResult();

            for(Map<String,Object> map : jobList){
                //获得正常的出入库站台
                Station station = Station.getStation(map.get("stationNo").toString());
                //对应的转向站台
                Station station1 = Station.getSwerveStationByGroupNo(station.getGroupNo());
                if(station.getMode().equals(StationMode.PUTAWAY)){
                    //若为入库站台

                    if(station1.getStationNo().equals("1301")){
                        map.put("name", "一号入库口");
                        //入库站台为1101，转向站台为1301
                        if(station1.getMode().equals(station1.getDirection())){
                            //若1301的direction为01，转向一巷道
                            map.put("putAwayArea", swerveMap.get(station1.getDirection()));
                        }else{
                            Station station1302 = Station.getStation("1302");
                            if(StationMode.RETRIEVAL2.equals(station1302.getDirection())){
                                map.put("putAwayArea", "2巷道");
                            }else{
                                map.put("putAwayArea", "入库路径冲突");
                            }
                        }
                    }else if(station1.getStationNo().equals("1302")){
                        map.put("name", "二号入库口");
                        //入库站台为1102，转向站台为1302
                        if(station1.getMode().equals(station1.getDirection())){
                            //若1302的direction为03，转向二巷道
                            map.put("putAwayArea", swerveMap.get(station1.getDirection()));
                        }else{
                            Station station1301 = Station.getStation("1301");
                            if(StationMode.PUTAWAY.equals(station1301.getDirection())){
                                map.put("putAwayArea", "1巷道");
                            }else{
                                map.put("putAwayArea", "入库路径冲突");
                            }
                        }
                    }
                    map.put("retrievalArea", "");
                }else{
                    //若为出库站台
                    if("1201".equals(station.getStationNo()) || "1202".equals(station.getStationNo())){
                        map.put("name", "一号出库口");
                        if(StationMode.PUTAWAY.equals(station1.getDirection())){
                            map.put("retrievalArea", "2巷道");
                        }else{
                            map.put("retrievalArea", "1,2巷道");
                        }
                    }else{
                        if("1203".equals(station.getStationNo()) || "1204".equals(station.getStationNo())){
                            map.put("name", "二号出库口");

                        }else if("1205".equals(station.getStationNo()) || "1206".equals(station.getStationNo())){
                            map.put("name", "三号出库口");
                        }
                        if(StationMode.PUTAWAY.equals(station1.getDirection())){
                            map.put("retrievalArea", "1,2巷道");
                        }else{
                            map.put("retrievalArea", "1巷道");
                        }
                    }
                }
            }
            returnObj.setSuccess(true);
            returnObj.setRes(jobList);
            returnObj.setCount(count);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg("JDBC连接错误");

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());
        }
        return returnObj;
    }

}
