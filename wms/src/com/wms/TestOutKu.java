package com.wms;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.MCar;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class TestOutKu {

    public static final String LocationNo = "014018002";

    public static void main(String[] args) {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query2 = HibernateUtil.getCurrentSession().createQuery("from Location l where l.locationNo = :locationNo")
                    .setString("locationNo","109002001");

            List<Location> locations = query2.list();
            for(Location location : locations) {
                MCar mCar = MCar.getMCarByPosition(StringUtils.isNotBlank(location.getOutPosition()) ? location.getOutPosition() : location.getPosition(),location.getLevel());

                Configuration configuration = Configuration.getConfig(mCar.getBlockNo());
                String[] stations = configuration.getValue().split(",");
                String toStation = "";
                int jobCount = 9999;
                for(String stationNo : stations){
                    Query q = session.createQuery("from Job j where j.toStation = :stationNo")
                            .setString("stationNo",stationNo);
                    int count = q.list().size();
                    if(jobCount > count){
                        jobCount = count;
                        toStation = stationNo;
                    }
                }
                Container container = location.getContainers().iterator().next();
                if(container != null){
                    Inventory inventory = container.getInventories().iterator().next();
                    if(inventory != null) {
                        Job job = new Job();

                        job.setType(AsrsJobType.RETRIEVAL);
                        job.setCreateDate(new Date());
                        job.setMcKey(Mckey.getNext());
                        job.setContainer(container.getBarcode());
                        job.setFromStation(mCar.getBlockNo());
                        job.setQty(inventory.getQty());
                        job.setToStation(toStation);
                        job.setFromLocation(location);
                        job.setOrderNo("");

                        HibernateUtil.getCurrentSession().save(job);


                        AsrsJob asrsJob = new AsrsJob();
                        asrsJob.setToStation(job.getToStation());
                        asrsJob.setStatus("1");
                        asrsJob.setPriority(0);
                        asrsJob.setStatusDetail("0");
                        asrsJob.setFromStation(mCar.getBlockNo());
                        asrsJob.setBarcode(job.getContainer());
                        asrsJob.setType(AsrsJobType.RETRIEVAL);
                        asrsJob.setMcKey(job.getMcKey());
                        asrsJob.setFromLocation(location.getLocationNo());
                        HibernateUtil.getCurrentSession().save(asrsJob);

                        location.setReserved(true);
                    }
                }

            }
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
        }

    }
}
