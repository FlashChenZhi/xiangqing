package com.wms.service;

import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Sku;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 16:45 2018/7/11
 * @Description:
 * @Modified By:
 */
@Service
public class UpdateSkuLotNumService {

    /*
     * @author：ed_chen
     * @date：2018/7/11 17:00
     * @description：查找商品批次及状态
     * @param skuCode
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> findSkuCodeLotNum(String skuCode){
        ReturnObj<Map<String, Object>> s = new ReturnObj<>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Sku sku=Sku.getByCode(skuCode);
            Map<String, Object> map =new HashMap<>();
            if(sku!=null){
                map.put("isManual", sku.isManual()?"1":"0");
                if (sku.isManual()){
                    map.put("lotNum", sku.getLotNum());
                }else{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String s1=sdf.format(new Date());
                    map.put("lotNum",s1 );
                }
            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("此商品代码不存在！");
                return s;
            }
            s.setSuccess(true);
            s.setRes(map);
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return s;
    }


    /*
     * @author：ed_chen
     * @date：2018/7/11 17:00
     * @description：更新货品批次状态
     * @param skuCode
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<String> updateSkuCodeLotNum(String skuCode,
                                                  String isManual,String lotNum){
        ReturnObj<String> s = new ReturnObj<>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Sku sku=Sku.getByCode(skuCode);
            Map<String, Object> map =new HashMap<>();
            if(sku!=null){
                boolean flag="0".equals(isManual) ? false : true;
                sku.setManual(flag);
                if (flag){
                   sku.setLotNum(lotNum);
                }
            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("此商品代码不存在！");
                return s;
            }
            s.setSuccess(true);
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return s;
    }
}
