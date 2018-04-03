package com.order.vo;

import java.math.BigDecimal;

/**
 * Created by van on 2017/12/15.
 */
public class OrderVo {

    private int id;
    private String whCode;
    private String orderNo;
    private String jobType;
    private BigDecimal boxQty;
    private String coustomName;
    private String carrierName;
    private String toLocation;
    private String area;
    private String desc;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public BigDecimal getBoxQty() {
        return boxQty;
    }

    public void setBoxQty(BigDecimal boxQty) {
        this.boxQty = boxQty;
    }

    public String getCoustomName() {
        return coustomName;
    }

    public void setCoustomName(String coustomName) {
        this.coustomName = coustomName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
