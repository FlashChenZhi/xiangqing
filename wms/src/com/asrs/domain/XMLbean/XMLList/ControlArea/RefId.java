package com.asrs.domain.XMLbean.XMLList.ControlArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-4
 * Time: 下午1:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "RefId")
public class RefId {
    @XStreamAlias("Id")
    private String referenceId;
    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "REF_ID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "referenceId")
    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String id) {
        this.referenceId = id;
    }
}
