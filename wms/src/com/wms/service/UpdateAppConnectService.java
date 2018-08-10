package com.wms.service;

import com.util.common.ReturnObj;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Sku;
import com.wms.domain.erp.WEConnect;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 11:04 2018/8/10
 * @Description:
 * @Modified By:
 */
@Service
public class UpdateAppConnectService {
    /*
     * @author：ed_chen
     * @date：2018/8/10 11:08
     * @description：获取app连接状态
     * @param skuCode
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> getAppConnect(){
        ReturnObj<Map<String, Object>> s = new ReturnObj<>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            WEConnect weConnect =WEConnect.getById(1);
            Map<String, Object> map =new HashMap<>();
            if(weConnect!=null){
                map.put("isConnect", weConnect.isConnect()?"1":"0");
            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("此连接不存在！");
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
     * @date：2018/8/10 11:08
     * @description：更新app连接状态
     * @param skuCode
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> updateAppConnect(String isConnect){
        ReturnObj<Map<String, Object>> s = new ReturnObj<>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            WEConnect weConnect =WEConnect.getById(1);
            if(StringUtils.isNotEmpty(isConnect)){
                if(weConnect!=null){
                    Query query = session.createQuery("select count(*) as count from Job");
                    long count =(long) query.uniqueResult();
                    if(count==0){
                        weConnect.setConnect("1".equals(isConnect)?true:false);
                    }else{
                        Transaction.rollback();
                        s.setSuccess(false);
                        s.setMsg("存在未完成任务，不能更改连接状态！");
                        return s;
                    }
                }else{
                    Transaction.rollback();
                    s.setSuccess(false);
                    s.setMsg("此连接不存在！");
                    return s;
                }
            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("状态值为空！");
                return s;
            }
            Transaction.commit();
            s.setSuccess(true);
            s.setMsg("修改成功！");
        }catch (Exception e){
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return s;
    }
}
