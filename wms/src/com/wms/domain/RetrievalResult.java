package com.wms.domain;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by wangfan
 * Created on 2017/2/23.
 * 出库实绩
 */
@Entity
@Table(name = "RETRIEVAL_RESULT")
public class RetrievalResult {
    private int id;
    private String skuCode;
    private String skuName;
    private String skuSpec;
    private String custSkuName;
    private String skuEom;
    private String custName;
    private String providerName;
    private String orderNo;
    private String batchNo;
    private String lotNum;
    private String retrievalDate;
    private String retrievalTime;
    private BigDecimal qty;
    private String locationNo;
    private String palletBarcode;
    private String syncFlag;
    private int version;

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
    @Column(name = "SKU_CODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "SKU_NAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Basic
    @Column(name = "SKU_SPEC")
    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    @Basic
    @Column(name = "CUST_SKU_NAME")
    public String getCustSkuName() {
        return custSkuName;
    }

    public void setCustSkuName(String custSkuName) {
        this.custSkuName = custSkuName;
    }

    @Basic
    @Column(name = "SKU_EOM")
    public String getSkuEom() {
        return skuEom;
    }

    public void setSkuEom(String skuEom) {
        this.skuEom = skuEom;
    }

    @Basic
    @Column(name = "CUST_NAME")
    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    @Basic
    @Column(name = "PROVIDER_NAME")
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Basic
    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Basic
    @Column(name = "BATCH_NO")
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    @Basic
    @Column(name = "LOT_NUM")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    @Basic
    @Column(name = "RETRIEVAL_DATE")
    public String getRetrievalDate() {
        return retrievalDate;
    }

    public void setRetrievalDate(String retrievalDate) {
        this.retrievalDate = retrievalDate;
    }

    @Basic
    @Column(name = "RETRIEVAL_TIME")
    public String getRetrievalTime() {
        return retrievalTime;
    }

    public void setRetrievalTime(String retrievalTime) {
        this.retrievalTime = retrievalTime;
    }

    @Basic
    @Column(name = "QTY")
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @Basic
    @Column(name = "LOCATION_NO")
    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    @Basic
    @Column(name = "PALLET_BARCODE")
    public String getPalletBarcode() {
        return palletBarcode;
    }

    public void setPalletBarcode(String palletBarcode) {
        this.palletBarcode = palletBarcode;
    }

    @Basic
    @Column(name = "SYNC_FLAG")
    public String getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(String syncFlag) {
        this.syncFlag = syncFlag;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RetrievalResult that = (RetrievalResult) o;

        if (id != that.id) return false;
        if (qty != that.qty) return false;
        if (version != that.version) return false;
        if (skuCode != null ? !skuCode.equals(that.skuCode) : that.skuCode != null) return false;
        if (skuName != null ? !skuName.equals(that.skuName) : that.skuName != null) return false;
        if (skuSpec != null ? !skuSpec.equals(that.skuSpec) : that.skuSpec != null) return false;
        if (custSkuName != null ? !custSkuName.equals(that.custSkuName) : that.custSkuName != null) return false;
        if (skuEom != null ? !skuEom.equals(that.skuEom) : that.skuEom != null) return false;
        if (custName != null ? !custName.equals(that.custName) : that.custName != null) return false;
        if (providerName != null ? !providerName.equals(that.providerName) : that.providerName != null) return false;
        if (orderNo != null ? !orderNo.equals(that.orderNo) : that.orderNo != null) return false;
        if (batchNo != null ? !batchNo.equals(that.batchNo) : that.batchNo != null) return false;
        if (lotNum != null ? !lotNum.equals(that.lotNum) : that.lotNum != null) return false;
        if (retrievalDate != null ? !retrievalDate.equals(that.retrievalDate) : that.retrievalDate != null)
            return false;
        if (retrievalTime != null ? !retrievalTime.equals(that.retrievalTime) : that.retrievalTime != null)
            return false;
        if (locationNo != null ? !locationNo.equals(that.locationNo) : that.locationNo != null) return false;
        if (palletBarcode != null ? !palletBarcode.equals(that.palletBarcode) : that.palletBarcode != null)
            return false;
        return syncFlag != null ? syncFlag.equals(that.syncFlag) : that.syncFlag == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (skuCode != null ? skuCode.hashCode() : 0);
        result = 31 * result + (skuName != null ? skuName.hashCode() : 0);
        result = 31 * result + (skuSpec != null ? skuSpec.hashCode() : 0);
        result = 31 * result + (custSkuName != null ? custSkuName.hashCode() : 0);
        result = 31 * result + (skuEom != null ? skuEom.hashCode() : 0);
        result = 31 * result + (custName != null ? custName.hashCode() : 0);
        result = 31 * result + (providerName != null ? providerName.hashCode() : 0);
        result = 31 * result + (orderNo != null ? orderNo.hashCode() : 0);
        result = 31 * result + (batchNo != null ? batchNo.hashCode() : 0);
        result = 31 * result + (lotNum != null ? lotNum.hashCode() : 0);
        result = 31 * result + (retrievalDate != null ? retrievalDate.hashCode() : 0);
        result = 31 * result + (retrievalTime != null ? retrievalTime.hashCode() : 0);
        result = 31 * result + (locationNo != null ? locationNo.hashCode() : 0);
        result = 31 * result + (palletBarcode != null ? palletBarcode.hashCode() : 0);
        result = 31 * result + (syncFlag != null ? syncFlag.hashCode() : 0);
        result = 31 * result + version;
        return result;
    }
}
