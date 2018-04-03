package com.asrs.domain.XMLbean.XMLList;

import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.WorkStartEndDA;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/26.
 */
@Entity
@Table(name = "WorkStartEnd")
public class WorkStartEnd extends XMLProcess {
    @XStreamAsAttribute
    @XStreamAlias("version")
    private String version = XMLConstant.COM_VERSION;

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private WorkStartEndDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = WorkStartEndDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "WorkStartEndDAID", updatable = true)
    public WorkStartEndDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(WorkStartEndDA dataArea) {
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
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "WORKSTARTEND_SEQ", allocationSize = 1)
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
    }
}
