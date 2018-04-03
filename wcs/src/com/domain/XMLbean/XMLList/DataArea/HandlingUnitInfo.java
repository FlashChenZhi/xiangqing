package com.domain.XMLbean.XMLList.DataArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.domain.consts.xmlbean.XMLConstant;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-6
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "HandlingUnitInfo")
public class HandlingUnitInfo {
    /**
     * HandlingUnitType:Rack
     */
    public static final Integer RACK = 01;
    /**
     * HandlingUnitType:Aisle conveyor
     */
    public static final Integer Aisle_CONVEYOR = 02;
    /**
     * HandlingUnitType:Rack
     */
    public static final Integer Crane = 03;


    @XStreamAlias("Location")
    private XMLLocation XMLLocation;


    @XStreamAlias("HandlingUnitType")
    private String handlingUnitType;


    @XStreamAlias("HandlingUnitNbr")
    private String handlingUnitNbr;


    @XStreamAlias("ReasonCode")
    private String reasonCode;

    @XStreamAlias("Information")
    private String information = XMLConstant.LUAI_INFORMATION;

    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "XMLLocation", updatable = true)
    public XMLLocation getXMLLocation() {
        return XMLLocation;
    }

    public void setXMLLocation(XMLLocation XMLLocation) {
        this.XMLLocation = XMLLocation;
    }

    @Column(name = "handlingUnitType")
    public String getHandlingUnitType() {
        return handlingUnitType;
    }

    public void setHandlingUnitType(String handlingUnitType) {
        this.handlingUnitType = handlingUnitType;
    }

    @Column(name = "handlingUnitNbr")
    public String getHandlingUnitNbr() {
        return handlingUnitNbr;
    }

    public void setHandlingUnitNbr(String handlingUnitNbr) {
        this.handlingUnitNbr = handlingUnitNbr;
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

    @XStreamOmitField
    private Integer id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "HANDLINGUNITINFO_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
