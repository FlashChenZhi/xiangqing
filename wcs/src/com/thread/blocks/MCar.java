package com.thread.blocks;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.Location;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/11/1.
 * 母车
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "4")
public class MCar extends Block {
    private String sCarBlockNo;
    private boolean liftSide;
    private int bay;
    private int level;
    private String dock;
    private String liftNo;
    private String position; //位置 ： 1左，2:右
    private String actualArea;
    private Boolean checkLocation;//是否校准位置
    private Integer groupNo;
    private String cycle;


    @Basic
    @Column(name = "liftNo")
    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

    @Basic
    @Column(name = "LEV")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "liftSide")
    public boolean isLiftSide() {
        return liftSide;
    }

    public void setLiftSide(boolean liftSide) {
        this.liftSide = liftSide;
    }

    @Transient
    public String getDock(String mCarNo, String liftNo) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from Dock where mCarNo=:mcarNo and liftNo=:liftNO").setMaxResults(1);
        query.setParameter("mcarNo", mCarNo);
        query.setParameter("liftNO", liftNo);
        Dock dock = (Dock) query.uniqueResult();
        return  dock.getDockNo();
    }

    @Basic
    @Column(name = "BAY")
    public int getBay() {
        return bay;
    }

    public void setBay(int bay) {
        this.bay = bay;
    }

    @Basic
    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
    @Column(name = "SCARBLOCKNO")
    public String getsCarBlockNo() {
        return sCarBlockNo;
    }

    public void setsCarBlockNo(String sCarBlockNo) {
        this.sCarBlockNo = sCarBlockNo;
    }

    @Basic
    @Column(name = "ACTUALAREA")
    public String getActualArea() {
        return actualArea;
    }

    public void setActualArea(String actuaneArea) {
        this.actualArea = actuaneArea;
    }

    @Basic
    @Column(name = "CHECK_LOCATION")
    public Boolean getCheckLocation() {
        return checkLocation;
    }

    public void setCheckLocation(Boolean checkLocation) {
        this.checkLocation = checkLocation;
    }

    @Basic
    @Column(name = "GROUP_NO")
    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    @Basic
    @Column(name = "CYCLE")
    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    @Transient
    public boolean arrive(Location toLocation) {
        if (toLocation!=null && this.getLevel() == toLocation.getLevel() && this.getBay() == toLocation.getBay() && this.getActualArea().equals(toLocation.getActualArea()) && this.getCheckLocation())
            return true;
        return false;
    }

    @Transient
    public static MCar getMCarByGroupNo(Integer groupNo) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from MCar where groupNo =:groupNo");
        query.setParameter("groupNo", groupNo);
        return (MCar) query.uniqueResult();
    }

    @Transient
    public static MCar getMCarByPosition(String position,int level) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from MCar where position =:po and level=:lv");
        query.setParameter("po", position);
        query.setParameter("lv",level);
        query.setMaxResults(1);
        return (MCar) query.uniqueResult();

    }

    @Transient
    public static MCar getMCarByOtherLevOutKuAsrsJob(String position) {
        //查找没有小车并且没有小车正在赶往此母车并且此母车有出库任务 的母车
        Query query = HibernateUtil.getCurrentSession().createQuery("from MCar m where m.sCarBlockNo is null and " +
                "m.position=:position and " +
                "not exists (select 1 from AsrsJob a where a.type=:tp and a.toStation = m.blockNo ) " +
                "and " +
                "exists (select 1 from AsrsJob a where a.type =:tp1 and statusDetail = '0' and fromStation=m.blockNo )");
        query.setString("tp", AsrsJobType.CHANGELEVEL);
        query.setString("position", position);
        query.setString("tp1", AsrsJobType.RETRIEVAL);
        query.setMaxResults(1);
        MCar toMCar = (MCar) query.uniqueResult();
        return (MCar) query.uniqueResult();
    }

}
