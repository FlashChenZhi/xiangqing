package com.asrs.business.msgProc;

import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.Plc;
import com.asrs.domain.WcsMessage;
import com.asrs.message.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;

import java.util.List;

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
              Thread.sleep(100);
              for ( int i=0; i<10 ; i++){
                  Transaction.begin();
                  Query q = HibernateUtil.getCurrentSession().createQuery("from WcsMessage where machineNo=:mNo and mckey = :mckey and received=false ")
                          .setString("mNo", message23.MachineNo)
                          .setString("mckey",mcKey);
                  List<WcsMessage> wcsMessages = q.list();
                  if(wcsMessages.size()>0){
                      for(WcsMessage wcsMessage : wcsMessages){
                          wcsMessage.setReceived(true);
                      }
                      Transaction.commit();
                      break;
                  }else {
                      Transaction.commit();
                      //Thread.sleep(100);
                  }
              }

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
