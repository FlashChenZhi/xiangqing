package com.thread.threads;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.WcsMessage;
import com.thread.blocks.Block;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;

import java.util.List;
import org.hibernate.exception.LockAcquisitionException;

/**
 * @Author: ed_chen
 * @Date: Create in 14:53 2018/3/4
 * @Description:
 * @Modified By:
 */
public class AsrsJobClearThread {
    public static void main(String[] args) {
        while (true){
            try {
                Transaction.begin();
                Query jobQuery = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where status=:status").setParameter("status", AsrsJobStatus.DONE);
                List<AsrsJob> jobs = jobQuery.list();
                for (AsrsJob job : jobs) {
                    Query query = HibernateUtil.getCurrentSession().createQuery("from Block where reservedMcKey=:mckey or mcKey=:mckey");
                    query.setParameter("mckey", job.getMcKey());
                    List<Block> blocks = query.list();
                    if (blocks.isEmpty()) {
                        job.delete();
                    }
                }

                //Query msgQuery = HibernateUtil.getCurrentSession().createQuery("from WcsMessage wm where not exists(select aj.id from AsrsJob aj where aj.mcKey = wm.mcKey) and wm.received=true ");
                Query msgQuery = HibernateUtil.getCurrentSession().createQuery("select wa from WcsMessage wa," +
                        "WcsMessage wb where not exists(select aj.id from AsrsJob aj where " +
                        "aj.mcKey = wa.mcKey) and wa.mcKey=wb.mcKey  and " +
                        "wa.dock=wb.dock and wa.machineNo =wb.machineNo and wa.id != wb.id and " +
                        "wb.received=true and wa.received = true");
                List<WcsMessage> wms = msgQuery.list();
                for(WcsMessage wm : wms){
                    HibernateUtil.getCurrentSession().delete(wm);
                }
//                Query msgQuery2 = HibernateUtil.getCurrentSession().createQuery("select wa from WcsMessage wa " +
//                        "where not exists(select aj.id from AsrsJob aj where aj.mcKey = wa.mcKey) and not exists " +
//                        "(select wb.id from WcsMessage wb where wa.mcKey=wb.mcKey and wa.dock=wb.dock and " +
//                        "wa.machineNo =wb.machineNo and wb.msgType=:msgType1 ) and wa.msgType=:msgType2 and " +
//                        "wa.received = true");
//                msgQuery2.setString("msgType1", WcsMessage.MSGTYPE_03);
//                msgQuery2.setString("msgType2", WcsMessage.MSGTYPE_35);
//                List<WcsMessage> wms2 = msgQuery2.list();
//                for(WcsMessage wm : wms2){
//                    HibernateUtil.getCurrentSession().delete(wm);
//                }
                Transaction.commit();
            } catch (LockAcquisitionException e) {
                Transaction.rollback();
                e.printStackTrace();
                LogWriter.error(LoggerType.ERROR, LogWriter.getStackTrace(e));
                LogWriter.error(LoggerType.ERROR,String.format("AsrsjobClearThread LockAcquisitionException"));
                try {
                    Thread.sleep(7000);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }catch (Exception ex) {
                Transaction.rollback();
                ex.printStackTrace();

            } finally {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
