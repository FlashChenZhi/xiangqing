package com.wms.vo;

/**
 * Created by admin on 2017/3/1.
 */
public class PerformanceVo {
    private String skuCode;
    private String locationNo;
    private String palletBarcode;
    private String lotNumBeginDate;
    private String lotNumEndDate;
    private int currentPage;


    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }


    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getPalletBarcode() {
        return palletBarcode;
    }

    public void setPalletBarcode(String palletBarcode) {
        this.palletBarcode = palletBarcode;
    }

    public String getLotNumBeginDate() {
        return lotNumBeginDate;
    }

    public void setLotNumBeginDate(String lotNumBeginDate) {
        this.lotNumBeginDate = lotNumBeginDate;
    }

    public String getLotNumEndDate() {
        return lotNumEndDate;
    }

    public void setLotNumEndDate(String lotNumEndDate) {
        this.lotNumEndDate = lotNumEndDate;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
