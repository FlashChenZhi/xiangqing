package com.domain.XMLbean.XMLList.DataArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-6
 * Time: 上午10:11
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "FromLocation")
public class FromLocation {
    @XStreamAlias("MHA")
    private String MHA;

    @XStreamImplicit(itemFieldName = "Rack")
    private List<String> rack;

    @Column(name = "MHA")
    public String getMHA() {
        return MHA;
    }

    public void setMHA(String MHA) {
        this.MHA = MHA;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "FROMLOCATION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getRack() {
        return rack;
    }

    public void setRack(List<String> rack) {
        this.rack = rack;
    }
}
