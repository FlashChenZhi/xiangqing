package com.domain.XMLbean.XMLList.DataArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-6
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "StUnit")
public class StUnit {
    @XStreamAlias("StUnitId")
    private String stUnitID;

    @XStreamAlias("StUnitType")
    private String stUnitType;


    @XStreamAlias("BlockCode")
    private String blockCode;

    @XStreamAlias("RegDate")
    private String regDate;

    @Column(name = "stUnitID")
    public String getStUnitID() {
        return stUnitID;
    }

    public void setStUnitID(String stUnitID) {
        this.stUnitID = stUnitID;
    }

    @Column(name = "stUnitType")
    public String getStUnitType() {
        return stUnitType;
    }

    public void setStUnitType(String stUnitType) {
        this.stUnitType = stUnitType;
    }

    @Column(name = "blockCode")
    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    @Column(name = "regDate")
    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "STUNITID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
