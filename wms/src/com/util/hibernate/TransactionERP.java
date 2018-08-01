package com.util.hibernate;

import org.hibernate.Session;

/**
 * Author: Zhouyue
 * Date: 2008-9-3
 * Time: 17:58:53
 * Copyright Daifuku Shanghai Ltd.
 */
public class TransactionERP
{
      public static void begin()
      {            
            Session session = HibernateERPUtil.getCurrentSession();
            session.beginTransaction();
      }

      public static void commit()
      {
            HibernateERPUtil.getCurrentSession().getTransaction().commit();
      }

      public static void rollback()
      {
            try
            {
                  HibernateERPUtil.getCurrentSession().getTransaction().rollback();
            }
            catch (RuntimeException rbEx)
            {
            }
      }

}
