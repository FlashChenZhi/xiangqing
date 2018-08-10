package com.erpThread;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.util.common.Const;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.hibernate.HibernateERPUtil;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.hibernate.TransactionERP;
import com.wms.domain.*;
import com.wms.domain.blocks.ETruck;
import com.wms.domain.erp.Truck;
import com.wms.domain.erp.WEConnect;
import com.wms.domain.erp.WEInStock;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: ed_chen
 * @Date: Create in 23:27 2018/7/26
 * @Description:
 * @Modified By:
 */
public class WEInStockThread implements Runnable{
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
                    List<WEInStock> weInStockList = WEInStock.findUnReadWEInStock();
                    if (weInStockList.size() != 0) {
                        for (WEInStock weInStock : weInStockList) {
                            Job job = Job.getByContainer(weInStock.getBarcode());
                            Container container = Container.getByBarcode(weInStock.getBarcode());
                            boolean flag = false;
                            if (job != null || container != null) {
                                System.out.println("托盘号已存在！");
                                LogWriter.error(LoggerType.ERROR, "托盘号已存在！");
                            } else {
                                String barcode = weInStock.getBarcode();
                                String skuCode = weInStock.getSkuCode() + "";
                                String batch = weInStock.getBatch();
                                String stationNo = weInStock.getLine() == 1 ? "1101" : "1102";
                                batch = batch + "Q" + barcode.substring(14, 15);
                                flag = createJob(stationNo, skuCode, barcode, batch);
                            }
                            if (flag) {
                                weInStock.setStatus(1);
                                sessionERP.saveOrUpdate(weInStock);
                            } else {
                                System.out.println("创建入库任务失败！");
                            }

                        }
                    }
                }else{
                    System.out.println("WEInStockThread未与app连接！");
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

    /*
     * @author：ed_chen
     * @date：2018/7/10 22:39
     * @description：创建job
     * @param stationNo
     * @return：com.wms.domain.Job
     */
    public boolean createJob(String stationNo,String skuCode,String barcode,String batch) throws Exception{
        Session session = HibernateUtil.getCurrentSession();
        //String barcode = null;
        boolean flag =false;
        Job job = new Job();
        Sku sku = Sku.getByCode(skuCode);
        session.save(job);
        job.setFromStation(stationNo);
        job.setContainer(barcode);//托盘号
        job.setCreateDate(new Date());

        job.setType(AsrsJobType.PUTAWAY);
        job.setMcKey(Mckey.getNext());
        job.setStatus(AsrsJobStatus.WAITING);
        job.setSkuCode(sku.getSkuCode());
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String lotNum=batch;
        /*if(sku.isManual()){
            //手动装态
            lotNum=sku.getLotNum();
        }*/
        job.setLotNum(lotNum);
        job.setBeLongTo("ERP");
        JobDetail jobDetail = new JobDetail();
        session.save(jobDetail);
        jobDetail.setJob(job);
        jobDetail.setQty(new BigDecimal(Const.containerQty));//托盘上的货物数量

        InventoryView inventoryView = new InventoryView();
        session.save(inventoryView);

        inventoryView.setPalletCode(barcode);//托盘号
        inventoryView.setQty(sku.getPalletLoadQTy());//托盘上的货物数量
        inventoryView.setSkuCode(sku.getSkuCode()); //商品代码
        inventoryView.setSkuName(sku.getSkuName());//商品名称
        inventoryView.setWhCode(Const.warehouseCode);//仓库代码
        inventoryView.setLotNum(lotNum);//批次号
        flag=true;
        return flag;
    }

}
