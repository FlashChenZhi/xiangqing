package com.asrs.business.msgProc;

import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.util.common.LogWriter;

import java.rmi.RemoteException;
import java.util.HashMap;

public class WcsMsgProcCenterSubThread implements Runnable {
    HashMap<String, MsgProcess> _procFactory = new HashMap<String, MsgProcess>();
    MessageBuilder msg = new MessageBuilder();
    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;
    public WcsMsgProcCenterSubThread(HashMap<String, MsgProcess> procFactory,MessageBuilder msg,XmlProxy wmsProxy, MessageProxy wcsProxy){
        _procFactory = procFactory;
        this.msg = msg;
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    @Override
    public void run() {
        try {
            if (_procFactory.containsKey(msg.ID)) {
                MsgProcess proc = _procFactory.get(msg.ID);
                proc.Do(msg);
            } else {
                MsgProcess proc = GetNewIDProcess(msg.ID);
                _procFactory.put(msg.ID, proc);
                proc.Do(msg);
            }
        }catch (RemoteException e) {
            LogWriter.writeError(this.getClass(), "RMI发生错误");
            return;
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
}
