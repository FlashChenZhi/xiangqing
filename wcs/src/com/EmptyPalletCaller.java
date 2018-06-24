package com;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Container;
import com.asrs.domain.Job;
import com.asrs.domain.Location;
import com.thread.blocks.MCar;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

public class EmptyPalletCaller {
    public static void main(String[] args) {
        Map<String,Integer> stations = new HashMap<>();
        stations.put("0051",5);
        stations.put("0053",2);
        while (true) {
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                for(String station : stations.keySet()){
                    Query query = session.createQuery("from AsrsJob aj where aj.toStation = :station")
                            .setString("station",station);
                    List<AsrsJob> asrsJobs = query.list();

                    int maxCount = stations.get(station);
                    if(asrsJobs.size() < maxCount){
                            query = HibernateUtil.getCurrentSession().createQuery("from Location l where l.id in (" +
                                    "select i.container.location.id from Inventory i where i.skuCode='ktp') and l.bay>=13 and l.bay<=22 and l.level=1 and " +
                                    "l.position=1 and (l.bank<=8 or l.bank>=16) and l.empty=false and l.reserved=false order by l.bay asc,l.bank asc ").setMaxResults(1);
                            Location location = (Location) query.uniqueResult();
                            if (location!=null){
                                location.setReserved(true);
                                MCar mcar = MCar.getMCarByPosition(org.apache.commons.lang3.StringUtils.isNotBlank(location.getOutPosition()) ? location.getOutPosition() : location.getPosition(), location.getLevel());
                                Query query1 = HibernateUtil.getCurrentSession().createQuery("from Container where locationId=:id").setMaxResults(1);
                                query1.setParameter("id",location.getId());
                                Container container = (Container) query1.uniqueResult();
                                AsrsJob asrsJob=new AsrsJob();
                                asrsJob.setFromLocation(location.getLocationNo());
                                asrsJob.setFromStation(mcar.getBlockNo());
                                asrsJob.setStatus("1");
                                asrsJob.setStatusDetail("0");
                                asrsJob.setGenerateTime(new Date());
                                asrsJob.setToStation(station);
                                asrsJob.setType("03");
                                String mc= StringUtils.leftPad(HibernateUtil.nextSeq("seq_mckey") + "", 4, "0");
                                if(StringUtils.isNotBlank(container.getBarcode()))
                                    asrsJob.setBarcode(container.getBarcode());
                                asrsJob.setMcKey(mc);
                                HibernateUtil.getCurrentSession().save(asrsJob);
                                Job job =new Job();
                                job.setToStation(station);
                                job.setType(AsrsJobType.RETRIEVAL);
                                job.setCreateDate(new Date());
                                job.setMcKey(mc);
                                if(StringUtils.isNotBlank(container.getBarcode()))
                                    job.setContainer(container.getBarcode().trim());
                                job.setFromStation(mcar.getBlockNo());
                                job.setFromLocation(location);
                                //job.setOrderNo(String.valueOf(object.get("TaskDetailID")));
                                HibernateUtil.getCurrentSession().save(job);
                            }


                    }
                }



                Transaction.commit();
            } catch (Exception ex) {
                Transaction.rollback();
                ex.printStackTrace();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
