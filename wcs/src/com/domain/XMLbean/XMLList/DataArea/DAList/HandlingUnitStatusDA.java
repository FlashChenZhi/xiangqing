package com.domain.XMLbean.XMLList.DataArea.DAList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.domain.XMLbean.XMLList.DataArea.HandlingUnitInfo;

import javax.persistence.*;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-6
 * Time: 上午9:33
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "HandlingUnitStatusDA")
public class HandlingUnitStatusDA {
    @XStreamAlias("HandlingUnitInfo")
    private HandlingUnitInfo handlingUnitInfo;

    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "HandlingUnitInfo", updatable = true)
    public HandlingUnitInfo getHandlingUnitInfo() {
        return handlingUnitInfo;
    }

    public void setHandlingUnitInfo(HandlingUnitInfo handlingUnitInfo) {
        this.handlingUnitInfo = handlingUnitInfo;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "HANDLINGUNITSTATUS_DA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
