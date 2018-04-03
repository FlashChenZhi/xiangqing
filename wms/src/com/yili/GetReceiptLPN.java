package com.yili;

import com.wms.domain.SystemLog;
import com.yili.vo.ReceiptLpnVo;
import commonservice.CommonService;
import net.sf.json.JSONObject;

/**
 * Created by van on 2017/12/29.
 */
public class GetReceiptLPN {



    public static void main(String[] args) {
         String param = "{\"WhID\": \"WH113\",\"LpnID\": \"C0001\"}";
        CommonService service = new CommonService();

        String result = service.getCommonServiceSoap().queryReceiptLPN(param);

        JSONObject jsonObject = JSONObject.fromObject(result);

        String receiptID = String.valueOf(jsonObject.get("ReceiptID"));
        String receiptLineID = String.valueOf(jsonObject.get("ReceiptLineID"));
        String ownerID = String.valueOf(jsonObject.get("OwnerID"));
        String skuID = String.valueOf(jsonObject.get("SkuID"));
        String qty = String.valueOf(jsonObject.get("Qty"));
        String lotAttr05 = String.valueOf(jsonObject.get("LotAttr05"));
        String produceDate = String.valueOf(jsonObject.get("ProduceDate"));
        String lpnID = String.valueOf(jsonObject.get("LpnID"));
        String serverMsg = String.valueOf(jsonObject.get("ServerMsg"));

        String Status = String.valueOf(jsonObject.get("Status"));

        if ("00".equals(Status)) {
            //正常
            ReceiptLpnVo vo = new ReceiptLpnVo();
            vo.setReceiptID(receiptID);
            vo.setReceiptLineID(receiptLineID);
            vo.setOwnerID(ownerID);
            vo.setSkuID(skuID);
            vo.setLotAttr05(lotAttr05);
            vo.setQty(qty);
            vo.setProduceDate(produceDate);
            vo.setLpnID(lpnID);
            vo.setServerMsg(serverMsg);

        }

    }

    public static ReceiptLpnVo getReceipt(String lpnId){
        CommonService service = new CommonService();
        String param = "{\"WhID\": \"WH113\",\"LpnID\": \""+lpnId.trim()+"\"}";
        String result = service.getCommonServiceSoap().queryReceiptLPN(param);

        JSONObject jsonObject = JSONObject.fromObject(result);

        String receiptID = String.valueOf(jsonObject.get("ReceiptID"));
        String receiptLineID = String.valueOf(jsonObject.get("ReceiptLineID"));
        String ownerID = String.valueOf(jsonObject.get("OwnerID"));
        String skuID = String.valueOf(jsonObject.get("SkuID"));
        String qty = String.valueOf(jsonObject.get("Qty"));
        String lotAttr05 = String.valueOf(jsonObject.get("LotAttr05"));
        String produceDate = String.valueOf(jsonObject.get("ProduceDate"));
        String lpnID = String.valueOf(jsonObject.get("LpnID"));
        String serverMsg = String.valueOf(jsonObject.get("ServerMsg"));

        String Status = String.valueOf(jsonObject.get("Status"));
        SystemLog.info("请求参数：【" + jsonObject.toString() + "】，返回：【" + result + "】");

        if ("00".equals(Status)) {
            //正常
            ReceiptLpnVo vo = new ReceiptLpnVo();
            vo.setReceiptID(receiptID);
            vo.setReceiptLineID(receiptLineID);
            vo.setOwnerID(ownerID);
            vo.setSkuID(skuID);
            vo.setLotAttr05(lotAttr05);
            vo.setQty(qty);
            vo.setProduceDate(produceDate);
            vo.setLpnID(lpnID);
            vo.setServerMsg(serverMsg);
            return vo;

        }

        return null;

    }
}
