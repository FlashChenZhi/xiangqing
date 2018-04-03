package com.yili.vo;

/**
 * Created by van on 2018/1/25.
 */
public class InventoryVo {

    private String WhID;
    private String Loc;
    private String LpnID;
    private String SkuID;
    private String OwnerID;
    private String Qty;

    public String getWhID() {
        return WhID;
    }

    public void setWhID(String whID) {
        WhID = whID;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public String getLpnID() {
        return LpnID;
    }

    public void setLpnID(String lpnID) {
        LpnID = lpnID;
    }

    public String getSkuID() {
        return SkuID;
    }

    public void setSkuID(String skuID) {
        SkuID = skuID;
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
