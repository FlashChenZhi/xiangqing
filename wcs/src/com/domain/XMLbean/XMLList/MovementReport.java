package com.domain.XMLbean.XMLList;

import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLProcess;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "MovementReport")
public class MovementReport extends XMLProcess {
    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private MovementReportDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = MovementReportDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "MovementReportDAID", updatable = true)
    public MovementReportDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(MovementReportDA dataArea) {
        this.dataArea = dataArea;
    }

    @XStreamOmitField
    private int id;


    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "MOVEMENTREPORT_SEQ", allocationSize = 1)
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
