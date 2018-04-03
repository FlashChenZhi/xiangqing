package com.yili;

import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Inventory;
import com.wms.domain.Location;
import com.wms.domain.SystemLog;
import com.yili.vo.TransferVo;
import commonservice.CommonService;
import net.sf.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Created by van on 2018/1/25.
 */
public class Transfer {
    public static void main(String[] args) {
        try {

            Transaction.begin();


            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Location.class);
            criteria.add(Restrictions.eq(Location.__EMPTY, true));
            criteria.setMaxResults(1);
            Location location = (Location) criteria.uniqueResult();


            Criteria invCri = HibernateUtil.getCurrentSession().createCriteria(Inventory.class);
            invCri.add(Restrictions.eq(Inventory.__ID, 22));
            invCri.setMaxResults(1);
            Inventory inventory = (Inventory) invCri.uniqueResult();

            TransferVo vo = new TransferVo();
            vo.setOwnerID("SN");
            vo.setExternalOrderID("0001");
            vo.setWhID(Const.WHID);
            vo.setFromLoc(inventory.getContainer().getLocation().getLocationNo());
            vo.setToLoc(location.getLocationNo());
            vo.setFromLpnID(inventory.getContainer().getBarcode());
            vo.setToLpnID(inventory.getContainer().getBarcode());
            vo.setSkuID(inventory.getSkuCode());
            vo.setQty(inventory.getQty().toString());

            inventory.getContainer().setLocation(location);
            location.setEmpty(false);


            System.out.println(JSONObject.fromObject(vo).toString());
            CommonService service = new CommonService();
            String result = service.getCommonServiceSoap().moveInvLocation(JSONObject.fromObject(vo).toString());
            System.out.println(result);

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }

    public static void transfer(Location location,Inventory inventory){
        try {


            TransferVo vo = new TransferVo();
            vo.setOwnerID("SN");
            vo.setExternalOrderID("0001");
            vo.setWhID(Const.WHID);
            vo.setFromLoc(inventory.getContainer().getLocation().getWmsLocationNo());
            vo.setToLoc(location.getWmsLocationNo());
            vo.setFromLpnID(inventory.getContainer().getBarcode());
            vo.setToLpnID(inventory.getContainer().getBarcode());
            vo.setSkuID(inventory.getSkuCode());
            vo.setQty(inventory.getQty().toString());

            inventory.getContainer().setLocation(location);
            location.setEmpty(false);

            Transaction.commit();

            System.out.println(JSONObject.fromObject(vo).toString());
            CommonService service = new CommonService();
            String result = service.getCommonServiceSoap().moveInvLocation(JSONObject.fromObject(vo).toString());

            JSONObject resultObj = JSONObject.fromObject(result);
            System.out.println(result);
            if ("00".equals(resultObj.get("Status"))) {
                //发送成功
                System.out.println("成功");

            }
            SystemLog.info("请求参数:[" + JSONObject.fromObject(vo).toString() + "],返回数据：【" + result + "】");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
