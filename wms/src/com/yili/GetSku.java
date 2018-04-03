package com.yili;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Sku;
import commonservice.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by van on 2017/12/29.
 */
public class GetSku {

    public static String param = "{\"WhID\": \"WH113\",\"BeginDateTime\": \"\",\"EndDateTime\": \"\"}";

    public static void main(String[] args) {
        //根据工厂创建一个MobileCodeWSSoap对象

        CommonService service = new CommonService();
        String result = service.getCommonServiceSoap().querySku(param);
        System.out.println(result);


        JSONObject jsonObject = JSONObject.fromObject(result);


        String Status = String.valueOf(jsonObject.get("Status"));

        if ("00".equals(Status)) {
            //正常
            JSONArray jsonArray = jsonObject.getJSONArray("Skus");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String skuId = String.valueOf(object.get("SkuID"));
                String ownerId = String.valueOf(object.get("OwnerID"));
                String descr = String.valueOf(object.get("Descr"));
                String descr2 = String.valueOf(object.get("Descr2"));

                try {
                    Transaction.begin();
                    Sku sku = Sku.getByCode(skuId);
                    if (sku == null) {
                        sku = new Sku();
                        sku.setSkuCode(skuId);
                    }

                    sku.setCustName(ownerId);
                    sku.setSkuName(descr);
                    sku.setSkuSpec(descr2);
                    HibernateUtil.getCurrentSession().saveOrUpdate(sku);
                    Transaction.commit();
                } catch (Exception e) {
                    Transaction.rollback();
                    e.printStackTrace();
                }

            }

        }

    }
}
