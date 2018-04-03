package com.asrs.domain.XMLbean.XMLList;

import com.asrs.domain.XMLbean.XMLProcess;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.AcceptLoadUnitAtIdDA;
import com.asrs.domain.consts.xmlbean.XMLConstant;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-4
 * Time: 下午1:26
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "AcceptLoadUnitAtID")
public class AcceptLoadUnitAtID extends XMLProcess {
    @XStreamAsAttribute
    @XStreamAlias("version")
    private String version = XMLConstant.COM_VERSION;

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private AcceptLoadUnitAtIdDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = AcceptLoadUnitAtIdDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "AcceptLoadUnitAtIdDAID", updatable = true)
    public AcceptLoadUnitAtIdDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(AcceptLoadUnitAtIdDA dataArea) {
        this.dataArea = dataArea;
    }

    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "ACCEPTLOADUNITATID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void execute() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
