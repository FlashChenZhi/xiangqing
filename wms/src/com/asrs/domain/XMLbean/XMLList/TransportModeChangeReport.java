package com.asrs.domain.XMLbean.XMLList;

import com.wms.domain.Station;
import com.asrs.xml.util.XMLUtil;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportModeChangeDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.WorkStartEndDA;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.hibernate.Transaction;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/26.
 */
@Entity
@Table(name = "TransportModeChangeReport")
public class TransportModeChangeReport extends XMLProcess {
    @XStreamAsAttribute
    @XStreamAlias("version")
    private String version = XMLConstant.COM_VERSION;

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
            String model = dataArea.getTransportType();
            String stationNo = dataArea.getMha();
            Station station = Station.getStation(stationNo);

            if (!model.equals(station.getMode())) {
                station.setOldMode(station.getMode());
                station.setMode(model);
                station.setModeChangeTime(new Date());
            }
            Sender sender = new Sender();
            sender.setDivision(XMLConstant.COM_DIVISION);
            ControlArea controlArea = new ControlArea();
            controlArea.setSender(sender);
            TransportModeChangeDA transportModeChangeDA = new TransportModeChangeDA();
            transportModeChangeDA.setMha(stationNo);
            transportModeChangeDA.setTransportType(model);
            TransportModeChangeReport transportModeChangeReport = new TransportModeChangeReport();
            transportModeChangeReport.setDate(new Date());
            transportModeChangeReport.setControlArea(controlArea);
            transportModeChangeReport.setDataArea(transportModeChangeDA);
            Envelope envelope = new Envelope();
            envelope.setTransportModeChangeReport(transportModeChangeReport);
            XMLUtil.sendEnvelope(envelope);
            Transaction.commit();

        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
        }
    }
}
