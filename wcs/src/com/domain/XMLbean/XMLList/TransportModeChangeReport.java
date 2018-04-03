package com.domain.XMLbean.XMLList;

import com.asrs.communication.MessageProxy;
import com.asrs.domain.Station;
import com.asrs.message.Message40;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.DataArea.DAList.TransportModeChangeDA;
import com.domain.XMLbean.XMLList.DataArea.DAList.WorkStartEndDA;
import com.domain.XMLbean.XMLProcess;
import com.thread.blocks.StationBlock;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;

import javax.persistence.*;
import java.rmi.Naming;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/26.
 */
@Entity
@Table(name = "TransportModeChangeReport")
public class TransportModeChangeReport extends XMLProcess {
    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private TransportModeChangeDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = WorkStartEndDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "TransportModeChangeDAID", updatable = true)
    public TransportModeChangeDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(TransportModeChangeDA dataArea) {
        this.dataArea = dataArea;
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
            String model = dataArea.getTransportType();
            String stationNo = dataArea.getMha();
            Station station = Station.getStation(stationNo);
            StationBlock block = (StationBlock) HibernateUtil.getCurrentSession().createQuery("from StationBlock sb where sb.stationNo = :stationNo").uniqueResult();

            if (!model.equals(station.getMode())) {
                station.setOldMode(station.getMode());
                station.setMode(model);
                station.setModeChangeTime(new Date());
            }
            Message40 message40 = new Message40();
            message40.setPlcName(block.getPlcName());
            message40.Mode = model;
            message40.Station = stationNo;
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            _wcsproxy.addSndMsg(message40);
            Transaction.commit();

        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
        }
    }
}
