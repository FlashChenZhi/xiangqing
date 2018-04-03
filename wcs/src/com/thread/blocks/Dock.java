package com.thread.blocks;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;

/**
 * Created by van on 2017/5/3.
 */
@Entity
@Table(name = "DOCK")
public class Dock {
    private int id;
    private String liftNo;
    private String mCarNo;
    private String dockNo;
    private int level;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "LIFT_NO")
    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

    @Column(name = "MCAR_NO")
    public String getmCarNo() {
        return mCarNo;
    }

    public void setmCarNo(String mCarNo) {
        this.mCarNo = mCarNo;
    }

    @Basic
    @Column(name = "DOCK_NO")
    public String getDockNo() {
        return dockNo;
    }

    public void setDockNo(String dockNo) {
        this.dockNo = dockNo;
    }

    @Basic
    @Column(name = "`LEVEL`")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Transient
    public static Dock getDockBydockNo(String dockNo) {
        return (Dock) HibernateUtil.getCurrentSession().createQuery("from Dock where dockNo=:dockNo ").setString("dockNo", dockNo).setMaxResults(1).uniqueResult();
    }
}
