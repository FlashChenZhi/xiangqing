package com.domain.XMLMessage;

import com.domain.consts.xmlmessage.XMLMessageStatus;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-25
 * Time: 下午1:40
 */
@Entity
@Table(name = "messageSendstatus")
public class XMLMessage {

    public static final String __REQUESTTYPE = "type";

    public static final String __ITEMDATEVALUE = "itemDateValue";

    public static final String __STUNITID = "stunitId";

    public static final String __STATUS = "status";

    public static final String __CREATEDATETIME = "creationDateTime";


    private int id;
    private String version;
    private String division;
    private String confirmation;
    private List<String> rack;
    private String mha;
    private String standAloneFlag;
    private String weight;
    private String errorCode;
    private String wcsloadId;
    private String itemDateValue;
    private String scanFlag;
    private String toRack;
    private String toMha;
    private String toX;
    private String toY;
    private String toZ;
    private String fromRack;
    private String fromMha;
    private String fromX;
    private String fromY;
    private String fromZ;
    private String refid;
    private String creationDateTime;
    private String reasonCode;
    private String requestId;
    private String stunitId;
    private String handlingUnitType;
    private String handlingUnitNbr;
    private String information;

    private String sendId;
    private String orderId;

    /**
     * 状态：XMLMessageStatus
     */
    private int status;

    /**
     *  类型：XMLMessageType
     */
    private int type;

    /**
     *  显示的类型：XMLMessageTypeDetail
     */
    private int displayType;
    private Date sendDate;
    public static final String __SENDDATE = "sendDate";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_MESSAGESENDSTATUS")
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Column(name="division")
    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @Column(name="confirmation")
    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public List<String> getRack() {
        return rack;
    }

    public void setRack(List<String> rack) {
        this.rack = rack;
    }

    @Column(name="mha")
    public String getMha() {
        return mha;
    }

    public void setMha(String mha) {
        this.mha = mha;
    }

    @Column(name="standAloneFlag")
    public String getStandAloneFlag() {
        return standAloneFlag;
    }

    public void setStandAloneFlag(String standAloneFlag) {
        this.standAloneFlag = standAloneFlag;
    }

    @Column(name="weight")
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Column(name="errorCode")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Column(name="wcsloadId")
    public String getWcsloadId() {
        return wcsloadId;
    }

    public void setWcsloadId(String wcsloadId) {
        this.wcsloadId = wcsloadId;
    }

    @Column(name="itemDateValue")
    public String getItemDateValue() {
        return itemDateValue;
    }

    public void setItemDateValue(String itemDateValue) {
        this.itemDateValue = itemDateValue;
    }

    @Column(name="scanFlag")
    public String getScanFlag() {
        return scanFlag;
    }

    public void setScanFlag(String scanFlag) {
        this.scanFlag = scanFlag;
    }

    @Column(name="toRack")
    public String getToRack() {
        return toRack;
    }

    public void setToRack(String toRack) {
        this.toRack = toRack;
    }

    @Column(name="toMha")
    public String getToMha() {
        return toMha;
    }

    public void setToMha(String toMha) {
        this.toMha = toMha;
    }

    @Column(name="toX")
    public String getToX() {
        return toX;
    }

    public void setToX(String toX) {
        this.toX = toX;
    }

    @Column(name="toY")
    public String getToY() {
        return toY;
    }

    public void setToY(String toY) {
        this.toY = toY;
    }

    @Column(name="toZ")
    public String getToZ() {
        return toZ;
    }

    public void setToZ(String toZ) {
        this.toZ = toZ;
    }

    @Column(name="fromRack")
    public String getFromRack() {
        return fromRack;
    }

    public void setFromRack(String fromRack) {
        this.fromRack = fromRack;
    }

    @Column(name="fromMha")
    public String getFromMha() {
        return fromMha;
    }

    public void setFromMha(String fromMha) {
        this.fromMha = fromMha;
    }

    @Column(name="fromX")
    public String getFromX() {
        return fromX;
    }

    public void setFromX(String fromX) {
        this.fromX = fromX;
    }

    @Column(name="fromY")
    public String getFromY() {
        return fromY;
    }

    public void setFromY(String fromY) {
        this.fromY = fromY;
    }

    @Column(name="fromZ")
    public String getFromZ() {
        return fromZ;
    }

    public void setFromZ(String fromZ) {
        this.fromZ = fromZ;
    }

    @Column(name="refid")
    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    @Column(name="creationDateTime")
    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    @Column(name="reasonCode")
    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    @Column(name="requestId")
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Column(name="stunitId")
    public String getStunitId() {
        return stunitId;
    }

    public void setStunitId(String stunitId) {
        this.stunitId = stunitId;
    }

    @Column(name="handlingUnitType")
    public String getHandlingUnitType() {
        return handlingUnitType;
    }

    public void setHandlingUnitType(String handlingUnitType) {
        this.handlingUnitType = handlingUnitType;
    }

    @Column(name="handlingUnitNbr")
    public String getHandlingUnitNbr() {
        return handlingUnitNbr;
    }

    public void setHandlingUnitNbr(String handlingUnitNbr) {
        this.handlingUnitNbr = handlingUnitNbr;
    }

    @Column(name = "information")
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

   @Column(name="status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name="TYPE")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name="displayType")
    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    @Column(name="SENDID")
    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    @Column(name="ORDERID")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name="SENDDATE")
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDdate) {
        this.sendDate = sendDdate;
    }

    private Date answerDate;

    @Column(name="ANSWERDATE")
    public Date getAnswerDate()
    {
        return answerDate;
    }

    public void setAnswerDate(Date answerDate)
    {
        this.answerDate = answerDate;
    }

    public static XMLMessage getBySendId(String sendId) {
        Session session = HibernateUtil.getCurrentSession();
        org.hibernate.Query q = session.createQuery("from XMLMessage m where m.sendId = :sendId and m.status = :status");
        q.setString("sendId", sendId);
        q.setInteger("status", XMLMessageStatus.SENT);

        return (XMLMessage) q.uniqueResult();
    }

    public static List<XMLMessage> getMessageForSend(){
        Session session = HibernateUtil.getCurrentSession();
        org.hibernate.Query q = session.createQuery("from XMLMessage m where m.status = :status order by m.creationDateTime,m.id").setInteger("status", XMLMessageStatus.CREATED);
        List<XMLMessage> createList = q.list();

       q = session.createQuery("from XMLMessage m where m.status = :status and m.sendDate <= :sendDate order by m.creationDateTime,m.id").setInteger("status", XMLMessageStatus.SENT).setDate("sendDate",new Date(new Date().getTime() - (10 * 60 * 1000)));
        List<XMLMessage> sentList = q.list();

        List result = new ArrayList();
        for (XMLMessage m: createList){
            result.add(m);
        }

        int TIME_OUT = 5*60*1000;
        for (XMLMessage m: sentList){
            Date time_out = new Date(System.currentTimeMillis() - TIME_OUT);

            if (time_out.after(m.getSendDate())){
                //zhangming 2013/10/3 begin
//                m.setStatus(XMLMessageStatus.TIME_OUT);
//                XMLMessage xm = new XMLMessage();
//                xm.setErrorCode(m.getErrorCode());
//                xm.setStatus(XMLMessageStatus.CREATED);
//                xm.setConfirmation(m.getConfirmation());
//                xm.setCreationDateTime(m.getCreationDateTime());
//                xm.setDisplayType(m.getDisplayType());
//                xm.setDivision(m.getDivision());
//                xm.setFromMha(m.getFromMha());
//                xm.setFromRack(m.getFromRack());
//                xm.setFromX(m.getFromX());
//                xm.setFromY(m.getFromY());
//                xm.setFromZ(m.getFromZ());
//                xm.setHandlingUnitNbr(m.getHandlingUnitNbr());
//                xm.setHandlingUnitType(m.getHandlingUnitType());
//                xm.setInformation(m.getInformation());
//                xm.setItemDateValue(m.getItemDateValue());
//                xm.setMha(m.getMha());
//                xm.setRack(m.getRack());
//                xm.setReasonCode(m.getReasonCode());
//                xm.setRefid(m.getRefid());
//                xm.setRequestId(m.getRequestId());
//                xm.setScanFlag(m.getScanFlag());
//                xm.setStandAloneFlag(m.getStandAloneFlag());
//                xm.setStatus(XMLMessageStatus.CREATED);
//                xm.setStunitId(m.getStunitId());
//                xm.setToMha(m.getToMha());
//                xm.setToRack(m.getToRack());
//                xm.setToX(m.getToX());
//                xm.setToY(m.getToY());
//                xm.setToZ(m.getToZ());
//                xm.setType(m.getType());
//                xm.setWcsloadId(m.getWcsloadId());
//                xm.setWeight(m.getWeight());
//                session.save(xm);
//                session.flush();
//                result.add(xm);
                //end
                result.add(m);
            }
        }

        return result;
    }
}
