package com.webservice;

import com.util.common.ContentUtil;
import com.util.common.HttpMessage;
import com.util.hibernate.Transaction;
import com.webservice.vo.PushOrderDetail;
import com.webservice.vo.PushOrderVo;
import com.wms.domain.Sku;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by van on 2017/11/22.
 */
public class Demo {

    public static void main(String[] args) throws Exception {
        Transaction.begin();
        Sku sku = Sku.getByCode("123");
        Transaction.commit();
//        PushOrderVo vo = new PushOrderVo();
//
//        vo.setOrderCode("oc1111");
//        vo.setOrderType("0");
//        vo.setWhCode("WJ");
//        vo.setCaseQty(new BigDecimal(20));
//        vo.setBpName("客户名称");
//        vo.setCarrierName("承运商");
//        vo.setLocCode("目的货位");
//        vo.setAreaCode("备货区");
//        vo.setNotice("特殊说明");
//
//        vo.setItems(new ArrayList<PushOrderDetail>());
//
//        PushOrderDetail detail = new PushOrderDetail();
//        detail.setItemCode("ITEM001");
//        detail.setQty(new BigDecimal(10));
//        detail.setBatch("20171101");
//
//        vo.getItems().add(detail);
//
//        detail = new PushOrderDetail();
//        detail.setItemCode("ITEM002");
//        detail.setQty(new BigDecimal(20));
//        detail.setBatch("20171010");
//
//        vo.getItems().add(detail);
//
//        System.out.println(JSONObject.fromObject(vo).toString());
//
//        String result = ContentUtil.getResultJsonType("http://10.10.1.111:8080/wms/opple/order/pushOrder.do", JSONObject.fromObject(vo).toString());
//
//        HttpMessage httpMessage = (HttpMessage) JSONObject.toBean(JSONObject.fromObject(result),HttpMessage.class);
//
//        System.out.println(httpMessage.toString());

    }
}
