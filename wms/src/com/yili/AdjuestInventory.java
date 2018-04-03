package com.yili;

import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Inventory;
import commonservice.CommonService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Query;

import java.math.BigDecimal;

/**
 * Created by van on 2018/1/25.
 */
public class AdjuestInventory {
    public static final String param = "{\"WhID\": \"WH113\"}";

    public static void main(String[] args) {
        while (true) {
            try {
                CommonService commonService = new CommonService();
                String result = commonService.getCommonServiceSoap().queryInvAdjustment(param);
                System.out.println(result);

                JSONObject object = JSONObject.fromObject(result);
                JSONArray jsonArray = object.getJSONArray("Adjustments");
                for (int i = 0; i < jsonArray.size(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    String lpnId = String.valueOf(obj.get("LpnID"));
                    BigDecimal qty = new BigDecimal(String.valueOf(obj.get("Qty")));

                    Transaction.begin();
                    Query query = HibernateUtil.getCurrentSession().createQuery("from Inventory  where container.barcode=:barcode");
                    query.setParameter("barcode", lpnId);
                    Inventory inventory = (Inventory) query.uniqueResult();
                    if (inventory != null) {
                        inventory.setQty(qty);
                    }

                    Transaction.commit();

                }
            } catch (Exception e) {
                e.printStackTrace();
                Transaction.rollback();
            } finally {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
