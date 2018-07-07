package com.web.service;

import com.asrs.business.consts.StationMode;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.Station;

import com.asrs.message.Message40;
import com.thread.blocks.Conveyor;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.web.vo.ReturnObj;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PlatformSwitchService {
    /**
     * 进入页面查询站台模式，默认下拉框选择该模式
     * @param stationNo 站台
     * @return 模式编号
     */

    public ReturnObj<Map<String,Object>> findPlatformSwitch(String stationNo){
        System.out.println(stationNo);
        ReturnObj<Map<String,Object>> returnObj = new ReturnObj<>();
        Map<String,Object> map = new HashMap<>();
        List<Map<String,Object>> mapList = new ArrayList<>();

        try {
            Transaction.begin();
            Station station= (Station) HibernateUtil.getCurrentSession().createQuery("from Station s where stationNo = :stationNo").
                              setString("stationNo",stationNo).uniqueResult();
            //对应的转向站台
            Station station1 = Station.getSwerveStationByGroupNo(station.getGroupNo());

            Map<String,Object> mapPut01 = new HashMap<>();
            Map<String,Object> mapPut03 = new HashMap<>();
            if(station.getMode().equals(StationMode.PUTAWAY)){
                mapPut01.put("direction", "01");
                mapPut01.put("name", "1巷道");
                mapPut03.put("direction", "03");
                mapPut03.put("name", "2巷道");
            }else{
                //若为出库站台
                if("1201".equals(station.getStationNo()) || "1202".equals(station.getStationNo())){
                    mapPut01.put("direction", "01");
                    mapPut01.put("name", "2巷道");
                    mapPut03.put("direction", "03");
                    mapPut03.put("name", "1,2巷道");
                }else{
                    mapPut01.put("direction", "01");
                    mapPut01.put("name", "1,2巷道");
                    mapPut03.put("direction", "03");
                    mapPut03.put("name", "1巷道");
                }
            }
            mapList.add(mapPut01);
            mapList.add(mapPut03);
            map.put("selectRows", mapList);
            map.put("selectRowsFirst", station1.getDirection());
            map.put("updateStation", station1.getStationNo());
            returnObj.setRes(map);
            returnObj.setSuccess(true);
            Transaction.commit();

        }catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
            returnObj.setSuccess(false);
        }

        return returnObj;
     }

    /**
     * 站台模式切换更新
     * @param direction 模式
     * @param stationNo 站台ID
     * @return "0"设定成功，"1"设定失败
     */
    public ReturnObj<String> updatePlatformSwitch(String direction,String stationNo){
        ReturnObj<String> returnObj = new ReturnObj<>();
        try {
            Transaction.begin();

            Session session=HibernateUtil.getCurrentSession();
            String[] rightFromStationNames ={"MC01","MC02","MC03","MC04"};
            String[] leftFromStationNames  ={"MC05","MC06","MC07","MC08"};
            stationNo=Station.getSwerveStationByGroupNo(Station.getStation(stationNo).getGroupNo()).getStationNo();
            if(stationNo!=null){
                Conveyor conveyor=Conveyor.getByStationNo(stationNo);
                Station station=Station.getStation(stationNo);
                if(conveyor!=null){
                    if(conveyor.getStationNo().equals("1301")){
                        //站台是1301
                        Station station1302=Station.getStation("1302");
                        Station station1101=Station.getStation("1101");
                        if(station1302.getDirection().equals(StationMode.PUTAWAY) && StationMode.RETRIEVAL.equals(direction)){
                            returnObj.setSuccess(false);
                            returnObj.setMsg("路径交叉不能切换");
                        }else if(station1101.isStatus() && StationMode.RETRIEVAL.equals(direction)){
                            returnObj.setSuccess(false);
                            returnObj.setMsg(station1101.getStationNo()+"启用状态，不能将"+station.getStationNo()+"其转向2巷道");

                        }else{
                            if(StationMode.PUTAWAY.equals(direction)){
                                //想将1301转向1巷道
                                long count1=(long)session.createQuery("select count(*) from AsrsJob aj where aj.fromStation =:fromStation and " +
                                        "aj.toStation in (:toStation)  and aj.type =:type ").
                                        setString("type","01").
                                        setString("fromStation","1101").
                                        setParameterList("toStation",leftFromStationNames).uniqueResult();
                                if(count1>0){
                                    //存在1101向2巷道的入库任务，不能切换为转向一巷道
                                    returnObj.setSuccess(false);
                                    returnObj.setMsg("存在1101向2巷道的入库任务，不能切换为转向1巷道");
                                }else{
                                    send40(stationNo,direction);
                                }
                            }else if(StationMode.RETRIEVAL.equals(direction)){
                                //想将1301转向2巷道
                                long count1=(long)session.createQuery("select count(*) from AsrsJob aj where (aj.fromStation =:fromStation " +
                                        "or aj.fromStation =:fromStation2) and aj.toStation in (:toStation)  and aj.type =:type ").
                                        setString("type","01").
                                        setString("fromStation","1101").
                                        setString("fromStation2","1102").
                                        setParameterList("toStation",rightFromStationNames).uniqueResult();
                                if(count1>0){
                                    //存在1101向2巷道的入库任务，不能切换为转向一巷道
                                    returnObj.setSuccess(false);
                                    returnObj.setMsg("存在1101或1102向1巷道的入库任务，不能切换为转向2巷道");
                                }else{
                                    send40(stationNo,direction);
                                }
                            }
                        }
                    }else if(conveyor.getStationNo().equals("1302")){
                        //站台是1302
                        Station station1301=Station.getStation("1301");
                        Station station1102=Station.getStation("1102");
                        if(station1301.getDirection().equals(StationMode.RETRIEVAL) && StationMode.PUTAWAY.equals(direction)){
                            returnObj.setSuccess(false);
                            returnObj.setMsg("路径交叉不能切换");
                        }else if(station1102.isStatus() && StationMode.PUTAWAY.equals(direction)){
                            returnObj.setSuccess(false);
                            returnObj.setMsg(station1102.getStationNo()+"启用状态，不能将"+station.getStationNo()+"其转向1巷道");

                        }else{
                            if(StationMode.RETRIEVAL.equals(direction)){
                                //想将1302转向2巷道
                                long count1=(long)session.createQuery("select count(*) from AsrsJob aj where aj.fromStation =:fromStation and " +
                                        "aj.toStation in (:toStation)  and aj.type =:type ").
                                        setString("type","01").
                                        setString("fromStation","1102").
                                        setParameterList("toStation",rightFromStationNames).uniqueResult();
                                if(count1>0){
                                    //存在1101向2巷道的入库任务，不能切换为转向一巷道
                                    returnObj.setSuccess(false);
                                    returnObj.setMsg("存在1102向1巷道的入库任务，不能切换为转向2巷道");
                                }else{
                                    send40(stationNo,direction);
                                }
                            }else if(StationMode.PUTAWAY.equals(direction)){
                                //想将1301转向1巷道
                                long count1=(long)session.createQuery("select count(*) from AsrsJob aj where (aj.fromStation =:fromStation " +
                                        "or aj.fromStation =:fromStation2) and aj.toStation in (:toStation)  and aj.type =:type ").
                                        setString("type","01").
                                        setString("fromStation","1101").
                                        setString("fromStation2","1102").
                                        setParameterList("toStation",leftFromStationNames).uniqueResult();
                                if(count1>0){
                                    //存在1101向2巷道的入库任务，不能切换为转向一巷道
                                    returnObj.setSuccess(false);
                                    returnObj.setMsg("存在1101或1102向2巷道的入库任务，不能切换为转向1巷道");
                                }else{
                                    send40(stationNo,direction);
                                }
                            }
                        }
                    }else if(conveyor.getStationNo().equals("1303")) {
                        //站台是1303
                        if (StationMode.RETRIEVAL.equals(direction)) {
                            //想将1302转向2巷道
                            long count1 = (long) session.createQuery("select count(*) from AsrsJob aj where aj.fromStation in (:fromStation) and " +
                                    "(aj.toStation =:toStation or aj.toStation =:toStation2)  and aj.type =:type ").
                                    setString("type", "03").
                                    setString("toStation", "1204").
                                    setString("toStation2", "1206").
                                    setParameterList("fromStation", leftFromStationNames).uniqueResult();
                            if (count1 > 0) {
                                //存在2巷道向1204或1206的出库任务，不能切换为转向1巷道
                                returnObj.setSuccess(false);
                                returnObj.setMsg("存在2巷道向1204或1206的出库任务，不能切换为转向2巷道");
                            } else {
                                send40(stationNo, direction);
                            }
                        } else if (StationMode.PUTAWAY.equals(direction)) {
                            //想将1301转向1巷道
                            long count1 = (long) session.createQuery("select count(*) from AsrsJob aj where aj.fromStation in (:fromStation) and " +
                                    "aj.toStation =:toStation  and aj.type =:type ").
                                    setString("type", "03").
                                    setString("toStation", "1202").
                                    setParameterList("fromStation", rightFromStationNames).uniqueResult();
                            if (count1 > 0) {
                                //存在1巷道向1202的出库任务，不能切换为转向2巷道
                                returnObj.setSuccess(false);
                                returnObj.setMsg("存在1巷道向1202的出库任务，不能切换为转向1巷道");
                            } else {
                                send40(stationNo, direction);
                            }
                        }

                    }
                }else{
                    returnObj.setSuccess(false);
                    returnObj.setMsg("转向站台不存在");
                }
            }else{
                returnObj.setSuccess(false);
                returnObj.setMsg("不存在绑定转向站台");
            }

            Transaction.commit();
        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
            returnObj.setSuccess(false);
        }
        return returnObj;
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
}
