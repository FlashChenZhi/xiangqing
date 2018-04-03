package com.domain.XMLTransportOrder;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-30
 * Time: 下午12:58
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "xmlTransportOrer")
public class XMLTransportOrder {

    public static final String _FROMLOCATION = "fromLocationNo";
    public static final String _TOLOCATION = "toLocationNo";
    public static final String __WASSBARCODE = "stunitId";
    public static final String __FROMSELECT = "fromMHA";
    public static final String __TOSELECT = "toMHA";
    public static final String __TODATETIME ="creationDateTime" ;

    private int id;
    private String creationDateTime;
    private String refId;
    private String requestId;
    private String priority;
    private String transportType;
    private String identiffiedBy;
    private String partid;
    private String partRev;
    private String zone;
    private String fromMHA;
    private String fromRACK;
    private String fromX;
    private String fromY;
    private String fromZ;
    private String stunitId;
    private String stunitType;
    private String blockCode;
    private String regDate;
    private String toMHA;       // 目标站台
    private String toRACK;
    private String toX;
    private String toY;
    private String toZ;
    private String calcWeight;
    private String errorCode;
    private String updateCreate;
    private String treatmentCode;

    //常量
    private String division;
    private String confirmation;
    private String version;
    private String partDivision;
    private String information;
    private String toLocationNo;
    private String fromLocationNo;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_TRANSPORTORDER")
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Column(name="creationDateTime")
    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
    @Column(name="refId")
    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }
    @Column(name="requestId")
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    @Column(name="priority")
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
    @Column(name="transportType")
    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }
    @Column(name="identiffiedBy")
    public String getIdentiffiedBy() {
        return identiffiedBy;
    }

    public void setIdentiffiedBy(String identiffiedBy) {
        this.identiffiedBy = identiffiedBy;
    }
    @Column(name="partId")
    public String getPartid() {
        return partid;
    }

    public void setPartid(String partid) {
        this.partid = partid;
    }
    @Column(name="partRev")
    public String getPartRev() {
        return partRev;
    }

    public void setPartRev(String partRev) {
        this.partRev = partRev;
    }

    @Column(name="zone")
    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
    @Column(name="fromMHA")
    public String getFromMHA() {
        return fromMHA;
    }

    public void setFromMHA(String fromMHA) {
        this.fromMHA = fromMHA;
    }
    @Column(name="fromRACK")
    public String getFromRACK() {
        return fromRACK;
    }

    public void setFromRACK(String fromRACK) {
        this.fromRACK = fromRACK;
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
    @Column(name="stunitId")
    public String getStunitId() {
        return stunitId;
    }

    public void setStunitId(String stunitId) {
        this.stunitId = stunitId;
    }
    @Column(name="stunitType")
    public String getStunitType() {
        return stunitType;
    }

    public void setStunitType(String stunitType) {
        this.stunitType = stunitType;
    }
    @Column(name="blockCode")
    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }
    @Column(name="regDate")
    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    @Column(name="toMHA")
    public String getToMHA() {
        return toMHA;
    }

    public void setToMHA(String toMHA) {
        this.toMHA = toMHA;
    }


    @Column(name="toRACK")
    public String getToRACK() {
        return toRACK;
    }

    public void setToRACK(String toRACK) {
        this.toRACK = toRACK;
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

    @Column(name="calcWeight")
    public String getCalcWeight() {
        return calcWeight;
    }

    public void setCalcWeight(String calcWeight) {
        this.calcWeight = calcWeight;
    }
    @Column(name="errorCode")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    @Column(name="updateCreate")
    public String getUpdateCreate() {
        return updateCreate;
    }

    public void setUpdateCreate(String updateCreate) {
        this.updateCreate = updateCreate;
    }
    @Column(name="treatmentCode")
    public String getTreatmentCode() {
        return treatmentCode;
    }

    public void setTreatmentCode(String treatmentCode) {
        this.treatmentCode = treatmentCode;
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
    @Column(name="version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    @Column(name="partDivision")
    public String getPartDivision() {
        return partDivision;
    }

    public void setPartDivision(String partDivision) {
        this.partDivision = partDivision;
    }
    @Column(name="information")
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Column(name="toLocationNo")

    public String getToLocationNo() {
        return toLocationNo;
    }

    public void setToLocationNo(String toLocationNo) {
        this.toLocationNo = toLocationNo;
    }
    @Column(name="fromLocationNo")
    public String getFromLocationNo() {
        return fromLocationNo;
    }

    public void setFromLocationNo(String fromLocationNo) {
        this.fromLocationNo = fromLocationNo;
    }
    
    public static boolean wassIdExist(String wassId){
        Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        org.hibernate.Query q = session.createQuery("from XMLTransportOrder o where o.requestId = :requestId")
                .setString("requestId",wassId);
        List result = q.list();
        Transaction.commit();
        if(result.isEmpty()){
            return false;
        } else {
            return true;
        }
    }
}
