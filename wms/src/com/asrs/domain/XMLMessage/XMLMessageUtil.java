package com.asrs.domain.XMLMessage;

import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.*;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.HandlingUnitStatusDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.LoadUnitAtIdDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.asrs.domain.XMLbean.XMLList.HandlingUnitStatus;
import com.asrs.domain.XMLbean.XMLList.LoadUnitAtID;
import com.asrs.domain.XMLbean.XMLList.MovementReport;
import com.asrs.domain.XMLbean.XMLList.UserArea.UserArea;
import com.asrs.domain.consts.xmlmessage.XMLMessageStatus;
import com.asrs.domain.consts.xmlmessage.XMLMessageType;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

//import com.util.hibernate.Transaction;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-25
 * Time: 下午2:24
 */
public class XMLMessageUtil {

    /**
     * 新增
     *
     * @param xmlMessage
     */

    public static void add(XMLMessage xmlMessage) {
//        try {
//            Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        session.save(xmlMessage);
//            Transaction.commit();
//        } catch (RuntimeException ex) {
//            Transaction.rollback();
//            LogWriter.error(XMLMessageUtil.class, ex.getMessage());
//            throw ex;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 修改
     *
     * @param xmlMessage
     */
    public static void modify(XMLMessage xmlMessage) {
//        try {
//            Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        session.update(xmlMessage);
//            Transaction.commit();
//        } catch (RuntimeException ex) {
//            Transaction.rollback();
//            LogWriter.error(XMLMessageUtil.class, ex.getMessage());
//            throw ex;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 根据状态查找
     *
     * @param status
     * @return
     */
    public static List<XMLMessage> getByStatus(int status) {
        List<XMLMessage> list = null;
//        try {
//            Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        Query query = session.createQuery("from XMLMessage where status=:status");
        query.setParameter("status", status);
        list = query.list();
//            Transaction.commit();
//        } catch (RuntimeException ex) {
//            Transaction.rollback();
//            LogWriter.error(XMLMessageUtil.class, ex.getMessage());
//            throw ex;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return list;
    }

    /**
     * 根据barcode将状态更新为已发送
     *
     * @param barcode
     * @return
     */
    public static void modifyToSent(String barcode) {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("from XMLMessage where stunitId=:barcode");
            query.setParameter("barcode", barcode);
            XMLMessage m = (XMLMessage) query.list().get(0);
            m.setStatus(XMLMessageStatus.SENT);
            if (m.getType() == XMLMessageType.LOAD_UNIT_AT_ID) {
                session.update(m);
            } else {
                LogWriter.info(LoggerType.XMLMessageInfo, "Type:" + m.getType() + " Barcode:" + barcode + " is sent and deleted.");
                session.delete(m);
            }
            Transaction.commit();
        } catch (RuntimeException ex) {
            Transaction.rollback();
            LogWriter.error(LoggerType.XMLMessageInfo, ex.getMessage());
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据barcode将状态更新为已应答
     *
     * @param barcode
     * @return
     */
    public static void modifyToAnswered(String barcode) {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("from XMLMessage where stunitId=:barcode");
            query.setParameter("barcode", barcode);
            XMLMessage m = (XMLMessage) query.list().get(0);
            m.setStatus(XMLMessageStatus.ANSWERED);
            LogWriter.info(LoggerType.XMLMessageInfo, "Type:" + m.getType() + " Barcode:" + barcode + " is answered and deleted.");
            session.delete(m);
            Transaction.commit();
        } catch (RuntimeException ex) {
            Transaction.rollback();
            LogWriter.error(LoggerType.XMLMessageInfo, ex.getMessage());
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除
     *
     * @param messageSendStatus
     */
    public static void delete(XMLMessage messageSendStatus) {
        try {
//            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            session.delete(messageSendStatus);
//            Transaction.commit();
        } catch (RuntimeException ex) {
//            Transaction.rollback();
            LogWriter.error(LoggerType.XMLMessageInfo, ex.getMessage());
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Envelope getEnvolope(XMLMessage xmlMessage) {
        Envelope envelope = new Envelope();
        if (xmlMessage.getType() == XMLMessageType.LOAD_UNIT_AT_ID) {
            envelope.setLoadUnitAtID(getLoadUnitAtID(xmlMessage));
        } else if (xmlMessage.getType() == XMLMessageType.MOVEMENT_REPORT) {
            envelope.setMovementReport(getMovementReport(xmlMessage));
        } else {
            envelope.setHandlingUnitStatus(getHandlingUnitStatus(xmlMessage));
        }
        return envelope;
    }

    private static LoadUnitAtID getLoadUnitAtID(XMLMessage xmlMessage) {
        LoadUnitAtID loadUnitAtID = new LoadUnitAtID();
        ControlArea controlArea = new ControlArea();
        Sender sender = new Sender();
        controlArea.setSender(sender);
        //  controlArea.setCreationDateTime(xmlMessage.getCreationDateTime());
        RefId refId = new RefId();
        refId.setReferenceId(xmlMessage.getRefid());
        controlArea.setRefId(refId);
        loadUnitAtID.setControlArea(controlArea);
        LoadUnitAtIdDA loadUnitAtIdDA = new LoadUnitAtIdDA();
        loadUnitAtIdDA.setWcsLoadId(xmlMessage.getWcsloadId());
        XMLLocation xmlLocation = new XMLLocation();
        xmlLocation.setMHA(xmlMessage.getMha());
        loadUnitAtIdDA.setXMLLocation(xmlLocation);
        ScanData scanData = new ScanData();
//        scanData.setScanFlag(xmlMessage.getScanFlag());
        ItemData itemData = new ItemData();
        itemData.setValue(xmlMessage.getItemDateValue());

        scanData.setItemData(itemData);
//        loadUnitAtIdDA.setScanData(scanData);
        loadUnitAtIdDA.setWeight(xmlMessage.getWeight());
        if (StringUtils.isNotBlank(xmlMessage.getInformation()) && xmlMessage.getInformation().length() > 1) {
            loadUnitAtIdDA.setStUnitType(xmlMessage.getInformation().substring(1,2));
            loadUnitAtIdDA.setInformation(xmlMessage.getInformation().substring(0,2));
        }
        loadUnitAtID.setDataArea(loadUnitAtIdDA);
        return loadUnitAtID;
    }

    private static MovementReport getMovementReport(XMLMessage xmlMessage) {
        MovementReport movementReport = new MovementReport();
        ControlArea controlArea = new ControlArea();  //controlArea
        Sender sender = new Sender();                 //sender
        controlArea.setSender(sender);                 //end sender
        //  controlArea.setCreationDateTime(new Date().toString());
        RefId refId = new RefId();  //refid
        refId.setReferenceId(xmlMessage.getRefid());  //end refid
        controlArea.setRefId(refId);
        movementReport.setControlArea(controlArea); //end controlarea

        MovementReportDA movementReportDA = new MovementReportDA(); //dataarea
        movementReportDA.setRequestId(xmlMessage.getRequestId()); //requestId
        FromLocation fromLocation = new FromLocation();
        fromLocation.setMHA(xmlMessage.getFromMha());
//        fromLocation.setRack(xmlMessage.getFromRack());
//        fromLocation.setX(xmlMessage.getFromX());
//        fromLocation.setY(xmlMessage.getFromY());
//        fromLocation.setZ(xmlMessage.getFromZ());
        movementReportDA.setFromLocation(fromLocation); //end fromlocation
        StUnit stUnit = new StUnit();
        stUnit.setStUnitID(xmlMessage.getStunitId());
        // stUnit.setStUnitType(xmlMessage.gets);
        stUnit.setBlockCode(xmlMessage.getReasonCode());
        //stUnit.setRegDate(xmlMessage.getr);

        movementReportDA.setStUnit(stUnit);  //end stUnit

        ToLocation toLocation = new ToLocation();
        toLocation.setMHA(xmlMessage.getToMha());
//        toLocation.setRack(xmlMessage.getToRack());
//        toLocation.setX(xmlMessage.getToX());
//        toLocation.setY(xmlMessage.getToY());
//        toLocation.setZ(xmlMessage.getToZ());

        movementReportDA.setToLocation(toLocation);   //end tolocation
        movementReportDA.setReasonCode(xmlMessage.getReasonCode());

        UserArea ua = new UserArea();
        ua.setOrderID(xmlMessage.getOrderId());
        movementReportDA.setUserArea(ua);

        movementReport.setDataArea(movementReportDA);   //end movementReport

        return movementReport;
    }

    private static HandlingUnitStatus getHandlingUnitStatus(XMLMessage xmlMessage) {
        HandlingUnitStatus handlingUnitStatus = new HandlingUnitStatus();

        ControlArea controlArea = new ControlArea();
        handlingUnitStatus.setControlArea(controlArea);
        Sender sender = new Sender();
        controlArea.setSender(sender);
        //  controlArea.setCreationDateTime(xmlMessage.getCreationDateTime());
        RefId refId = new RefId();
        controlArea.setRefId(refId);
        refId.setReferenceId(xmlMessage.getRefid());
        HandlingUnitStatusDA handlingUnitStatusDA = new HandlingUnitStatusDA();
        handlingUnitStatus.setDataArea(handlingUnitStatusDA);
        HandlingUnitInfo handlingUnitInfo = new HandlingUnitInfo();
        handlingUnitStatusDA.setHandlingUnitInfo(handlingUnitInfo);
        handlingUnitInfo.setHandlingUnitType(xmlMessage.getHandlingUnitType());
        handlingUnitInfo.setHandlingUnitNbr(xmlMessage.getHandlingUnitNbr());
        handlingUnitInfo.setReasonCode(xmlMessage.getReasonCode());

        XMLLocation xmlLocation = new XMLLocation();
        handlingUnitInfo.setXMLLocation(xmlLocation);
        xmlLocation.setMHA(xmlMessage.getMha());
//        xmlLocation.setRack(xmlMessage.getRack());

        return handlingUnitStatus;
    }
}
