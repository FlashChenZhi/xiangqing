package com.erpThread;

import com.util.hibernate.HibernateERPUtil;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.hibernate.TransactionERP;
import com.wms.domain.blocks.ETruck;
import com.wms.domain.erp.Truck;
import com.wms.domain.erp.WEConnect;
import org.hibernate.Session;

import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 22:05 2018/7/26
 * @Description:
 * @Modified By:
 */
public class TruckThread implements Runnable{
    @Override
    public void run() {
        while (true){
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                TransactionERP.begin();
                Session sessionERP = HibernateERPUtil.getCurrentSession();

                WEConnect weConnect =WEConnect.getById(1);
                if(weConnect.isConnect()){
                    List<Truck> truckList = Truck.findUnReadTruck();
                    if(truckList.size()!=0){
                        for(Truck truck:truckList){
                            ETruck eTruck=ETruck.findETruckByMark(truck.getMark());
                            if(eTruck!=null){
                                eTruck.setIsDel(truck.getIsDel());
                            }else{
                                eTruck=new ETruck();
                                eTruck.setIsDel(truck.getIsDel());
                                eTruck.setMark(truck.getMark());
                                eTruck.seteId(truck.getId());
                            }
                            truck.setStatus(1);
                            session.saveOrUpdate(eTruck);
                            sessionERP.saveOrUpdate(truck);
                        }
                    }

                }else{
                    System.out.println("TruckThread未与app连接！");
                }
                Transaction.commit();
                TransactionERP.commit();
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
