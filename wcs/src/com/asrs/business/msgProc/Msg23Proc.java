package com.asrs.business.msgProc;

import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.Plc;
import com.asrs.message.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg23Proc implements MsgProcess
{
      public void Do(MessageBuilder msg) throws MsgException
      {
            Message23 message23 = new Message23(msg.DataString);
            message23.setPlcName(msg.PlcName);
            Do(message23);
      }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;


      public void Do(Message23 message23)
      {
          System.out.println(message23.getID() + message23.getPlcName());
          try {
              String mcKey = message23.McKey;
              Transaction.begin();

              HibernateUtil.getCurrentSession().createQuery("update WcsMessage set received=true where machineNo=:mNo and received=false ")
                      .setParameter("mNo", message23.MachineNo).executeUpdate();

              Transaction.commit();

          } catch (Exception e) {
              Transaction.rollback();

          }

          try {
              Transaction.begin();
              Plc plc = Plc.getPlcByPlcName(message23.getPlcName());
              plc.setStatus("1");
              Transaction.commit();
          } catch (Exception e) {
              Transaction.rollback();
          }

      }
}
