package com.domain.XMLbean.XMLList.DataArea.DAList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-4
 * Time: 下午1:28
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "AcceptLoadUnitAtIdDA")
public class AcceptLoadUnitAtIdDA {
    @XStreamAlias("WcsLoadId")
    private String wcsloadId;

    @XStreamAlias("StUnitId")
    private String stUnitID;

    @Column(name = "stUnitID")
    public String getStUnitID() {
        return stUnitID;
    }

    public void setStUnitID(String stUnitID) {
        this.stUnitID = stUnitID;
    }

    @Column(name = "wcsloadId")
    public String getWcsloadId() {
        return wcsloadId;
    }

    public void setWcsloadId(String wcsloadId) {
        this.wcsloadId = wcsloadId;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "ACCEPTLOADUNITATID_DA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
