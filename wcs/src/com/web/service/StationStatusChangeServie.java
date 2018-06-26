package com.web.service;
import com.asrs.domain.Station;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.web.vo.ReturnObj;
import org.hibernate.Session;
import org.springframework.stereotype.Service;


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
         if (asrsJobCount == 0) {
              if(station !=null ){
                 if("0".equals(pattern)){
                      station.setStatus(false);
                 }else if("1".equals(pattern)){
                      station.setStatus(true);
                 }
                 stationReturnObj.setSuccess(true);
                 stationReturnObj.setRes(station.isStatus()?"1":"0");
                 stationReturnObj.setMsg("切换成功！");
              }
        }else {
         long count1=(long)session.createQuery("select count(*) from AsrsJob aj where aj.fromStation =:fromStation and " +
                        "aj.toStation in (:toStation)  and aj.type =:type ").
                        setString("type","01").
                        setString("fromStation","1101").
                        setParameterList("toStation",rightFromStationNames).uniqueResult();
         long  count2=(long)session.createQuery("select count(*) from AsrsJob aj where aj.fromStation =:fromStation and " +
                        "aj.toStation in (:toStation) and aj.type =:type").
                        setString("type","01").
                        setString("fromStation","1102").
                        setParameterList("toStation",leftFromStationNames).uniqueResult();
          if(count1>0){
              if(stationNo.equals("1102")) {
                  if ("0".equals(pattern)) {
                      station.setStatus(false);
                      stationReturnObj.setSuccess(true);
                      stationReturnObj.setMsg("禁用1102站台成功！^-^");
                  } else if ("1".equals(pattern)) {
                      station.setStatus(false);
                      stationReturnObj.setSuccess(false);
                      stationReturnObj.setMsg("启用1102站台失败！");
                  }
              }else if(stationNo.equals("1101")) {
                  if ("0".equals(pattern)) {
                      station.setStatus(true);
                      stationReturnObj.setSuccess(false);
                      stationReturnObj.setMsg("目前存在入库任务，无法禁用1101站台！");
                  }else if("1".equals(pattern)){
                      station.setStatus(true);
                      stationReturnObj.setSuccess(true);
                      stationReturnObj.setMsg("启用1101站台成功！^-^");
                  }
              }
            }else if(count2>0){
              if(stationNo.equals("1101")) {
                  if ("0".equals(pattern)) {
                      station.setStatus(false);
                      stationReturnObj.setSuccess(true);
                      stationReturnObj.setMsg("禁用1101站台成功！^-^");
                  } else if ("1".equals(pattern)) {
                      station.setStatus(false);
                      stationReturnObj.setSuccess(false);
                      stationReturnObj.setMsg("启用1101站台失败！");
                  }
              }else if(stationNo.equals("1102")){
                  if ("0".equals(pattern)) {
                      station.setStatus(true);
                      stationReturnObj.setSuccess(false);
                      stationReturnObj.setMsg("目前存在入库任务，无法禁用1102站台！");
                  } else if ("1".equals(pattern)) {
                      station.setStatus(true);
                      stationReturnObj.setSuccess(true);
                      stationReturnObj.setMsg("启用1102站台成功！^-^");
                  }
              }
          }
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

}
