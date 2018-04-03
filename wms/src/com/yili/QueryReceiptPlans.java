package com.yili;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.ReceivingPlan;
import commonservice.CommonService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by van on 2018/1/25.
 */
public class QueryReceiptPlans {

    //{"ReceiptPlans":[{"WhID":"WH113","SkuID":"214500000400","ProduceDate":"2018-01-25 00:00:00","PlansQty":100},
    // {"WhID":"WH113","SkuID":"214507002300","ProduceDate":"2018-01-25 00:00:00","PlansQty":100},
    // {"WhID":"WH113","SkuID":"214525000500","ProduceDate":"2018-01-25 00:00:00","PlansQty":100}],"Status":"00","ServerMsg":null}


    public static final String parma = "{\n" +
            "   \"WhID\": \"WH113\",\n" +
            "    \"BeginDateTime\": \"\",\n" +
            "    \"EndDateTime\": \"\"\n" +
            "}";

    public static void main(String[] args) {

        try {
            Transaction.begin();

            CommonService service = new CommonService();
            String result = service.getCommonServiceSoap().queryReceiptPlans(parma);
            System.out.println(result);

            JSONObject jsonObject = JSONObject.fromObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("ReceiptPlans");
            if ("00".equals(jsonObject.get("Status"))) {

                for (int i = 0; i < jsonArray.size(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);

                    ReceivingPlan receivingPlan = new ReceivingPlan();
                    receivingPlan.setQty(new BigDecimal(String.valueOf(object.get("PlansQty"))));
                    receivingPlan.setSku(String.valueOf(object.get("SkuID")));
                    receivingPlan.setBatchNo(String.valueOf(object.get("ProduceDate")));

                    HibernateUtil.getCurrentSession().save(receivingPlan);

                }

            }

            Transaction.commit();

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }

    }
}
