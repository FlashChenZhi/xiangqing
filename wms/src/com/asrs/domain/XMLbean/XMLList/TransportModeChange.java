package com.asrs.domain.XMLbean.XMLList;

import com.asrs.business.consts.StationMode;
import com.wms.domain.Station;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
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
@Table(name = "TransportModeChange")
public class TransportModeChange extends XMLProcess {
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
        String model =dataArea.getTransportType();
        String station=dataArea.getMha();
        try {
            Transaction.begin();
            Station st=Station.getStation(station);
            if(!model.equals(StationMode.UNKNOWN)){
                st.setMode(model);
            }else{
                st.setMode(st.getOldMode());
            }
            st.setModeChangeTime(new Date());
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
        }



    }
}
