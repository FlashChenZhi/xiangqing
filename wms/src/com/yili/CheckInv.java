package com.yili;

import com.yili.vo.InventoryVo;
import commonservice.CommonService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by van on 2018/1/25.
 */
public class CheckInv {

    public static final String para = "{\"WhID\":\"WH113\"}";

    public static void main(String[] args) {

        CommonService service = new CommonService();
        String result = service.getCommonServiceSoap().queryInventory(para);
        System.out.println(result);

        JSONObject object = JSONObject.fromObject(result);

        JSONArray jsonArray = object.getJSONArray("Inventorys");
        List<InventoryVo> vos = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            InventoryVo vo = new InventoryVo();
            vo.setQty(String.valueOf(jsonObject.get("Qty")));
            vo.setWhID(String.valueOf(jsonObject.get("WhID")));
            vo.setLoc(String.valueOf(jsonObject.get("Loc")));
            vo.setLpnID(String.valueOf(jsonObject.get("LpnID")));
            vo.setSkuID(String.valueOf(jsonObject.get("SkuID")));
            vo.setOwnerID(String.valueOf(jsonObject.get("OwnerID")));

            vos.add(vo);

        }


        System.out.println(vos);
    }

}
