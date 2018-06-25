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
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        Transaction.begin();
        String s = UUID.randomUUID().toString().substring(0, 15);
        System.out.println(s);
        Transaction.commit();
    }
}
