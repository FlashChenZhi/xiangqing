package com.wms.domain.blocks;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/16.
 * 输送机
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "1")
public class Conveyor extends Block {
    private String dock;
    private String onCar;
    private Integer lev;
    private boolean manty;
    private boolean mantWaiting;
    private String stationNo;

    @Basic
    @Column(name = "DOCK")
    public String getDock() {
        return dock;
    }

    public void setDock(String dock) {
        this.dock = dock;
    }

    @Basic
    @Column(name = "ONCAR")
    public String getOnCar() {
        return onCar;
    }

    public void setOnCar(String onCar) {
        this.onCar = onCar;
    }

    @Basic
    @Column(name = "MANTY")
    public boolean isManty() {
        return manty;
    }

    public void setManty(boolean manty) {
        this.manty = manty;
    }

    @Basic
    @Column(name = "MANT_WAITING")
    public boolean isMantWaiting() {
        return mantWaiting;
    }

    public void setMantWaiting(boolean mantWaiting) {
        this.mantWaiting = mantWaiting;
    }

    @Basic
    @Column(name = "LEV")
    public Integer getLev() {
        return lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
    }

    @Basic
    @Column(name = "stationNo")
    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public static Conveyor getByStationNo(String stationNo) {
        return (Conveyor) HibernateUtil.getCurrentSession().createQuery("from Conveyor sb where sb.stationNo = :stationNo")
                .setString("stationNo", stationNo).uniqueResult();
    }
}
