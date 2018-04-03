package com.util.hibernate;

import org.hibernate.*;

/**
 * Author: Zhouyue
 * Date: 2008-9-3
 * Time: 17:58:53
 * Copyright Daifuku Shanghai Ltd.
 */
public class Transaction
{
      public static void begin()
      {            
            Session session = HibernateUtil.getCurrentSession();
            session.beginTransaction();
      }

      public static void commit()
      {
            HibernateUtil.getCurrentSession().getTransaction().commit();
      }

      public static void rollback()
      {
            try
            {
                  HibernateUtil.getCurrentSession().getTransaction().rollback();
            }
            catch (RuntimeException rbEx)
            {
            }
      }

}
