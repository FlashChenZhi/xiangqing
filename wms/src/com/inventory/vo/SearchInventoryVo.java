package com.inventory.vo;

/**
 * Created by van on 2018/1/14.
 */
public class SearchInventoryVo {
    private String locationNo;
    private String containerBarcode;
    private String whCode;
    private String barcodes;
    private String skuCode;
    private String daily;
    private String batchNo;
    private String status;

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getContainerBarcode() {
        return containerBarcode;
    }

    public void setContainerBarcode(String containerBarcode) {
        this.containerBarcode = containerBarcode;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(String barcodes) {
        this.barcodes = barcodes;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
