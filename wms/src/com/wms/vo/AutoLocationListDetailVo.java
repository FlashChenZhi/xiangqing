package com.wms.vo;

/**
 * Created by xiongying on 15/11/9.
 */
public class AutoLocationListDetailVo {
    private String containerBarcode;
    private String skuCode;
    private String qty;
    private String eom;
    private String productDate;
    private String qaStatus;
    private String batchNo;

    public String getContainerBarcode() {
        return containerBarcode;
    }

    public void setContainerBarcode(String containerBarcode) {
        this.containerBarcode = containerBarcode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getEom() {
        return eom;
    }

    public void setEom(String eom) {
        this.eom = eom;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getQaStatus() {
        return qaStatus;
    }

    public void setQaStatus(String qaStatus) {
        this.qaStatus = qaStatus;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
