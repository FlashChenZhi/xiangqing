package com.util.hibernate;

import org.hibernate.*;
import org.hibernate.cfg.*;

/**
 * Author: Zhouyue
 * Date: 2008-8-26
 * Time: 14:09:05
 * Copyright Daifuku Shanghai Ltd.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            //sessionFactory = new Configuration().configure().buildSessionFactory();
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getCurrentSession() {
        SessionFactory sessionFactory = getSessionFactory();
        return sessionFactory.getCurrentSession();
    }

    public static Session openSession() {
        return getSessionFactory().openSession();
    }

    public static void renewSessionFactory() {
        getSessionFactory().close();
        sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
    }

    public static int nextSeq(String seqName) {
        Session session = HibernateUtil.getCurrentSession();
//                Query q = session.createSQLQuery("select _nextval('"+seqName+"')");
//                Query q = session.createSQLQuery("select " + seqName + ".nextval as seq from dual");
        Query q = session.createSQLQuery("select next value for " + seqName);
        String a = String.valueOf(q.uniqueResult());
        return Integer.parseInt(a);
    }

}
