package com.domain.XMLbean.XMLList.DataArea.DAList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.domain.XMLbean.XMLList.DataArea.StUnit;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.UserArea.UserArea;
import com.domain.consts.xmlbean.XMLConstant;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-6
 * Time: 上午9:31
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "MovementReportDA")
public class MovementReportDA {
    /**
     * ReasonCode:Carried out as requested
     */
    public static final String ALL_RIGHT = "00";

    @XStreamAlias("RequestId")
    private String requestId;

    @XStreamAlias("FromLocation")
    private FromLocation fromLocation;

    @XStreamAlias("StUnit")
    private StUnit stUnit;

    @XStreamAlias("StUnitId")
    private String stUnitId;

    @XStreamAlias("ToLocation")
    private ToLocation toLocation;

    @XStreamAlias("ReasonCode")
    private String reasonCode;

    @XStreamAlias("Information")
    private String information =XMLConstant.LUAI_INFORMATION;

    @Column(name = "requestId")
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @OneToOne(targetEntity = FromLocation.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "FromLocationID", updatable = true)
    public FromLocation getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(FromLocation fromLocation) {
        this.fromLocation = fromLocation;
    }

    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "StUnit", updatable = true)
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

    @Column(name = "reasonCode")
    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    @Column(name = "information")
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getStUnitId() {
        return stUnitId;
    }

    public void setStUnitId(String stUnitId) {
        this.stUnitId = stUnitId;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "MOVEMENTREPORT_DA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @XStreamAlias("UserArea")
    private UserArea userArea;

    @OneToOne(targetEntity = UserArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserAreaID", updatable = true)
    public UserArea getUserArea() {
        return userArea;
    }

    public void setUserArea(UserArea userArea) {
        this.userArea = userArea;
    }


}
