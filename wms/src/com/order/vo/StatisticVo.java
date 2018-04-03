package com.order.vo;

import java.math.BigDecimal;

/**
 * Created by van on 2018/3/7.
 */
public class StatisticVo {
    private String type;
    private BigDecimal qty;
    private BigDecimal volumn;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getVolumn() {
        return volumn;
    }

    public void setVolumn(BigDecimal volumn) {
        this.volumn = volumn;
    }

}
