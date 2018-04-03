package com.asrs;


import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.Wcs;
import com.asrs.domain.XMLCommand;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLMessage.XMLMessage;
import com.domain.XMLMessage.XMLMessageUtil;
import com.domain.XMLbean.Envelope;
import com.domain.consts.xmlbean.XMLConstant;
import com.domain.consts.xmlmessage.XMLMessageStatus;
import com.util.common.LogWriter;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-20
 * Time: 9:01:54
 * Copyright Dsl.Worgsoft.
 */
public class XMLMonitor implements Runnable {
    public static final int INTERVAL = 200;

    private long lastSendTime;
    private static long PING_INTERVAL = 5 * 1000;

    private boolean isDoSomething = false;

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;

    public XMLMonitor(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    public void run() {
        List<XMLCommand> xmlCommands;
        List<XMLMessage> xmls;
        while (true) {
            try {
                Transaction.begin();
                xmlCommands = XMLCommand.getXmlCommands();
                xmls = XMLMessage.getMessageForSend();
                Transaction.commit();
            } catch (RuntimeException e) {
                Transaction.rollback();
                String errMsg = e.getMessage();
                LogWriter.writeError(this.getClass(), errMsg);
                e.printStackTrace();
                try {
                    Thread.sleep(INTERVAL);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                continue;
            }

            try {
                isDoSomething = false;

                for (XMLCommand xc : xmlCommands) {
                    doCommand(xc);
                }

                for (XMLMessage m : xmls) {
                    addToProxy(m);
                }

                sendPing();
            } catch (RemoteException e) {
                LogWriter.writeError(this.getClass(), "RMI发生错误,AsrsJobMonitor终止");
                return;
            } catch (InterruptedException e) {
                String errMsg = "AsrsJobMonitor Interrupted";
                LogWriter.writeError(this.getClass(), errMsg);
                break;
            }
        }
    }

    private void doCommand(XMLCommand xc) throws RemoteException {
        try {
            Transaction.begin();
            String wmsName = xc.getParam1();

            if (xc.getCommand().equals(XMLCommand.ONLINE)) {
//                if (Wms.NAME_SENDER.equals(wmsName)) {
                _wmsProxy.addSendXML(XMLConstant.PROTOCOL_SOH);
                lastSendTime = System.currentTimeMillis();
//                }
            } else if (xc.getCommand().equals(XMLCommand.OFFLINE)) {
//                if (Wms.NAME_SENDER.equals(wmsName)) {
                _wmsProxy.addSendXML(XMLConstant.PROTOCOL_EOT);
                lastSendTime = System.currentTimeMillis();
//                }
            }

            isDoSomething = true;


            HibernateUtil.getCurrentSession().delete(xc);
            Transaction.commit();
        } catch (RuntimeException ex) {
            Transaction.rollback();
            LogWriter.writeError(this.getClass(), ex.getMessage());
        }
    }

    private void addToProxy(XMLMessage m) throws RemoteException {
        if (Wcs.getWcs().getStatus().equals(Wcs.OFFLINE)) return;

        try {
            Transaction.begin();

            Envelope envelope = XMLMessageUtil.getEnvolope(m);

            String sendId = "";
            if (m.getSendId() != null) {
                sendId = m.getSendId();
            } else {
                sendId = StringUtils.leftPad(String.valueOf(HibernateUtil.nextSeq("seq_xml")), 5, "0");
                m.setSendId(sendId);
            }
            m.setSendDate(new Date());
            HibernateUtil.getCurrentSession().update(m);
            Transaction.commit();

            String result = sendId + XMLUtil.getSendXML(envelope);

            _wmsProxy.addSendXML(result);
            System.out.println("Send XML:" + result);
            lastSendTime = System.currentTimeMillis();
            isDoSomething = true;
            //XMLUtil.saveEnvelopeAsFile(envelope);

            Transaction.begin();

            m.setStatus(XMLMessageStatus.SENT);
            HibernateUtil.getCurrentSession().merge(m);

            Transaction.commit();

        } catch (RuntimeException ex) {
            Transaction.rollback();
            LogWriter.writeError(this.getClass(), ex.getMessage());
        }
    }

    private void sendPing() throws RemoteException, InterruptedException {
        if (Wcs.getWcs().getStatus().equals(Wcs.OFFLINE)) return;

        if (isDoSomething) return;

        if (System.currentTimeMillis() - PING_INTERVAL > lastSendTime) {
            _wmsProxy.addSendXML(XMLConstant.PROTOCOL_PING);
            lastSendTime = System.currentTimeMillis();
        }

        Thread.sleep(INTERVAL);
    }
}
