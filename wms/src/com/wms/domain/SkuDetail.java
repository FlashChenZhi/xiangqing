package com.wms.domain;

import javax.persistence.*;

/**
 * @Author: ed_chen
 * @Date: Create in 19:41 2018/7/18
 * @Description:
 * @Modified By:
 */
@Entity
@Table(name = "SKUDETAIL")
public class SkuDetail {
    private int id;
    private String skuDetailCode;//商品代码
    private String skuDetailName;//商品名称
    private int skuDetailQty;

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
    @Column(name = "SKUDETAILCODE")
    public String getSkuDetailCode() {
        return skuDetailCode;
    }

    public void setSkuDetailCode(String skuDetailCode) {
        this.skuDetailCode = skuDetailCode;
    }
    @Basic
    @Column(name = "SKUDETAILNAME")
    public String getSkuDetailName() {
        return skuDetailName;
    }

    public void setSkuDetailName(String skuDetailName) {
        this.skuDetailName = skuDetailName;
    }
    @Basic
    @Column(name = "SKUDETAILQTY")
    public int getSkuDetailQty() {
        return skuDetailQty;
    }

    public void setSkuDetailQty(int skuDetailQty) {
        this.skuDetailQty = skuDetailQty;
    }

    private Sku sku;

    @ManyToOne
    @JoinColumn(name = "SKUID", referencedColumnName = "ID")
    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }


}
