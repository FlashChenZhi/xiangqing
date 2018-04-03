package com.asrs.domain.XMLbean.XMLList.DataArea.DAList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.asrs.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.asrs.domain.XMLbean.XMLList.DataArea.Part;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.asrs.domain.XMLbean.XMLList.UserArea.UserArea;
import com.asrs.domain.consts.xmlbean.XMLConstant;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nan
 * Date: 13-6-6
 * Time: 上午9:30
 */
@Entity
@Table(name = "TransportOrderDA")
public class TransportOrderDA {
    /**
     * transportType:出库
     */
    public static final Integer OUTGOING_ORDER = 1;
    /**
     * transportType:入库
     */
    public static final Integer IN_TRANSPORT = 2;
    /**
     * transportType:再分配
     */
    public static final Integer REARRANGE = 3;
    /**
     * transportType:拒绝（LoadUnitAtID）
     */
    public static final Integer REJECT = 4;


    /**
     * identifiedBy:stationUnitID
     */
    public static final Integer ST_UNIT_ID = 1;
    /**
     * identifiedBy:from location
     */
    public static final Integer FROM_LOCATION = 2;
    /**
     * identifiedBy:part
     */
    public static final Integer PART = 3;
    /**
     * identifiedBy:part & batch
     */
    public static final Integer PART_AND_BATCH = 4;


    @XStreamAlias("RequestId")
    private String requestId;

    @XStreamAlias("Priority")
    private String priority;


    @XStreamAlias("TransportType")
    private String transportType;


    @XStreamAlias("IdentifiedBy")
    private String identifiedBy;

    @XStreamAlias("Part")
    private Part part;

    @XStreamAlias("Batch")
    private String batch;

    @XStreamAlias("FromLocation")
    private FromLocation fromLocation;

    @XStreamAlias("StUnit")
    private StUnit stUnit;

    @XStreamAlias("ToLocation")
    private ToLocation toLocation;


    @XStreamAlias("CalcWeight")
    private String calcWeight;


    @XStreamAlias("ErrorCode")
    private String errorCode;


    @XStreamAlias("TreatmentCode")
    private String treatmentCode;

    @XStreamAlias("Information")
    private String information = XMLConstant.LUAI_INFORMATION;

    @Column(name = "requestId")
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Column(name = "priority")
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Column(name = "transportType")
    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    @Column(name = "identifiedBy")
    public String getIdentifiedBy() {
        return identifiedBy;
    }

    public void setIdentifiedBy(String identifiedBy) {
        this.identifiedBy = identifiedBy;
    }

    @OneToOne(targetEntity = Part.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "PartID", updatable = true)
    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    @Column(name = "batch")
    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    @OneToOne(targetEntity = FromLocation.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "FromLocationID", updatable = true)
    public FromLocation getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(FromLocation fromLocation) {
        this.fromLocation = fromLocation;
    }

    @OneToOne(targetEntity = StUnit.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "StUnitID", updatable = true)
    public StUnit getStUnit() {
        return stUnit;
    }

    public void setStUnit(StUnit stUnit) {
        this.stUnit = stUnit;
    }

    @OneToOne(targetEntity = ToLocation.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ToLocationID", updatable = true)
    public ToLocation getToLocation() {
        return toLocation;
    }

    public void setToLocation(ToLocation toLocation) {
        this.toLocation = toLocation;
    }

    @Column(name = "calcWeight")
    public String getCalcWeight() {
        return calcWeight;
    }

    public void setCalcWeight(String calcWeight) {
        this.calcWeight = calcWeight;
    }

    @Column(name = "errorCode")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Column(name = "treatmentCode")
    public String getTreatmentCode() {
        return treatmentCode;
    }

    public void setTreatmentCode(String treatmentCode) {
        this.treatmentCode = treatmentCode;
    }

    @Column(name = "information")
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "TRANSPORTORDER_DA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne(targetEntity = UserArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserAreaID", updatable = true)
    public UserArea getUserArea() {
        return userArea;
    }

    public void setUserArea(UserArea userArea) {
        this.userArea = userArea;
    }

    @XStreamAlias("UserArea")
    private UserArea userArea;
}
