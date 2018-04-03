package com.asrs.domain;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;

/**
 * Created by van on 2018/1/19.
 */
@Entity
@Table(name = "INMESSAGE")
public class InMessage {
    private String stationNo;
    private String palletNo;
    private String sku;
    private String lotNum;
    private String errorMessage;
    private String palletStatus;
    private String pcQty;

    @Id
    @Column(name = "STATION_NO")
    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
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
    @Column(name = "SKU")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Basic
    @Column(name = "LOTNUM")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    @Basic
    @Column(name = "ERRORMESSAGE")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Basic
    @Column(name = "PALLET_STATUS")
    public String getPalletStatus() {
        return palletStatus;
    }

    public void setPalletStatus(String palletStatus) {
        this.palletStatus = palletStatus;
    }

    @Basic
    @Column(name = "PCQTY")
    public String getPcQty() {
        return pcQty;
    }

    public void setPcQty(String pcQty) {
        this.pcQty = pcQty;
    }

    public static void error(String blockNo, String message) {
        InMessage inMessage = (InMessage) HibernateUtil.getCurrentSession().get(InMessage.class, blockNo);
        if (inMessage != null) {
            inMessage.setPalletNo(null);
            inMessage.setLotNum(null);
            inMessage.setPalletStatus(null);
            inMessage.setSku(null);
            inMessage.setPcQty(null);
            inMessage.setErrorMessage(message);
        }
    }
}
