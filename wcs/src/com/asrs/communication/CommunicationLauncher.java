package com.asrs.communication;

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
public class CommunicationLauncher {
    public static void main(String[] args) {
        try {
            final Registry registry = LocateRegistry.getRegistry();
            registry.rebind("WcsMessageProxy", MessageCenter.instance());

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        registry.unbind("WcsMessageProxy");
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

        List<Plc> plcs = null;
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            plcs = session.createCriteria(Plc.class).list();
            Transaction.commit();
        } catch (RuntimeException ex) {
            try {
                Transaction.rollback();
            } catch (RuntimeException rbEx) {
            }
            System.out.println("Database Error, Can't startup the Application.");
            LogWriter.writeError(CommunicationLauncher.class, ex.getMessage());
            System.exit(-1);
        }

        ConnectionManager connectionManager = new ConnectionManager();

        for (Plc plc : plcs) {
            connectionManager.Connect(plc.getPlcName(), plc.getIpAddress(), plc.getPort());
        }

        Thread thread1 = new Thread(connectionManager);
        thread1.setName("ConnectionManager");
        thread1.start();
    }
}

