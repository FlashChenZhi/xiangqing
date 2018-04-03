package com.wms.vo;

import java.math.BigDecimal;

/**
 * Created by van on 2017/4/3.
 */
public class SkuVo {

    private String skuCode;//商品代码
    private String skuName;//商品名称
    private String skuSpec;//商品规格
    private String custSkuName;//客户货名
    private String skuEom;//计量单位
    private String custName;//货主名称
    private BigDecimal packageQty;//装箱数量
    private BigDecimal palletLoadQTy;//托盘装载数量
    private String skuType;//商品属性

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

    public String getSkuEom() {
        return skuEom;
    }

    public void setSkuEom(String skuEom) {
        this.skuEom = skuEom;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public BigDecimal getPackageQty() {
        return packageQty;
    }

    public void setPackageQty(BigDecimal packageQty) {
        this.packageQty = packageQty;
    }

    public BigDecimal getPalletLoadQTy() {
        return palletLoadQTy;
    }

    public void setPalletLoadQTy(BigDecimal palletLoadQTy) {
        this.palletLoadQTy = palletLoadQTy;
    }

    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }
}
