package com.yili.vo;

import com.util.common.Const;

/**
 * Created by van on 2017/12/29.
 */
public class PutawayVo {
    private String WhID = Const.WHID;
    private String ReceiptID;
    private String ReceiptLineID;
    private String LpnID;
    private String ToLoc;

    public String getWhID() {
        return WhID;
    }

    public void setWhID(String whID) {
        WhID = whID;
    }

    public String getReceiptID() {
        return ReceiptID;
    }

    public void setReceiptID(String receiptID) {
        ReceiptID = receiptID;
    }

    public String getReceiptLineID() {
        return ReceiptLineID;
    }

    public void setReceiptLineID(String receiptLineID) {
        ReceiptLineID = receiptLineID;
    }

    public String getLpnID() {
        return LpnID;
    }

    public void setLpnID(String lpnID) {
        LpnID = lpnID;
    }

    public String getToLoc() {
        return ToLoc;
    }

    public void setToLoc(String toLoc) {
        ToLoc = toLoc;
    }
}
