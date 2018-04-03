package com.wms.domain;

import javax.persistence.*;

/**
 * Created by van on 2017/12/15.
 */
@Entity
@Table(name = "WMS_JOB_INVENTORY")
public class WmsJobInventory {

    private int id;
    private String palletNo;
    private boolean send;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PALLETNO")
    public String getPalletNo() {
        return palletNo;
    }

    public void setPalletNo(String palletNo) {
        this.palletNo = palletNo;
    }

    @Basic
    @Column(name = "SEND")
    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }
}
