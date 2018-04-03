package com.asrs.xml.util;

import com.asrs.RefKey;
import com.asrs.communication.XmlProxy;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.domain.XMLbean.XMLList.DataArea.StUnit;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.MovementReport;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.util.common.Const;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.rmi.Naming;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Nan
 * Date: 13-6-5
 * Time: 上午9:27
 * 解析XML的工具类，使用XStream工具
 */
public class XMLUtil {
    private static XStream xStream = new XStream(new DomDriver());

    static {
        xStream.processAnnotations(Envelope.class);
    }

    public static String toXML(Envelope envelope) {
        return xStream.toXML(envelope);
    }

    public static Envelope readXML(InputStream inputStream) {
        Envelope result = null;
        try {
            result = (Envelope) xStream.fromXML(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 使用XStream读取指定位置的XML文件，为便于本项目使用，默认添加本项目XML的命名空间
     *
     * @param targetBean       待写入的目标bean
     * @param absoluteFilePath 待写入文件的绝对路径
     */
    public static void writeXMLFile(Object targetBean, String absoluteFilePath) {
        String s = xStream.toXML(targetBean);
        StringBuilder result = new StringBuilder("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?> \n");
        result.append(s);
        result.insert(65, " xmlns:xcl=\"I bought a watch in the last year\"\n" +
                "xmlns:xsi=\"I bought a watch in the last year\"\n");


        File file = new File(absoluteFilePath);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(result.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MovementReport getNewMovementReport(String requestID, StUnit stUnit, ToLocation toLocation, FromLocation fromLocation) {
        MovementReport movementReport = new MovementReport();
        ControlArea controlArea = new ControlArea();  //controlArea
        Sender sender = new Sender();                 //sender

        controlArea.setSender(sender);                 //end sender
        controlArea.setCreationDateTime(new Date().toString());
        RefId refId = new RefId();  //refid
        refId.setReferenceId(RefKey.getNext());  //end refid
        controlArea.setRefId(refId);
        movementReport.setControlArea(controlArea); //end controlarea

        MovementReportDA movementReportDA = new MovementReportDA(); //dataarea
        movementReportDA.setRequestId(requestID); //requestId
        movementReportDA.setFromLocation(fromLocation); //end fromlocation


        movementReportDA.setStUnit(stUnit);  //end stUnit

        movementReportDA.setToLocation(toLocation);   //end tolocation
        movementReportDA.setReasonCode("0");
        movementReport.setDataArea(movementReportDA);   //end movementReport


        return movementReport;
    }

    public static MovementReport getNewMovementReport(String requestID, FromLocation fromLocation, StUnit stUnit, ToLocation toLocation) {
        MovementReport movementReport = new MovementReport();
        ControlArea controlArea = new ControlArea();
        MovementReportDA movementReportDA = new MovementReportDA();
        movementReport.setControlArea(controlArea);
        movementReport.setDataArea(movementReportDA);
        Sender sender = new Sender();
        RefId refId = new RefId();
        refId.setReferenceId(RefKey.getNext());
        controlArea.setSender(sender);
        controlArea.setRefId(refId);

        movementReportDA.setFromLocation(fromLocation);
        movementReportDA.setStUnit(stUnit);
        movementReportDA.setToLocation(toLocation);
        controlArea.setCreationDateTime(new Date().toString());
        controlArea.setRefId(refId);

        movementReportDA.setRequestId(requestID);
        movementReportDA.setReasonCode("00");

        return movementReport;
    }

    /**
     * 生成新的ControlArea
     */
    public static ControlArea getNewControlArea() {
        ControlArea controlArea = new ControlArea();
        Sender sender = new Sender();
        RefId refId = new RefId();
        refId.setReferenceId(RefKey.getNext());
        controlArea.setSender(sender);
        controlArea.setCreationDateTime(new Date().toString());
        controlArea.setRefId(refId);

        return controlArea;
    }

    public static ControlArea getNewControlAreaForTest(String refid) {
        ControlArea controlArea = new ControlArea();
        Sender sender = new Sender();
        RefId refId = new RefId();
        refId.setReferenceId(refid);
        controlArea.setSender(sender);
        controlArea.setRefId(refId);
        controlArea.setCreationDateTime(new Date().toString());


        return controlArea;
    }

    public static Envelope getEnvelope(String xml) {

        Object o = null;
        Envelope envelope = null;


        o = xStream.fromXML(xml.trim());
        envelope = (Envelope) o;
        return envelope;
    }

    public static String getSendXML(Envelope envelope) {
        String s = xStream.toXML(envelope);
        StringBuilder result = new StringBuilder();
        result.append(s);

        String schema;
        if (s.contains("LoadUnitAtId")) {
            schema = "LoadUnitAtId";
        } else if (s.contains("MovementReport")) {
            schema = "MovementReport";
        } else {
            schema = "HandlingUnitStatus";
        }

//        String str=" xmlns=\"http://www.consafelogistics.com\" xmlns:xcl=\"http://www.consafelogistics.com/wmswcs\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"xcl "
//            + schema + "_0100.xsd\" version=\"0100\"";
//
//        result.insert(65,str);
        String xml = result.toString();
        xml.replaceAll("__", "_");

        return xml;
    }

/*    public static void saveEnvelopeAsFile(Envelope envelope) {
        String result = xStream.toXML(envelope);
        saveAsFile(result);
    }

    public static void saveAsFile(String s){
        String elementName;
        if (s.contains("LoadUnitAtID")) {
            elementName = "LoadUnitAtID";
        } else if (s.contains("AcceptLoadUnitAtID")) {
            elementName = "AcceptLoadUnitAtID";
        } else if (s.contains("CancelTransportOrder")) {
            elementName = "CancelTransportOrder";
        } else if (s.contains("HandlingUnitStatus")) {
            elementName = "HandlingUnitStatus";
        } else if (s.contains("MovementReport")) {
            elementName = "MovementReport";
        } else if (s.contains("TransportOrderLog")) {
            elementName = "TransportOrderLog";
        } else {
            elementName = "Envelope";
        }
        File file = new File("c:/logs/xml/"+ elementName + System.currentTimeMillis() + ".xml");

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(s);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static String convertString(String s) {
        if (StringUtils.isBlank(s)) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            byte[] bytes = s.getBytes();
            for (byte b : bytes) {
                int i = b;
                String a = "\\u" + StringUtils.leftPad("" + i, 4, "0");
                sb.append(a);
            }
            return sb.toString();
        }
    }

    public static Date convertDate(String dataString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        try {
            date = format.parse(dataString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static void sendEnvelope(Envelope envelope) throws Exception {

//        Session session = HibernateUtil.getCurrentSession();
//        boolean connected = session.getTransaction().isActive();
//        if(!connected) {
//            Transaction.begin();
//        }
//
//        String sendId = StringUtils.leftPad(String.valueOf(HibernateUtil.nextSeq("seq_xml")), 5, "0");
//        if(!connected) {
//            Transaction.commit();
//        }

//        String sendId = "10000";
        XmlProxy _wmsproxy = (XmlProxy) Naming.lookup(Const.WMSPROXY);

//        String result = sendId + XMLUtil.getSendXML(envelope);

        _wmsproxy.addSendXML(XMLUtil.getSendXML(envelope));
    }
}
