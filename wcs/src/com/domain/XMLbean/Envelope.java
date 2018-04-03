package com.domain.XMLbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.domain.XMLbean.XMLList.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nan
 * Date: 13-6-19
 * Time: 下午1:12
 * envelope of WMS_WCS
 */
@Entity
@Table(name = "WcsEnvelope")
@XStreamAlias("WmsWcsXML_Envelope")
public class Envelope implements Serializable {
    @XStreamAlias("TransportModeChange")
    private TransportModeChange transportModeChange;

    @XStreamAlias("TransportModeChangeReport")
    private TransportModeChangeReport transportModeChangeReport;

    @XStreamAlias("WorkStartEnd")
    private WorkStartEnd workStartEnd;

    @XStreamAlias("AcceptLoadUnitAtId")
    private AcceptLoadUnitAtID acceptLoadUnitID;

    @XStreamAlias("AcceptTransportOrder")
    private AcceptTransportOrder acceptTransportOrder;

    @XStreamAlias("CancelTransportOrder")
    private CancelTransportOrder cancelTransportOrder;

    @XStreamAlias("HandlingUnitStatus")
    private HandlingUnitStatus handlingUnitStatus;

    @XStreamAlias("LoadUnitAtID")
    private LoadUnitAtID loadUnitAtID;

    @XStreamAlias("MovementReport")
    private MovementReport movementReport;

    @XStreamAlias("TransportOrder")
    private TransportOrder transportOrder;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TransportModeChangeReportID", updatable = true)
    public TransportModeChangeReport getTransportModeChangeReport() {
        return transportModeChangeReport;
    }

    public void setTransportModeChangeReport(TransportModeChangeReport transportModeChangeReport) {
        this.transportModeChangeReport = transportModeChangeReport;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TransportModeChangeID", updatable = true)
    public TransportModeChange getTransportModeChange() {
        return transportModeChange;
    }

    public void setTransportModeChange(TransportModeChange transportModeChange) {
        this.transportModeChange = transportModeChange;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "WorkStartEndID", updatable = true)
    public WorkStartEnd getWorkStartEnd() {
        return workStartEnd;
    }

    public void setWorkStartEnd(WorkStartEnd workStartEnd) {
        this.workStartEnd = workStartEnd;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TransportOrderID", updatable = true)
    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    public void setTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
    }

    @OneToOne(targetEntity = MovementReport.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "MovementReportID", updatable = true)
    public MovementReport getMovementReport() {
        return movementReport;
    }

    public void setMovementReport(MovementReport movementReport) {
        this.movementReport = movementReport;
    }

    @OneToOne(targetEntity = LoadUnitAtID.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "LoadUnitAtIDID", updatable = true)
    public LoadUnitAtID getLoadUnitAtID() {
        return loadUnitAtID;
    }

    public void setLoadUnitAtID(LoadUnitAtID loadUnitAtID) {
        this.loadUnitAtID = loadUnitAtID;
    }

    public AcceptTransportOrder getAcceptTransportOrder() {
        return acceptTransportOrder;
    }

    public void setAcceptTransportOrder(AcceptTransportOrder acceptTransportOrder) {
        this.acceptTransportOrder = acceptTransportOrder;
    }

    @OneToOne(targetEntity = HandlingUnitStatus.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "HandlingUnitStatusID", updatable = true)
    public HandlingUnitStatus getHandlingUnitStatus() {
        return handlingUnitStatus;
    }

    public void setHandlingUnitStatus(HandlingUnitStatus handlingUnitStatus) {
        this.handlingUnitStatus = handlingUnitStatus;
    }

    @OneToOne(targetEntity = CancelTransportOrder.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "CancelTransportOrderID", updatable = true)
    public CancelTransportOrder getCancelTransportOrder() {
        return cancelTransportOrder;
    }

    public void setCancelTransportOrder(CancelTransportOrder cancelTransportOrder) {
        this.cancelTransportOrder = cancelTransportOrder;
    }

    @OneToOne(targetEntity = AcceptLoadUnitAtID.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "AcceptLoadUnitAtIDID", updatable = true)
    public AcceptLoadUnitAtID getAcceptLoadUnitID() {
        return acceptLoadUnitID;
    }

    public void setAcceptLoadUnitID(AcceptLoadUnitAtID acceptLoadUnitID) {
        this.acceptLoadUnitID = acceptLoadUnitID;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "ENVELOPE_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
