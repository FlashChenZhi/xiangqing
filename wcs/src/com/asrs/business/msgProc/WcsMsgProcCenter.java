package com.asrs.business.msgProc;

import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.util.common.LogWriter;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Author: Zhouyue
 * Date: 2008-9-25
 * Time: 11:43:39
 * Copyright Daifuku Shanghai Ltd.
 */
public class WcsMsgProcCenter implements Runnable {
    HashMap<String, MsgProcess> _procFactory = new HashMap<String, MsgProcess>();
    Logger logger = Logger.getLogger(this.getClass());
    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;

    public WcsMsgProcCenter(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }


    private void DoMsg(MessageBuilder msg) throws RemoteException {
        try {
            if (_procFactory.containsKey(msg.ID)) {
                MsgProcess proc = _procFactory.get(msg.ID);
                proc.Do(msg);
            } else {
                MsgProcess proc = GetNewIDProcess(msg.ID);
                _procFactory.put(msg.ID, proc);
                proc.Do(msg);
            }
        } catch (MsgException msgEx) {
            LogWriter.writeError(this.getClass(), msgEx.getMessage());
        }
    }

    public MsgProcess GetNewIDProcess(String id) {
        String classname = MsgProcess.class.getPackage().getName() + ".Msg" + id + "Proc";
        try {
            Class c = Class.forName(classname);
            Object obj = c.newInstance();
            if (obj instanceof MsgProcess) {
                MsgProcess proc = (MsgProcess) obj;
                proc.setProxy(_wmsProxy, _wcsProxy);
                return proc;
            } else {
                return new UnknownMsgProcess();
            }
        } catch (Exception e) {
            LogWriter.writeError(this.getClass(), e.getMessage());
        }
        return new UnknownMsgProcess();
    }

    public void run() {
        while (true) {
            try {
                MessageBuilder msg = _wcsProxy.getRcvdMsg();
                DoMsg(msg);
            } catch (RemoteException e) {
                LogWriter.writeError(this.getClass(), "RMI发生错误,MsgProcCenter终止");
                return;
            } catch (InterruptedException e) {
                LogWriter.writeError(this.getClass(), "MsgProcCenter Interrupted");
                return;
            } catch (Exception e) {
                LogWriter.writeError(this.getClass(), e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
