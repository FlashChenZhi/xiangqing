package com.asrs.domain;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wangfan
 * Created on 2017/2/23.
 * 商品数据
 */
@Entity
@Table(name = "SKU")
public class Sku {

    public static final String __SKUCODE = "skuCode";
    private int id;
    private String skuCode;//商品代码
    private String skuName;//商品名称
    private String skuSpec;//商品规格
    private String custSkuName;//客户货名
    private String skuEom;//计量单位
    private String custName;//货主名称
    private BigDecimal packageQty;//装箱数量
    private BigDecimal palletLoadQTy;//托盘装载数量
    private String skuType;//商品属性
    private int version;
    private String lotNum;

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
    @Column(name = "PACKAGE_QTY")
    public BigDecimal getPackageQty() {
        return packageQty;
    }

    public void setPackageQty(BigDecimal packageQty) {
        this.packageQty = packageQty;
    }

    @Basic
    @Column(name = "PALLET_LOAD_QTY")
    public BigDecimal getPalletLoadQTy() {
        return palletLoadQTy;
    }

    public void setPalletLoadQTy(BigDecimal palletLoadQTy) {
        this.palletLoadQTy = palletLoadQTy;
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
    @Column(name = "SKUTYPE")
    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    @Basic
    @Column(name = "LOTNUM")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    private boolean isManual;
    @Basic
    @Column(name = "ISMANUAL")
    public boolean isManual() {
        return isManual;
    }

    public void setManual(boolean manual) {
        isManual = manual;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sku)) return false;

        Sku sku = (Sku) o;

        if (id != sku.id) return false;
        if (packageQty != sku.packageQty) return false;
        if (palletLoadQTy != sku.palletLoadQTy) return false;
        if (version != sku.version) return false;
        if (!skuCode.equals(sku.skuCode)) return false;
        if (!skuName.equals(sku.skuName)) return false;
        if (!skuSpec.equals(sku.skuSpec)) return false;
        if (!custSkuName.equals(sku.custSkuName)) return false;
        if (!skuEom.equals(sku.skuEom)) return false;
        return custName.equals(sku.custName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + skuCode.hashCode();
        result = 31 * result + skuName.hashCode();
        result = 31 * result + skuSpec.hashCode();
        result = 31 * result + custSkuName.hashCode();
        result = 31 * result + skuEom.hashCode();
        result = 31 * result + custName.hashCode();
        result = 31 * result + version;
        return result;
    }

    private Collection<SkuDetail> _skuDetails = new ArrayList<SkuDetail>();

    @OneToMany(mappedBy = "sku")
    public Collection<SkuDetail> getSkuDetails() {
        return _skuDetails;
    }

    public void setSkuDetails(Collection<SkuDetail> skuDetails) {
        _skuDetails = skuDetails;
    }

    public static Sku getByCode(String skuCode) {

        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from Sku where skuCode =:skuCode");
        query.setParameter("skuCode",skuCode);
        query.setMaxResults(1);
        return (Sku) query.uniqueResult();
    }
}
