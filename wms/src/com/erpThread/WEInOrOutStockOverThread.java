package com.erpThread;

import com.asrs.business.consts.AsrsJobType;
import com.util.common.Const;
import com.util.hibernate.HibernateERPUtil;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.hibernate.TransactionERP;
import com.wms.domain.JobLog;
import com.wms.domain.RetrievalOrder;
import com.wms.domain.blocks.ETruck;
import com.wms.domain.erp.Truck;
import com.wms.domain.erp.WEConnect;
import com.wms.domain.erp.WEInStockOver;
import com.wms.domain.erp.WEOutStockOver;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 22:13 2018/7/27
 * @Description:
 * @Modified By:
 */
public class WEInOrOutStockOverThread implements Runnable {
    @Override
    public void run() {
        while (true){
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                TransactionERP.begin();
                Session sessionERP = HibernateERPUtil.getCurrentSession();

                WEConnect weConnect =WEConnect.getById(1);
                if(weConnect.isConnect()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    List<JobLog> jobLogList = JobLog.findUnReadJobLog();
                    if(jobLogList.size()!=0){
                        for(JobLog jobLog:jobLogList){
                            if(AsrsJobType.PUTAWAY.equals(jobLog.getType())){
                                WEInStockOver weInStockOver = new WEInStockOver();
                                weInStockOver.setBarcode(jobLog.getContainer());
                                weInStockOver.setCreateTime(sdf.format(new Date()));
                                weInStockOver.setLocationNo(jobLog.getToLocation());
                                weInStockOver.setNewLocationNo(jobLog.getToLocation());
                                weInStockOver.setStatus(0);
                                sessionERP.save(weInStockOver);
                            }else if(AsrsJobType.RETRIEVAL.equals(jobLog.getType())){
                                WEOutStockOver weOutStockOver = new WEOutStockOver();
                                weOutStockOver.setBarcode(jobLog.getContainer());
                                weOutStockOver.setCreateTime(sdf.format(new Date()));
                                weOutStockOver.setLocationNo(jobLog.getFromLocation());
                                weOutStockOver.setOutType(jobLog.getType());
                                weOutStockOver.setStatus(0);
                                weOutStockOver.setWareNum(jobLog.getQty().intValue());
                                weOutStockOver.setWareId(Integer.parseInt(jobLog.getSkuCode()));
                                if(StringUtils.isNotBlank(jobLog.getOrderNo()) ){
                                    if(Const.dingdian.equals(jobLog.getOrderNo())){
                                        weOutStockOver.setOrderNo("定点出库");
                                    }else{
                                        weOutStockOver.setOrderNo(jobLog.getOrderNo());
                                    }
                                    RetrievalOrder retrievalOrder = RetrievalOrder.getByOrderNo(jobLog.getOrderNo());
                                    if(retrievalOrder!=null){
                                        weOutStockOver.setTruckId(Integer.parseInt(retrievalOrder.getCarrierCar()));
                                    }
                                }
                                sessionERP.saveOrUpdate(weOutStockOver);
                            }

                            jobLog.setStatus("1");
                            session.saveOrUpdate(jobLog);
                        }
                    }
                }else{
                    System.out.println("WEInOrOutStockOverThread未与app连接！");
                }

                TransactionERP.commit();
                Transaction.commit();
            }catch (Exception e){
                Transaction.rollback();
                TransactionERP.rollback();
                e.printStackTrace();
            }finally {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
