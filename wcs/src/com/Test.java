package com;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.msgProc.Msg50Proc;
import com.asrs.business.msgProc.MsgProcess;
import com.asrs.communication.MessageCenter;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.*;

import com.asrs.message.MessageBuilder;
import com.asrs.xml.util.XMLUtil;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.Session;

import java.rmi.Naming;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        try{
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("from AsrsJob a where a.toStation=:toStation " +
                    "and a.type=:type and a.status!=:status");
            query.setParameter("toStation", "MC01");
            query.setParameter("type", AsrsJobType.PUTAWAY);
            query.setParameter("status", AsrsJobStatus.DONE);
            query.setMaxResults(1);
            AsrsJob asrsJob1 =(AsrsJob) query.uniqueResult();
            if(asrsJob1!=null){
                System.out.println(1);
            }
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
        }


    }
}
