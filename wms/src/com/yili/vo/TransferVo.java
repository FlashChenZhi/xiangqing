package com.yili.vo;

/**
 * Created by van on 2018/1/25.
 */
public class TransferVo {

    private String WhID;
    private String ExternalOrderID;
    private String FromLoc;
    private String FromLpnID;
    private String SkuID;
    private String ToLoc;
    private String ToLpnID;
    private String OwnerID;
    private String Qty;

    public String getWhID() {
        return WhID;
    }

    public void setWhID(String whID) {
        WhID = whID;
    }

    public String getExternalOrderID() {
        return ExternalOrderID;
    }

    public void setExternalOrderID(String externalOrderID) {
        ExternalOrderID = externalOrderID;
    }

    public String getFromLoc() {
        return FromLoc;
    }

    public void setFromLoc(String fromLoc) {
        FromLoc = fromLoc;
    }

    public String getFromLpnID() {
        return FromLpnID;
    }

    public void setFromLpnID(String fromLpnID) {
        FromLpnID = fromLpnID;
    }

    public String getSkuID() {
        return SkuID;
    }

    public void setSkuID(String skuID) {
        SkuID = skuID;
    }

    public String getToLoc() {
        return ToLoc;
    }

    public void setToLoc(String toLoc) {
        ToLoc = toLoc;
    }

    public String getToLpnID() {
        return ToLpnID;
    }

    public void setToLpnID(String toLpnID) {
        ToLpnID = toLpnID;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }
}
