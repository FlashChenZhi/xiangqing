package com;

import com.asrs.domain.LedMessage;
import com.asrs.domain.ScarChargeLocation;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        Query query = session.createQuery("from ScarChargeLocation s where scarBlockNo=:scarBlockNo and status=true ");
        query.setParameter("scarBlockNo", "SC01");
        List<ScarChargeLocation> scarChargeLocationList = query.list();
        for(ScarChargeLocation s :scarChargeLocationList){
            System.out.println(s.getChargeLocation().getLocationNo());
        }
        Transaction.commit();
    }
}
