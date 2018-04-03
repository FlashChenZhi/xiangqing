package com.domain.XMLbean.XMLList;

import com.domain.XMLbean.XMLProcess;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.DataArea.DAList.HandlingUnitStatusDA;
import com.domain.consts.xmlbean.XMLConstant;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "HandlingUnitStatus")
public class HandlingUnitStatus extends XMLProcess {
    @XStreamAsAttribute
    @XStreamAlias("version")
    private String version = XMLConstant.COM_VERSION;

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private HandlingUnitStatusDA dataArea;

    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = HandlingUnitStatusDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "HandlingUnitStatusDAID", updatable = true)
    public HandlingUnitStatusDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(HandlingUnitStatusDA dataArea) {
        this.dataArea = dataArea;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "HANDLINGUNITSTATUS_SEQ", allocationSize = 1)
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
