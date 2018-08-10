package com.erpThread;

import com.util.hibernate.HibernateERPUtil;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.hibernate.TransactionERP;
import com.wms.domain.blocks.EOutStock;
import com.wms.domain.erp.WEConnect;
import com.wms.domain.erp.WEOutStock;
import org.hibernate.Session;

import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 21:51 2018/7/27
 * @Description:
 * @Modified By:
 */
public class WEOutStockThread implements Runnable {
    @Override
    public void run() {
        while (true){
            try {
                Transaction.begin();
                TransactionERP.begin();
                Session session = HibernateUtil.getCurrentSession();
                Session sessionERP = HibernateERPUtil.getCurrentSession();

                WEConnect weConnect =WEConnect.getById(1);
                if(weConnect.isConnect()) {
                    List<WEOutStock> weOutStockList = WEOutStock.findUnReadWEOutStock();
                    if (weOutStockList.size() != 0) {
                        for (WEOutStock weOutStock : weOutStockList) {

                            EOutStock eOutStock = new EOutStock();
                            eOutStock.setCreateTime(weOutStock.getCreateTime());
                            eOutStock.setPerson(weOutStock.getPerson());
                            eOutStock.setWareId(weOutStock.getWareId());
                            eOutStock.setWareNum(weOutStock.getWareNum());
                            eOutStock.setStatus(weOutStock.getStatus());
                            session.save(eOutStock);

                            weOutStock.setStatus(1);
                            sessionERP.saveOrUpdate(weOutStock);
                        }
                    }
                }else{
                    System.out.println("WEOutStockThread未与app连接！");
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
