package com.thread.blocks;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/28.
 * 站台
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "3")
public class StationBlock extends Block {
    protected String stationNo;
    private String dock;
    private String liftNo;
    private String buffMckey;
    private String load;
    private String inPostion;
    private Integer groupNo;//组号，绑定出库双站台
    private boolean outLoad;//出库时候货位是否身上

    @Basic
    @Column(name = "OUTLOAD")
    public boolean isOutLoad() {
        return outLoad;
    }

    public void setOutLoad(boolean outLoad) {
        this.outLoad = outLoad;
    }

    @Basic
    @Column(name = "GROUP_NO")
    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer group) {
        this.groupNo = group;
    }
    @Basic
    @Column(name = "stationNo")
    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    @Basic
    @Column(name = "liftNo")
    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

    @Basic
    @Column(name = "DOCK")
    public String getDock() {
        return dock;
    }

    public void setDock(String dock) {
        this.dock = dock;
    }

    @Basic
    @Column(name = "BUFF_MCKEY")
    public String getBuffMckey() {
        return buffMckey;
    }

    public void setBuffMckey(String buffMckey) {
        this.buffMckey = buffMckey;
    }

    @Basic
    @Column(name = "LOAD_FLAG")
    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    @Basic
    @Column(name = "IN_POSITION")
    public String getInPostion() {
        return inPostion;
    }

    public void setInPostion(String inPostion) {
        this.inPostion = inPostion;
    }

    public static StationBlock getByStationNo(String stationNo) {
        return (StationBlock) HibernateUtil.getCurrentSession().createQuery("from StationBlock sb where sb.stationNo = :stationNo")
                .setString("stationNo", stationNo).uniqueResult();
    }
}
