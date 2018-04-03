package com.webservice.vo;

import java.math.BigDecimal;

/**
 * Created by van on 2017/11/22.
 */
public class PushOrderDetail {

    private BigDecimal qty;//总只数
    private String itemCode;//商品代码

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

}
