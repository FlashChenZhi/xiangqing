package com.asrs.communication;

import com.util.common.LogWriter;
import com.util.common.LoggerType;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-19
 * Time: 13:47:18
 * Copyright Dsl.Worgsoft.
 */
public class XmlCenter extends UnicastRemoteObject implements XmlProxy {
    BlockingQueue<String> _rcvdXmlQ = new LinkedBlockingQueue<String>();
    BlockingQueue<String> _sndXmlQ = new LinkedBlockingQueue<String>();

    public static XmlCenter instance() {
        return instance;
    }

    private static XmlCenter instance;

    static {
        try {
            instance = new XmlCenter();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private XmlCenter() throws RemoteException {
        super();
    }

    @Override
    public void addRcvdXML(String envelope) {
        _rcvdXmlQ.add(envelope);
    }


    @Override
    public String getRcvdXML() throws InterruptedException {
        String xml = _rcvdXmlQ.take();

        return xml;
    }

    @Override
    public void addSendXML(String s) throws RemoteException {
        _sndXmlQ.add(s);
    }


    @Override
    public String getSendXML() throws InterruptedException {
        return _sndXmlQ.take();
    }

    private void log(String type, String plcName, String id) {
        String log = String.format("[%1$s] [%2$s] [%3$s]", type, plcName, id);
        LogWriter.info(LoggerType.MessageLog, log);
    }


}
