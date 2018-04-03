package com.domain.XMLbean.XMLList;

import com.asrs.communication.MessageProxy;
import com.asrs.domain.Plc;
import com.asrs.message.Message01;
import com.asrs.message.Message02;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.DataArea.DAList.WorkStartEndDA;
import com.domain.XMLbean.XMLProcess;
import com.domain.consts.xmlbean.XMLConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;

import javax.persistence.*;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
@Entity
@Table(name = "CancelTransportOrder")
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
    @JoinColumn(name = "CancelTransportOrderDAID", updatable = true)
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
        try {
            Transaction.begin();
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            List<String> list = new ArrayList<String>();
            list.add("00");
            List<Plc> plcs = HibernateUtil.getCurrentSession().createCriteria(Plc.class).list();
            if (dataArea.getType().equals("start")) {
                for (Plc plc : plcs) {
                    Message01 message01 = new Message01();
                    message01.setPlcName(plc.getPlcName());
                    message01.DataCount = "1";
                    message01.MachineNos = list;
                    _wcsproxy.addSndMsg(message01);
                }
            } else if (dataArea.getType().equals("end")) {
                for (Plc plc : plcs) {
                    Message02 message02 = new Message02();
                    message02.setPlcName(plc.getPlcName());
                    message02.DataCount = "1";
                    message02.MachineNos = list;
                    _wcsproxy.addSndMsg(message02);
                }
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }
}
