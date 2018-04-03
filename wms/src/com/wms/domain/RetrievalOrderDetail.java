package com.wms.domain;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by van on 2017/11/22.
 */
@Entity
@Table(name = "RETRIEVAL_ORDER_DETAIL")
public class RetrievalOrderDetail {
    private int id;
    private BigDecimal qty;//总只数
    private String itemCode;//商品代码
    private String batch;//批次
    private String palletNo;
    private int version;

    private RetrievalOrder retrievalOrder;

    public static final String COL_RETRIEVALORDER = "retrievalOrder";
    public static final String COL_ITEMCODE = "itemCode";

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
    @Column(name = "ITEM_CODE")
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Basic
    @Column(name = "QTY")
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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
    @Column(name = "BATCH")
    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "id")
    public RetrievalOrder getRetrievalOrder() {
        return retrievalOrder;
    }

    public void setRetrievalOrder(RetrievalOrder retrievalOrder) {
        this.retrievalOrder = retrievalOrder;
    }

    @Basic
    @Column(name = "PALLET_NO")
    public String getPalletNo() {
        return palletNo;
    }

    public void setPalletNo(String palletNo) {
        this.palletNo = palletNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RetrievalOrderDetail that = (RetrievalOrderDetail) o;

        if (id != that.id) return false;
        if (version != that.version) return false;
        if (itemCode != null ? !itemCode.equals(that.itemCode) : that.itemCode != null) return false;
        return qty != null ? qty.equals(that.qty) : that.qty == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (itemCode != null ? itemCode.hashCode() : 0);
        result = 31 * result + (qty != null ? qty.hashCode() : 0);
        result = 31 * result + version;
        return result;
    }
}
