package com.web.service;

import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Station;
import org.hibernate.Session;
import org.springframework.stereotype.Service;


@Service
public class PlatformSwitchService {
    /**
     * 进入页面查询站台模式，默认下拉框选择该模式
     * @param stationNo 站台
     * @return 模式编号
     */

    public ReturnObj<Station> findPlatformSwitch(String direction, String stationNo){
        System.out.println(stationNo);
        ReturnObj<Station> returnObj = new ReturnObj<>();
        try {
            Transaction.begin();
            Station station= (Station) HibernateUtil.getCurrentSession().createQuery("from Station s where stationNo = :stationNo").
                              setString("stationNo",stationNo).uniqueResult();
            returnObj.setRes(station);
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
            Station station=Station.getStation(stationNo);
            Session session=HibernateUtil.getCurrentSession();
            String[] rightFromStationNames ={"MC01","MC02","MC03","MC04"};
            String[] leftFromStationNames  ={"MC05","MC06","MC07","MC08"};
            String[] rightToStationNames   ={"1203","1204","1205","1206"};
            String[] leftToStationNames    ={"1201","1202"};
            String success1201="切换到1201 站台方向成功！";
            String fail1201="无法切换到1201 站台方向";
            String success1203="切换到1203 站台方向成功！";
            String fail1203="无法切换到1203站台方向";
            long total=(long)session.createQuery("select count(*) from AsrsJob aj  where aj.type = :type").
                            setString("type","03").
                            uniqueResult();
          if(total!=0) {
              long jobCount1,jobCount2;
              jobCount1 = (long) session.createQuery("select count(*) from AsrsJob j where j.fromStation in (:fromStation) " +
                      "and j.toStation in (:toStation)").
                      setParameterList("fromStation", rightFromStationNames).
                      setParameterList("toStation", leftToStationNames).
                      uniqueResult();
              jobCount2 = (long) session.createQuery("select count(*) from AsrsJob j where j.fromStation in (:fromStation) and j.toStation in (:toStation)").
                      setParameterList("fromStation", leftFromStationNames).
                      setParameterList("toStation", rightToStationNames).
                      uniqueResult();
              if ((jobCount1 > 0 && jobCount2==0) || (jobCount1>jobCount2)) {
                  if (direction.equals("1")) {
                      station.set_direction(true);
                      returnObj.setSuccess(true);
                      returnObj.setMsg(success1201);
                  } else{
                      station.set_direction(true);
                      returnObj.setSuccess(false);
                      returnObj.setMsg(fail1203);
                  }
              } else if ((jobCount2 > 0 && jobCount1==0) || (jobCount2>jobCount1)) {
                  if (direction.equals("0")) {
                      station.set_direction(false);
                      returnObj.setSuccess(true);
                      returnObj.setMsg(success1203);
                  }else  {
                      station.set_direction(false);
                      returnObj.setSuccess(false);
                      returnObj.setMsg(fail1201);
                  }
              } else{
                  if (direction.equals("1")) {
                      station.set_direction(true);
                      returnObj.setSuccess(true);
                      returnObj.setMsg(success1201);
                  }else {
                      station.set_direction(false);
                      returnObj.setSuccess(true);
                      returnObj.setMsg(success1203);
                  }
              }
              }else{
                    if (direction.equals("1")) {
                        station.set_direction(true);
                        returnObj.setSuccess(true);
                        returnObj.setMsg(success1201);
                    }else {
                        station.set_direction(false);
                        returnObj.setSuccess(true);
                        returnObj.setMsg(success1203);
                    }
               }
               Transaction.commit();
        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
            returnObj.setSuccess(false);
        }
            return returnObj;
    }
}
