package com.asrs.domain.XMLbean.XMLList.DataArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-6
 * Time: 上午9:53
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "Part")
public class Part {
    @XStreamAlias("PartId")
    private String partId;

    @XStreamAlias("PartRev")
    private String partRev;

    @XStreamAlias("Division")
    private String division;

    @XStreamAlias("Zone")
    private String zone;

    @Column(name = "partId")
    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    @Column(name = "partRev")
    public String getPartRev() {
        return partRev;
    }

    public void setPartRev(String partRev) {
        this.partRev = partRev;
    }

    @Column(name = "division")
    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @Column(name = "zone")
    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "PART_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
