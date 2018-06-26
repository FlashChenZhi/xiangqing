package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by van on 2017/12/14.
 */
@Entity
@Table(name = "INVENTORY_IFAC")
public class InventoryView {

    private int id;
    private String whCode;
    private String palletCode;
    private String skuCode;
    private String skuName;
    private String lotNum;
    private BigDecimal qty;
    private BigDecimal caseQty;
    private String caseBarCode;


    @Id
    @Column(name = "INV_STOR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Basic
    @Column(name = "WH_CODE")
    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    @Basic
    @Column(name = "PALLET_CODE")
    public String getPalletCode() {
        return palletCode;
    }

    public void setPalletCode(String palletCode) {
        this.palletCode = palletCode;
    }

    @Basic
    @Column(name = "ITEM_CODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "ITEM_NAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Basic
    @Column(name = "BATCH")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
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
    @Column(name = "CASE_QTY")
    public BigDecimal getCaseQty() {
        return caseQty;
    }

    public void setCaseQty(BigDecimal caseQty) {
        this.caseQty = caseQty;
    }

    @Basic
    @Column(name = "LM_CODE")
    public String getCaseBarCode() {
        return caseBarCode;
    }

    public void setCaseBarCode(String caseBarCode) {
        this.caseBarCode = caseBarCode;
    }

    public static InventoryView getByPalletNo(String palletNo) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView iv where iv.palletCode = :palletCode")
                .setString("palletCode",palletNo);
        return (InventoryView) query.uniqueResult();
    }
}
