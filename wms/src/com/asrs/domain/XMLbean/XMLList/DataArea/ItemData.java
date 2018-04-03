package com.asrs.domain.XMLbean.XMLList.DataArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午4:14
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "ItemData")
public class ItemData {
    @XStreamAsAttribute
    @XStreamAlias("AI")
    private String ai="00";

    @XStreamAsAttribute
    @XStreamAlias("Value")
    private String value;

    @Column(name = "ai")
    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "ITEMDATA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
