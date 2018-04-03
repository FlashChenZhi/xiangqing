package com.wms.vo;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/10/19.
 */
public class InOutDataVo {
    private int id;
    private String skuCode;
    private String skuName;
    private String skuSpec;
    private String custSkuName;
    private String custName;
    private String providerName;
    private String orderNo;
    private String batchNo;
    private String lotNum;
    private BigDecimal qty;
    private String skuEom;

    public InOutDataVo() {

    }

    public InOutDataVo(int id, String skuCode, String skuName, String skuSpec, String custSkuName, String custName, String providerName, String orderNo, String batchNo, String lotNum, BigDecimal qty, String skuEom) {
        this.id = id;
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.skuSpec = skuSpec;
        this.custSkuName = custSkuName;
        this.custName = custName;
        this.providerName = providerName;
        this.orderNo = orderNo;
        this.batchNo = batchNo;
        this.lotNum = lotNum;
        this.qty = qty;
        this.skuEom = skuEom;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    public String getCustSkuName() {
        return custSkuName;
    }

    public void setCustSkuName(String custSkuName) {
        this.custSkuName = custSkuName;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getSkuEom() {
        return skuEom;
    }

    public void setSkuEom(String skuEom) {
        this.skuEom = skuEom;
    }

}
