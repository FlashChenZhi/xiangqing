package com.asrs.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Qty {
    private int id;
    private String skuCode;
    private String skuSpec;
    private BigDecimal qty;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "skuCode")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "skuSpec")
    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    @Basic
    @Column(name = "qty")
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qty qty1 = (Qty) o;
        return id == qty1.id &&
                Objects.equals(skuCode, qty1.skuCode) &&
                Objects.equals(skuSpec, qty1.skuSpec) &&
                Objects.equals(qty, qty1.qty);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, skuCode, skuSpec, qty);
    }
}
