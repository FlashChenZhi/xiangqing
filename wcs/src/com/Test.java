package com;

import com.asrs.domain.LedMessage;
import com.wms.domain.Location;
import com.asrs.domain.ScarChargeLocation;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.blocks.WcsMessage;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        Transaction.begin();
        List<String> list = new ArrayList<>();
        list.add("1");
        Query  q = HibernateUtil.getCurrentSession().createQuery("from Location l where exists( select 1 from Job j where " +
                " and l.actualArea= j.toLocation.actualArea " +
                " and l.level = j.toLocation.level and l.bay = j.toLocation.bay and " +
                " j.skuCode=:skuCode and j.lotNum=:batchNo and l.position=j.toLocation.position )  " +
                " and l.empty=true and l.reserved=false and l.asrsFlag = true and " +
                " l.putawayRestricted = false and l.position in (:po)  order by l.position desc, l.seq asc ")
                .setParameter("batchNo", "test").setParameter("skuCode", "test");

        q.setParameterList("po", list);

        List<Location> wms = q.list();
        Transaction.commit();
    }
}
