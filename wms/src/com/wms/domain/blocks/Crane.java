package com.wms.domain.blocks;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/16.
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "7")
public class Crane extends Block {
    private String sCarNo;
    private int bay;
    private int level;

    @Basic
    @Column(name = "`LEVEL`")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
    @Column(name = "SCARNO")
    public String getsCarNo() {
        return sCarNo;
    }

    public void setsCarNo(String sCarNo) {
        this.sCarNo = sCarNo;
    }

    @Transient
    public boolean isOkToGo(String mcKey, int bay, int level) {
        if (!this.waitingResponse && mcKey.equals(this.reservedMcKey) && this.bay == bay && this.level == level) {
            return true;
        } else {
            return false;
        }
    }

    @Transient
    public static Crane getCrane(){
        return (Crane) HibernateUtil.getCurrentSession().createCriteria(Crane.class).setMaxResults(1).uniqueResult();
    }

}
