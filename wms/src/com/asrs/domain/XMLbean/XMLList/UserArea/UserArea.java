package com.asrs.domain.XMLbean.XMLList.UserArea;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: wyz
 * Date: 15/2/2
 * Time: 上午11:03
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "UserArea")
public class UserArea {
    @XStreamAlias("OrderId")
    private String orderID;
    @Column(name = "OrderID")
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
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

}

