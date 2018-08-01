package com.erpThread;

import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.hibernate.HibernateERPUtil;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.hibernate.TransactionERP;
import com.wms.domain.Container;
import com.wms.domain.Job;
import com.wms.domain.Sku;
import com.wms.domain.erp.WEInStock;
import com.wms.domain.erp.WESku;
import org.hibernate.Session;

import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 21:34 2018/7/27
 * @Description:
 * @Modified By:
 */
public class WESkuThread implements Runnable {
    @Override
    public void run() {
        while (true){
            try {
                Transaction.begin();
                TransactionERP.begin();
                Session session = HibernateUtil.getCurrentSession();
                Session sessionERP = HibernateERPUtil.getCurrentSession();

                List<WESku> weSkuList = WESku.findUnReadWESku();
                if(weSkuList.size()!=0){
                    for(WESku weSku:weSkuList){
                        Sku sku =Sku.getByCode(weSku.getId()+"");
                        boolean flag=false;
                        if(sku!=null ){
                            System.out.println("Sku:skuCode:"+weSku.getId()+"已存在！");
                            LogWriter.error(LoggerType.ERROR, "Sku:skuCode:"+weSku.getId()+"已存在！");
                        }else{
                            sku= new Sku();
                            sku.setManual(false);
                            sku.setSkuCode(weSku.getId()+"");
                            sku.setSkuName(weSku.getName());
                            sku.setSkuSpec(weSku.getModels());
                            sku.setSkuEom(weSku.getUnit());
                            session.save(sku);
                        }
                        weSku.setStatus(1);
                        sessionERP.saveOrUpdate(weSku);
                    }
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
