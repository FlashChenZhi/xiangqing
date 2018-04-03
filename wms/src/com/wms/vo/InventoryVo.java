package com.wms.vo;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/3/6.
 */
public class InventoryVo {
    private int id;
    private String skuCode;
    private String orderNo;
    private String lotNum;
    private BigDecimal qty;
    private String locationNo;
    private String palletBarcode;
    private String caseBarCode;

    public InventoryVo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getPalletBarcode() {
        return palletBarcode;
    }

    public void setPalletBarcode(String palletBarcode) {
        this.palletBarcode = palletBarcode;
    }

    public String getCaseBarCode() {
        return caseBarCode;
    }

    public void setCaseBarCode(String caseBarCode) {
        this.caseBarCode = caseBarCode;
    }

}
