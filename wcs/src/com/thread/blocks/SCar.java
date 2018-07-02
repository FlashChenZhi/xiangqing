package com.thread.blocks;

import com.asrs.domain.ScarChargeLocation;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 * 子车
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "5")
public class SCar extends Block {
    private int level;
    private int bay;
    private Integer bank;
    private String onMCar;
    private Integer power;
    private String position; //位置 ： 1左，2:右
    private String actualArea;
    private String chargeLocation;//充电位置
    private String tempLocation;//临时货位
    private String chargeChanel;//充电通道
    private Integer groupNo;//组号，用于子车和提升机，母车绑定
    private String load;

    @Basic
    @Column(name = "LEV")
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
    @Column(name = "ONMCAR")
    public String getOnMCar() {
        return onMCar;
    }

    public void setOnMCar(String onMCar) {
        this.onMCar = onMCar;
    }

    @Basic
    @Column(name = "POWER")
    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
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
    @Column(name = "LOAD_FLAG")
    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
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
    @Column(name = "BANK")
    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    @Basic
    @Column(name = "CHARGELOCATION")
    public String getChargeLocation() {
        return chargeLocation;
    }

    public void setChargeLocation(String chargeLocation) {
        this.chargeLocation = chargeLocation;
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
    @Column(name = "TEMP_LOCATION")
    public String getTempLocation() {
        return tempLocation;
    }

    public void setTempLocation(String tempLocation) {
        this.tempLocation = tempLocation;
    }

    @Basic
    @Column(name = "CHARGE_CHANEL")
    public String getChargeChanel() {
        return chargeChanel;
    }

    public void setChargeChanel(String chargeChanel) {
        this.chargeChanel = chargeChanel;
    }

    @Transient
    public static SCar getScarByGroup(Integer groupNo) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where groupNo =:groupNo");
        query.setParameter("groupNo", groupNo);
        query.setMaxResults(1);
        return (SCar) query.uniqueResult();
    }



}
