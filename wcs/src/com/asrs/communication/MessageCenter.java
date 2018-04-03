package com.asrs.communication;

import com.asrs.message.*;
import com.util.common.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-19
 * Time: 13:47:18
 * Copyright Dsl.Worgsoft.
 */
public class MessageCenter extends UnicastRemoteObject implements MessageProxy {
    BlockingQueue<MessageBuilder> _rcvdMsgQ = new LinkedBlockingQueue<MessageBuilder>();
    //      BlockingQueue<Message> _defaultSndMsgQ = new LinkedBlockingQueue<Message>();
    HashMap<String, BlockingQueue<Message>> _sndQmap = new HashMap<String, BlockingQueue<Message>>();

    public static MessageCenter instance() {
        return instance;
    }

    private static MessageCenter instance;

    static {
        try {
            instance = new MessageCenter();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private MessageCenter() throws RemoteException {
        super();
    }

    public void addRcvdMsg(MessageBuilder msg) {
        _rcvdMsgQ.add(msg);
        if(!msg.ID.equals("26") && !msg.ID.equals("30"))
        log("PUT", msg.PlcName, msg.ID);
    }

    public void addSndMsg(Message msg) {
        String plcName = msg.getPlcName();
        if (!_sndQmap.containsKey(plcName))

        {
            _sndQmap.put(plcName, new LinkedBlockingQueue<Message>());
        }

        _sndQmap.get(plcName).

                add(msg);
        if(!msg.getID().equals("06") && !msg.getID().equals("10"))
        log("PUT", plcName, msg.getID()

        );
    }

    public MessageBuilder getRcvdMsg() throws InterruptedException {
        MessageBuilder mb = _rcvdMsgQ.take();
        if(!mb.ID.equals("26") && !mb.ID.equals("30"))
        log("GET", mb.PlcName, mb.ID);
        return mb;
    }

    public Message getSndMsg(String plcName) throws InterruptedException {
        if (!_sndQmap.containsKey(plcName)) {
            _sndQmap.put(plcName, new LinkedBlockingQueue<Message>());
        }
        Message msg = _sndQmap.get(plcName).take();
        if(!msg.getID().equals("06") && !msg.getID().equals("10"))
        log("GET", plcName, msg.getID());
        return msg;
    }

    private void log(String type, String plcName, String id) {
        String log = String.format("[%1$s] [%2$s] [%3$s]", type, plcName, id);
        LogWriter.writeInfo("MessageLog", log);
    }


}
