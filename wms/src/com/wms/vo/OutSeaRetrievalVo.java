package com.wms.vo;

import java.math.BigDecimal;

/**
 * Created by van on 2017/12/14.
 */
public class OutSeaRetrievalVo {
    private String orderNo;
    private String itemCode;
    private BigDecimal qty;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
