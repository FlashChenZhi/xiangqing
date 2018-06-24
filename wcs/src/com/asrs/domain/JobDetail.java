package com.asrs.domain;

import com.asrs.domain.Inventory;
import com.asrs.domain.Job;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Author: Zhouyue
 * Date: 2010-11-12
 * Time: 12:19:30
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "JOBDETAIL")
public class JobDetail {
    public static final String __INVENTORY = "inventory";
    public static final String __JOB = "job";
    private int _id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    private BigDecimal _qty;

    @Column(name = "QTY")
    @Basic
    public BigDecimal getQty() {
        return _qty;
    }

    public void setQty(BigDecimal qty) {
        _qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobDetail jobDetail = (JobDetail) o;

        if (_id != jobDetail._id) return false;
        if (_qty != jobDetail._qty) return false;
        if (_inventory != null ? !_inventory.equals(jobDetail._inventory) : jobDetail._inventory != null) return false;
        if (_job != null ? !_job.equals(jobDetail._job) : jobDetail._job != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_job != null ? _job.hashCode() : 0);
        result = 31 * result + (_inventory != null ? _inventory.hashCode() : 0);
        return result;
    }

    private Job _job;

    @ManyToOne
    public
    @JoinColumn(name = "JOBID", referencedColumnName = "ID")
    Job getJob() {
        return _job;
    }

    public void setJob(Job job) {
        _job = job;
    }

    private Inventory _inventory;

    @ManyToOne
    public
    @JoinColumn(name = "INVENTORYID", referencedColumnName = "ID")
    Inventory getInventory() {
        return _inventory;
    }

    public void setInventory(Inventory inventory) {
        _inventory = inventory;
    }

}
