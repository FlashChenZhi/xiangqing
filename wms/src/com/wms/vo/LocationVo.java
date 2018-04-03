package com.wms.vo;

/**
 * Created by wangfan
 * Created on 2017/3/10.
 */
public class LocationVo {
    private String locationNo;
    private String bank;
    private String bay;
    private String level;
    private boolean emptyFlag;
    private boolean reserved;
    private String skuType;

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBay() {
        return bay;
    }

    public void setBay(String bay) {
        this.bay = bay;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isEmptyFlag() {
        return emptyFlag;
    }

    public void setEmptyFlag(boolean emptyFlag) {
        this.emptyFlag = emptyFlag;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }
}
