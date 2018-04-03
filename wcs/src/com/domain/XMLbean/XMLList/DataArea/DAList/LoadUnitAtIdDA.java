package com.domain.XMLbean.XMLList.DataArea.DAList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.domain.XMLbean.XMLList.DataArea.XMLLocation;
import com.domain.consts.xmlbean.XMLConstant;

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

    @XStreamAlias("Weight")
    private String weight;

    @XStreamAlias("ErrorCode")
    private String errorCode =XMLConstant.LUAI_ERROR_CODE;

    @XStreamAlias("Information")
    private String information =XMLConstant.LUAI_INFORMATION;

    @XStreamAlias("ScanData")
    private String scanDate;

    @XStreamAlias("LoadType")
    private String loadType;

    public String getScanDate() {
        return scanDate;
    }

    public void setScanDate(String scanDate) {
        this.scanDate = scanDate;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

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

    @Column(name = "weight")
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Column(name = "errorCode")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
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
