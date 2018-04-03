package com.asrs.domain.XMLbean.XMLList;

import com.asrs.domain.XMLbean.XMLProcess;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.asrs.xml.util.XMLUtil;
import com.asrs.domain.XMLTransportOrder.XMLTransportOrder;
import com.asrs.domain.XMLTransportOrder.XMLTransportOrderUtil;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.UserArea.UserArea;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.util.hibernate.Transaction;

import javax.persistence.*;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:17
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "TransportOrder")
public class TransportOrder extends XMLProcess {
    @XStreamAlias("version")
    @XStreamAsAttribute
    private String version = XMLConstant.COM_VERSION;

    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private TransportOrderDA dataArea;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TransportOrderDAID", updatable = true)
    public TransportOrderDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(TransportOrderDA dataArea) {
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
        UserArea ua = this.getDataArea().getUserArea();
        if(ua == null){
            ua = new UserArea();
            ua.setOrderID("");
            this.getDataArea().setUserArea(ua);
        }

        Date priority = XMLUtil.convertDate(dataArea.getPriority());
        String wassId = dataArea.getRequestId();

//        if(XMLTransportOrder.wassIdExist(wassId)){
//            LogWriter.error(this.getClass(), "wass ID:" + wassId +"exits, ignored!");
//            return;
//        }

        XMLTransportOrder xmlTransportOrder = XMLTransportOrderUtil.getTransportOrder(this);
        try {
            Transaction.begin();
            XMLTransportOrderUtil.add(xmlTransportOrder);
            Transaction.commit();
        } catch (RuntimeException ex) {
            Transaction.rollback();
            return;
        }

    }
}

