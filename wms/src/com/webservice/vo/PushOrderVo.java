package com.webservice.vo;

import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by van on 2017/11/22.
 */
public class PushOrderVo {

    private String orderCode;//单号
    private String whCode;//仓库简码
    private String carrierName;//承运商
    private String bpName;//客户名称
    private String areaCode;//备货区
    private String locCode;//目标货位
    private String orderType;//订单类型
    private String notice;//特殊说明
    private BigDecimal caseQty;//箱数
    private String batch;//批号


    List<PushOrderDetail> items;


    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }


    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public BigDecimal getCaseQty() {
        return caseQty;
    }

    public void setCaseQty(BigDecimal caseQty) {
        this.caseQty = caseQty;
    }

    public List<PushOrderDetail> getItems() {
        return items;
    }

    public void setItems(List<PushOrderDetail> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return JSONObject.fromObject(this).toString();
    }

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }


}
