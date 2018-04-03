package com.inventory.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongying on 15/11/9.
 */
public class AutoLocationListVo {
    private String locationNo;
    private String dpsAlias;
    private int bank;
    private String status;
    private String remark;
    private String bindSkuCode;
    private String realLocation;
    private List<AutoLocationListDetailVo> detailVos = new ArrayList<AutoLocationListDetailVo>(1);

    public static final String EMPTY = "0";
    public static final String NORMAL = "1";
    public static final String WARN = "2";
    public static final String RESTRICTED = "3";
    public static final String ABNORMAL = "4";
    public static final String EMPTY_PALLET = "5";
    public static final String TRANSFER_RESERVED = "6";
    public static final String SYSTEM_UN_AVAIL = "7";

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<AutoLocationListDetailVo> getDetailVos() {
        return detailVos;
    }

    public void setDetailVos(List<AutoLocationListDetailVo> detailVos) {
        this.detailVos = detailVos;
    }

    public String getBindSkuCode() {
        return bindSkuCode;
    }

    public void setBindSkuCode(String bindSkuCode) {
        this.bindSkuCode = bindSkuCode;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public String getDpsAlias() {
        return dpsAlias;
    }

    public void setDpsAlias(String dpsAlias) {
        this.dpsAlias = dpsAlias;
    }

    public String getRealLocation() {
        return realLocation;
    }

    public void setRealLocation(String realLocation) {
        this.realLocation = realLocation;
    }
}
