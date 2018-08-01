package com.wms.service;

import com.asrs.business.consts.RetrievalOrderStatus;
import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 0:05 2018/7/28
 * @Description:
 * @Modified By:
 */
@Service
public class FindERPOrderService {
    /*
     * @author：ed_chen
     * @date：2018/7/28 0:06
     * @description：
     * @param startIndex
     * @param defaultPageSize
     * @param orderNo
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    public PagerReturnObj<List<Map<String,Object>>> FindERPOrderData(int startIndex, int defaultPageSize, String orderNo){
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            orderNo= URLDecoder.decode(orderNo,"utf-8");
            Query query = session.createQuery("select s.skuName as skuName, r.wareId as skuCode," +
                    "r.person as person, r.status as status,r.createTime as createTime,r.wareNum as wareNum from " +
                    "EOutStock r,Sku s where r.wareId=s.skuCode "+(StringUtils.isNotBlank(orderNo)?" and r.person=:person ":"")+"  order by r.createTime desc ").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query1 = session.createQuery("select count(*) as count from EOutStock where 1=1 "+(StringUtils.isNotBlank(orderNo)?" and person=:person ":""));

            query.setFirstResult(startIndex);
            query.setMaxResults(defaultPageSize);
            if(StringUtils.isNotBlank(orderNo)){
                query.setParameter("person", orderNo);
                query1.setParameter("person", orderNo);
            }

            List<Map<String,Object>> jobList = query.list();
            long count = (long)query1.uniqueResult();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(Map<String,Object> map:jobList){
                map.put("createTime", sdf.format((Date)map.get("createTime")));
            }
            returnObj.setSuccess(true);
            returnObj.setRes(jobList);
            returnObj.setCount(count);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;
    }
}
