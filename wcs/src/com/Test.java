package com;

import com.asrs.domain.LedMessage;
import com.wms.domain.Location;
import com.asrs.domain.ScarChargeLocation;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        Query query2 = session.createQuery("from Block ");
            List<Block> list3 = query2.list();
            System.out.println(list3.get(0).getBlockNo());
        Transaction.commit();
    }
}
