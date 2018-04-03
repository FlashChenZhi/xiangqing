package com.asrs.domain.XMLbean.XMLList.DataArea.DAList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "TransportModeChange")
public class TransportModeChangeDA {

    @XStreamAlias("TransportType")
    private String transportType;

    @Column(name = "transportType")
    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    @XStreamAlias("MHA")
    private String mha;

    @Column(name = "mha")
    public String getMha() {
        return mha;
    }

    public void setMha(String mha) {
        this.mha = mha;
    }

    @XStreamAlias("Information")
    private String information;

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
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "TRANSPORTMODECHANGE_DA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
