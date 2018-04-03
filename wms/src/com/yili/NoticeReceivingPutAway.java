package com.yili;

import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.yili.vo.PutawayVo;
import com.yili.vo.ReceiptLpnVo;
import commonservice.CommonService;
import net.sf.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.math.BigDecimal;

/**
 * Created by van on 2017/12/29.
 */
public class NoticeReceivingPutAway {

    public static final String param = "{\"WhID\": \"WH113\",\"ReceiptID\": \"KA1-I1610200001\",\"ReceiptLineID\": \"120\",\"LpnID\": \"SN\",\"ToLoc\": \"214500000400\"}";

    public static void main(String[] args) {

        for (int i = 21; i < 23; i++) {
            ReceiptLpnVo vo = GetReceiptLPN.getReceipt("C00" + i);

            try {
                Transaction.begin();

                Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Location.class);
                criteria.add(Restrictions.eq(Location.__EMPTY, true));
                criteria.setMaxResults(1);
                Location location = (Location) criteria.uniqueResult();

                PutawayVo putawayVo = new PutawayVo();
                putawayVo.setLpnID(vo.getLpnID());
                putawayVo.setReceiptLineID(vo.getReceiptLineID());
                putawayVo.setReceiptID(vo.getReceiptID());
                putawayVo.setWhID(Const.WHID);
                putawayVo.setToLoc(location.getLocationNo());

                JSONObject jsonObject = JSONObject.fromObject(putawayVo);

                Container container = new Container();
                container.setLocation(location);
                container.setBarcode(vo.getLpnID());
                location.setEmpty(false);
                container.setLocation(location);
                HibernateUtil.getCurrentSession().save(container);

                Inventory inventory = new Inventory();
                inventory.setSkuCode(vo.getSkuID());
                inventory.setLotNum(vo.getLotAttr05());
                inventory.setStoreDate(vo.getProduceDate());
                inventory.setQty(new BigDecimal(vo.getQty()));
                inventory.setContainer(container);

                HibernateUtil.getCurrentSession().save(inventory);

                CommonService service = new CommonService();
                String result = service.getCommonServiceSoap().receivingAndPutawayLPN(jsonObject.toString());
                JSONObject resultObj = JSONObject.fromObject(result);
                System.out.println(result);
                if ("00".equals(resultObj.get("Status"))) {

                    System.out.println("成功");

                }
                Transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
                Transaction.rollback();
            }

        }

    }

    public static void notice(String palletNo, Location location) {
        ReceiptLpnVo vo = GetReceiptLPN.getReceipt(palletNo);

        try {

            PutawayVo putawayVo = new PutawayVo();
            putawayVo.setLpnID(vo.getLpnID());
            putawayVo.setReceiptLineID(vo.getReceiptLineID());
            putawayVo.setReceiptID(vo.getReceiptID());
            putawayVo.setWhID(Const.WHID);
            putawayVo.setToLoc(location.getWmsLocationNo());

            JSONObject jsonObject = JSONObject.fromObject(putawayVo);

            Container container = new Container();
            container.setLocation(location);
            container.setBarcode(vo.getLpnID());
            location.setEmpty(false);
            HibernateUtil.getCurrentSession().save(container);

            Inventory inventory = new Inventory();
            inventory.setSkuCode(vo.getSkuID());
            inventory.setLotNum(vo.getLotAttr05());
            inventory.setStoreDate(vo.getProduceDate());
            inventory.setQty(new BigDecimal(vo.getQty()));
            inventory.setContainer(container);

            HibernateUtil.getCurrentSession().save(inventory);

            CommonService service = new CommonService();
            String result = service.getCommonServiceSoap().receivingAndPutawayLPN(jsonObject.toString());
            JSONObject resultObj = JSONObject.fromObject(result);
            SystemLog.info("请求参数：【" + jsonObject.toString() + "】，返回：【" + result + "】");
            if ("00".equals(resultObj.get("Status"))) {
                //发送成功
                System.out.println("成功");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
