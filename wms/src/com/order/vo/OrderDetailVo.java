package com.order.vo;

import java.math.BigDecimal;

/**
 * Created by van on 2018/1/14.
 */
public class OrderDetailVo {

    private int id;
    private String itemCode;
    private String batch;
    private String palletNo;
    private BigDecimal qty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getPalletNo() {
        return palletNo;
    }

    public void setPalletNo(String palletNo) {
        this.palletNo = palletNo;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
