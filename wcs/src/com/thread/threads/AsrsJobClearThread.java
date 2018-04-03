package com.thread.threads;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.WcsMessage;
import com.thread.blocks.Block;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;

import java.util.List;

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

                Query msgQuery = HibernateUtil.getCurrentSession().createQuery("from WcsMessage wm where not exists(select aj.id from AsrsJob aj where aj.mcKey = wm.mcKey)");
                List<WcsMessage> wms = msgQuery.list();
                for(WcsMessage wm : wms){
                    HibernateUtil.getCurrentSession().delete(wm);
                }
                Transaction.commit();
            } catch (Exception ex) {
                Transaction.rollback();
                ex.printStackTrace();

            } finally {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
