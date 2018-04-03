package com.inventory.vo;

import java.math.BigDecimal;

/**
 * Created by van on 2018/1/14.
 */
public class InventoryVo {

    private int id;
    private String whCode;
    private String itemCode;
    private String itemName;
    private String locationNo;
    private String caseBarCode;
    private String palletNo;
    private String lotNum;
    private BigDecimal qty;
    private String status;
    private String daily;
    private int bank;
    private int lev;
    private int bay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getCaseBarCode() {
        return caseBarCode;
    }

    public void setCaseBarCode(String caseBarCode) {
        this.caseBarCode = caseBarCode;
    }

    public String getPalletNo() {
        return palletNo;
    }

    public void setPalletNo(String palletNo) {
        this.palletNo = palletNo;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public int getLev() {
        return lev;
    }

    public void setLev(int lev) {
        this.lev = lev;
    }

    public int getBay() {
        return bay;
    }

    public void setBay(int bay) {
        this.bay = bay;
    }
}
