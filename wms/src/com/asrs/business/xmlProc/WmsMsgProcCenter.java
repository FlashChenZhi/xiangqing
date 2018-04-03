package com.asrs.business.xmlProc;

import com.asrs.communication.XmlProxy;
import com.asrs.xml.util.XMLUtil;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.*;
import com.asrs.domain.XMLbean.XMLProcess;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/27.
 */
public class WmsMsgProcCenter implements Runnable {
    HashMap<String, XMLProcess> _procFactory = new HashMap<String, XMLProcess>();

    Logger logger = Logger.getLogger(this.getClass());
    XmlProxy _wmsProxy;

    public WmsMsgProcCenter(XmlProxy wmsProxy) {
        this._wmsProxy = wmsProxy;
    }

    private void DoXml(String xml) throws RemoteException {
        try {
            //收到XML
            System.out.println("Receive XML:" + xml);
            String sendId = xml.substring(0, 5);
            if(sendId.equals(xml)){
                //应答
                System.out.println("SendId:" + sendId + " response OK!");
            }else {
                _wmsProxy.addSendXML(sendId);

               // String msg = xml.substring(5);
                //XMLUtil.saveAsFile(msg);
                Envelope e = XMLUtil.getEnvelope(xml);

                XMLProcess xmlProcess = getOrder(e);

                if (xmlProcess != null) {
                    xmlProcess.execute();
                } else {
                    //XMl解读出错
                    String message = "XML Unmatched：" + xml;
                    LogWriter.error(LoggerType.XMLMessageInfo, message);
                    System.out.println(message);
                }
            }
        } catch (Exception e) {
            LogWriter.error(LoggerType.XMLMessageInfo, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String xml = _wmsProxy.getRcvdXML();
                DoXml(xml);
            } catch (RemoteException e) {
                LogWriter.error(LoggerType.XMLMessageInfo, "RMI发生错误,MsgProcCenter终止");
                return;
            } catch (InterruptedException e) {
                LogWriter.error(LoggerType.XMLMessageInfo, "MsgProcCenter Interrupted");
                return;
            } catch (Exception e) {
                LogWriter.error(LoggerType.XMLMessageInfo, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static XMLProcess getOrder(Envelope envelope) {
        XMLProcess xmlProcess = null;

        AcceptLoadUnitAtID acceptLoadUnitAtID = envelope.getAcceptLoadUnitID();
        CancelTransportOrder cancelTransportOrder = envelope.getCancelTransportOrder();
        HandlingUnitStatus handlingUnitStatus = envelope.getHandlingUnitStatus();
        LoadUnitAtID loadUnitAtID = envelope.getLoadUnitAtID();
        MovementReport movementReport = envelope.getMovementReport();
        TransportOrder transportOrder = envelope.getTransportOrder();
        WorkStartEnd workStartEnd = envelope.getWorkStartEnd();
        TransportModeChange transportModeChange = envelope.getTransportModeChange();

        if (null != acceptLoadUnitAtID) {
            xmlProcess = acceptLoadUnitAtID;
        } else if (null != cancelTransportOrder) {
            xmlProcess = cancelTransportOrder;
        } else if (null != handlingUnitStatus) {
            xmlProcess = handlingUnitStatus;
        } else if (null != loadUnitAtID) {
            xmlProcess = loadUnitAtID;
        } else if (null != movementReport) {
            xmlProcess = movementReport;
        } else if (null != transportOrder) {
            xmlProcess = transportOrder;
        } else if (null != workStartEnd) {
            xmlProcess = workStartEnd;
        } else if (null != transportModeChange) {
            xmlProcess = transportModeChange;
        } else {
            //未找到任何XMLOder
        }

        return xmlProcess;
    }
}
