package com.yili.vo;

/**
 * Created by van on 2018/1/25.
 */
public class ReceiptLpnVo {

   private String ReceiptID;
   private String ReceiptLineID;
   private String OwnerID;
   private String SkuID;
   private String Qty;
   private String LotAttr05;
   private String ProduceDate;
   private String LpnID;
   private String ServerMsg;

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

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public String getSkuID() {
        return SkuID;
    }

    public void setSkuID(String skuID) {
        SkuID = skuID;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getLotAttr05() {
        return LotAttr05;
    }

    public void setLotAttr05(String lotAttr05) {
        LotAttr05 = lotAttr05;
    }

    public String getProduceDate() {
        return ProduceDate;
    }

    public void setProduceDate(String produceDate) {
        ProduceDate = produceDate;
    }

    public String getLpnID() {
        return LpnID;
    }

    public void setLpnID(String lpnID) {
        LpnID = lpnID;
    }

    public String getServerMsg() {
        return ServerMsg;
    }

    public void setServerMsg(String serverMsg) {
        ServerMsg = serverMsg;
    }
}
