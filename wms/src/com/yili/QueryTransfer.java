package com.yili;

import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Inventory;
import commonservice.CommonService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Query;

/**
 * Created by van on 2018/1/25.
 */
public class QueryTransfer {

    public static String param = "{\"WhID\": \"WH113\",\"BeginDateTime\": \"\",\"EndDateTime\": \"\"}";

    public static void main(String[] args) {

        while (true) {
            try{
                CommonService service = new CommonService();
                String result = service.getCommonServiceSoap().queryInvTransfer(param);

                System.out.println(result);

                JSONObject jsonObject = JSONObject.fromObject(result);

                JSONArray array = jsonObject.getJSONArray("Transfers");

                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    String toLoc = String.valueOf(object.get("ToLoc"));
                    String toLpnId = String.valueOf(object.get("ToLpnID"));
                    String toSkuId = String.valueOf(object.get("ToSkuID"));
                    String lotAttr = String.valueOf(object.get("LotAttr05"));
                    String produceDate = String.valueOf(object.get("ProduceDate"));

                    Transaction.begin();
                    Query query = HibernateUtil.getCurrentSession().createQuery("from Inventory  where container.barcode=:barcode");
                    query.setParameter("barcode", toLpnId);
                    Inventory inventory = (Inventory) query.uniqueResult();
                    if (inventory != null) {
                        if (StringUtils.isNotEmpty(lotAttr)) {
                            inventory.setLotNum(lotAttr);
                        }
                        if (StringUtils.isNotEmpty(produceDate)) {
                            inventory.setStoreDate(produceDate);
                        }
                    }
                    Transaction.commit();

                }
            } catch(Exception e){
                e.printStackTrace();
                Transaction.rollback();
            } finally{
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
