package com.domain.XMLbean.XMLList;

import com.domain.XMLbean.XMLProcess;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.DataArea.DAList.LoadUnitAtIdDA;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "LoadUnitAtID")
public class LoadUnitAtID extends XMLProcess {

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private LoadUnitAtIdDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = LoadUnitAtIdDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "LoadUnitAtIdDAID", updatable = true)
    public LoadUnitAtIdDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(LoadUnitAtIdDA dataArea) {
        this.dataArea = dataArea;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "LOADUNITATID_SEQ", allocationSize = 1)
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
