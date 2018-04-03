package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by wangfan
 * Created on 2017/2/23.
 * 入库数据
 */
@Entity
@Table(name = "ReceivingPlan")
public class ReceivingPlan {
    private int id;

    private String providerName;
    private String orderNo;
    private String sku;
    private String batchNo;
    private String lotNum;
    private BigDecimal qty;
    private BigDecimal recvedQty = BigDecimal.ZERO;
    private String status;
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
    @Column(name = "PROVIDER_NAME")
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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
    @Column(name = "QTY")
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @Basic
    @Column(name = "RECVEDQTY")
    public BigDecimal getRecvedQty() {
        return recvedQty;
    }

    public void setRecvedQty(BigDecimal recvedQty) {
        this.recvedQty = recvedQty;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
    @Column(name = "STATUS")
    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "SKU")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public static ReceivingPlan getByOrderNo(String orderNo, String skuCode) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from ReceivingPlan  where orderNo=:orderNo and sku.skuCode=:skuCode");
        query.setParameter("orderNo", orderNo);
        query.setParameter("skuCode", skuCode);
        query.setMaxResults(1);
        return (ReceivingPlan) query.uniqueResult();
    }

    public static ReceivingPlan getByLotNum(String lotNum, String skuCode) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from ReceivingPlan  where batchNo=:lotNum and status<>'3' and sku.skuCode=:skuCode");
        query.setParameter("lotNum", lotNum);
        query.setParameter("skuCode", skuCode);
        query.setMaxResults(1);
        return (ReceivingPlan) query.uniqueResult();
    }
}
