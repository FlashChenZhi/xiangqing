package com.asrs.business.msgProc;

import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.Plc;
import com.asrs.message.Message30;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.util.hibernate.Transaction;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg30Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message30 message30 = new Message30(msg.DataString);
        message30.setPlcName(msg.PlcName);
        Do(message30);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;


    public void Do(Message30 message30) {
//        Message10 message10 = new Message10();
//        message10.ConsoleNo = message30.ConsoleNo;
//        message10.WcsNo = message30.WcsNo;
//        message10.HeartBeatCycle = message30.HeartBeatCycle;
//        try {
//            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
//            _wcsproxy.addSndMsg(message10);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try{
            Transaction.begin();

            Plc plc = Plc.getPlcByPlcName(message30.getPlcName());
            plc.setLastHeartBeatTime(new Date());

            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
        }
    }
}
