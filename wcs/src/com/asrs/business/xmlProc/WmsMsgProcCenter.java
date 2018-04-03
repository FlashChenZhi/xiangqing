package com.asrs.business.xmlProc;

import com.asrs.communication.XmlProxy;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.*;
import com.domain.XMLbean.XMLProcess;
import com.util.common.LogWriter;
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
            if (sendId.equals(xml)) {
                //应答
                System.out.println("SendId:" + sendId + " response OK!");
            } else {
                _wmsProxy.addSendXML(sendId);

//                String msg = xml.substring(5);
                Envelope e = XMLUtil.getEnvelope(xml);

                XMLProcess xmlProcess = getOrder(e);

                if (xmlProcess != null) {
                    xmlProcess.execute();
                } else {
                    //XMl解读出错
                    String message = "XML Unmatched：" + xml;
                    LogWriter.writeError(this.getClass(), message);
                    System.out.println(message);
                }
            }
        } catch (Exception e) {
            LogWriter.writeError(this.getClass(), e.getMessage());
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

    public static XMLProcess getOrder(Envelope envelope) {
        XMLProcess xmlProcess = null;

        TransportOrder transportOrder = envelope.getTransportOrder();

        if (null != transportOrder) {
            xmlProcess = transportOrder;
        } else {
            //未找到任何XMLOder
        }

        return xmlProcess;
    }
}
