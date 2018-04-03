package com.asrs.communication;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.asrs.domain.Plc;
import com.util.common.LogWriter;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * Author: Zhouyue Created by IntelliJ IDEA.
 * Date: 2008-2-19
 * Time: 12:31:30
 * Copyright Dsl.Worgsoft.
 */
public class XmlCommunicationLauncher {


    public static void main(String[] args) {
        try {
            final Registry registry = LocateRegistry.getRegistry();

            registry.rebind("WmsXmlProxy", XmlCenter.instance());

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        registry.unbind("WmsXmlProxy");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        }
//
//        List<Wms> wmss = new ArrayList<Wms>();
//        try {
//            Transaction.begin();
//            Session session = HibernateUtil.getCurrentSession();
//            wmss = session.createCriteria(Wms.class).list();
//            Transaction.commit();
//        } catch (RuntimeException ex) {
//            try {
//                Transaction.rollback();
//            } catch (RuntimeException rbEx) {
//            }
//            System.out.println("Database Error, Can't startup the Application.");
//            LogWriter.writeError(XmlCommunicationLauncher.class, ex.getMessage());
//            System.exit(-1);
//        }

        Thread thread1 = new Thread(new XmlServer());
        thread1.setName("XmlServer");
        thread1.start();
    }
}
