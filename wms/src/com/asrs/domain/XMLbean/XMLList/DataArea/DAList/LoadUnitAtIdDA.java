package com.asrs.domain.XMLbean.XMLList.DataArea.DAList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.asrs.domain.XMLbean.XMLList.DataArea.ScanData;
import com.asrs.domain.XMLbean.XMLList.DataArea.XMLLocation;
import com.asrs.domain.consts.xmlbean.XMLConstant;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "LoadUnitAtIdDA")
public class LoadUnitAtIdDA {
    @XStreamAlias("WcsLoadId")
    private String wcsLoadId;

    @XStreamAlias("Location")
    private XMLLocation xmlLocation;

    @XStreamAlias("ScanData")
    private String scanData;

    @XStreamAlias("Weight")
    private String weight;

    @XStreamAlias("LoadType")
    private String loadType;

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    @XStreamAlias("StUnitType")
    private String stUnitType=XMLConstant.LUAI_STUNITTYPE;

    @XStreamAlias("ErrorCode")
    private String errorCode =XMLConstant.LUAI_ERROR_CODE;

    @XStreamAlias("StandAloneFlag")
    private String standAloneFlag = XMLConstant.COM_STANDALONE_FLAG;

    @XStreamAlias("Information")
    private String information =XMLConstant.LUAI_INFORMATION;

    @Column(name = "wcsLoadId")
    public String getWcsLoadId() {
        return wcsLoadId;
    }

    public void setWcsLoadId(String wcsLoadId) {
        this.wcsLoadId = wcsLoadId;
    }

    @OneToOne(targetEntity = XMLLocation.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "XMLLocationID", updatable = true)
    public XMLLocation getXMLLocation() {
        return xmlLocation;
    }

    public void setXMLLocation(XMLLocation xmlLocation) {
        this.xmlLocation = xmlLocation;
    }

    public String getScanData() {
        return scanData;
    }

    public void setScanData(String scanData) {
        this.scanData = scanData;
    }

    @Column(name = "weight")
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Column(name = "stUnitType")
    public String getStUnitType() {
        return stUnitType;
    }

    public void setStUnitType(String stUnitType) {
        this.stUnitType = stUnitType;
    }

    @Column(name = "errorCode")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Column(name = "standAloneFlag")
    public String getStandAloneFlag() {
        return standAloneFlag;
    }

    public void setStandAloneFlag(String standAloneFlag) {
        this.standAloneFlag = standAloneFlag;
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
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "LOADUNITATID_DA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
