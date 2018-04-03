package com.domain.XMLbean.XMLList;

import com.asrs.business.consts.StationMode;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Station;
import com.asrs.message.Message40;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.TransportModeChangeDA;
import com.domain.XMLbean.XMLList.DataArea.DAList.WorkStartEndDA;
import com.domain.XMLbean.XMLProcess;
import com.domain.consts.xmlbean.XMLConstant;
import com.thread.blocks.StationBlock;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.*;

import javax.persistence.*;
import java.rmi.Naming;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
@Entity
@Table(name = "TransportModeChange")
//TransportModeChange
public class TransportModeChange extends XMLProcess {

    public static void main(String[] args) throws Exception {
        Message40 message40 = new Message40();
        message40.setPlcName("BL01");
        message40.Station = "1301";
        message40.Mode = "01";
        MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
        _wcsproxy.addSndMsg(message40);

    }

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

            StationBlock stationBlock = StationBlock.getByStationNo(dataArea.getMha());

            org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where toStation=:station or fromStation=:fStatiom");
            query.setString("station", stationBlock.getBlockNo());
            query.setString("fStatiom", stationBlock.getBlockNo());
            List<AsrsJob> list = query.list();

            if (list.isEmpty()) {
                Session session = HibernateUtil.getCurrentSession();
                Station station = Station.getStation(dataArea.getMha());
                station.setOldMode(station.getMode());
                station.setMode("09");
                session.saveOrUpdate(station);
                StationBlock block = (StationBlock) session.createQuery("from StationBlock sb where sb.stationNo = :stationNo")
                        .setParameter("stationNo", dataArea.getMha()).uniqueResult();
                Message40 message40 = new Message40();
                message40.setPlcName(block.getPlcName());
                message40.Station = dataArea.getMha();
                message40.Mode = dataArea.getTransportType();
                MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
                _wcsproxy.addSndMsg(message40);
            } else {
                //有任务，不允许切换，回复NG给wms
                Sender sender = new Sender();
                sender.setDivision(XMLConstant.COM_DIVISION);
                ControlArea controlArea = new ControlArea();
                controlArea.setSender(sender);
                TransportModeChangeDA transportModeChangeDA = new TransportModeChangeDA();
                transportModeChangeDA.setMha(this.dataArea.getMha());
                transportModeChangeDA.setTransportType(StationMode.UNKNOWN);
                transportModeChangeDA.setInformation("03");
                TransportModeChangeReport transportModeChangeReport = new TransportModeChangeReport();
                transportModeChangeReport.setDate(new Date());
                transportModeChangeReport.setControlArea(controlArea);
                transportModeChangeReport.setDataArea(transportModeChangeDA);
                Envelope envelope = new Envelope();
                envelope.setTransportModeChangeReport(transportModeChangeReport);
                try {
                    XMLUtil.sendEnvelope(envelope);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }
}
