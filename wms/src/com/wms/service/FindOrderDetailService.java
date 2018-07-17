package com.wms.service;

import com.asrs.business.consts.RetrievalOrderStatus;
import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.RetrievalOrder;
import com.wms.domain.RetrievalOrderDetail;
import com.wms.domain.Sku;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: ed_chen
 * @Date: Create in 15:46 2018/7/16
 * @Description:
 * @Modified By:
 */
@Service
public class FindOrderDetailService {

    /*
     * @author：ed_chen
     * @date：2018/7/16 15:51
     * @description：
     * @param startIndex
     * @param defaultPageSize
     * @param productId
     * @param beginDate
     * @param endDate
     * @param type
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    public PagerReturnObj<List<Map<String,Object>>> findOrderDetailData(int startIndex, int defaultPageSize, String orderNo){
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            Query query = session.createQuery("select r.orderNo as orderNo,r.coustomName as coustomName, " +
                    "r.toStation as toStation, r.toLocation as toLocation,r.carrierCar as carrierCar, " +
                    "r.status as status,r.createDate as createDate from RetrievalOrder r "+(StringUtils.isNotBlank(orderNo)?
                    " where r.orderNo=:orderNo ":"")).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query1 = session.createQuery("select count(*) as count from RetrievalOrder "+(StringUtils.isNotBlank(orderNo)?
                    " where orderNo=:orderNo ":""));

            query.setFirstResult(startIndex);
            query.setMaxResults(defaultPageSize);

            if(StringUtils.isNotBlank(orderNo)){
                query.setParameter("orderNo",orderNo );
                query1.setParameter("orderNo",orderNo );
            }

            List<Map<String,Object>> jobList = query.list();
            long count = (long)query1.uniqueResult();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(Map<String,Object> map:jobList){
                map.put("status", RetrievalOrderStatus.map.get(map.get("status")));
                map.put("createDate", sdf.format((Date)map.get("createDate")));
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

    /*
     * @author：ed_chen
     * @date：2018/7/16 17:12
     * @description：查看订单详情
     * @param orderNo
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String,Object>> FindOrderDetail(String orderNo){
        ReturnObj<Map<String,Object>> returnObj = new ReturnObj<>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            orderNo= URLDecoder.decode(orderNo,"utf-8"); //订单号

            Map<String,String> outmap = new HashMap<>();
            outmap.put("1202", "一号出库口");
            outmap.put("1204", "二号出库口");
            outmap.put("1206", "三号出库口");

            Query query = session.createQuery("from RetrievalOrder r where r.orderNo=:orderNo order by r.createDate desc ");
            query.setParameter("orderNo",orderNo );

            RetrievalOrder retrievalOrder = (RetrievalOrder)query.uniqueResult();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String,Object> map=new HashMap<>();
            map.put("status", RetrievalOrderStatus.map.get(retrievalOrder.getStatus()));
            map.put("createDate", sdf.format(retrievalOrder.getCreateDate()));
            map.put("orderNo", retrievalOrder.getOrderNo());
            map.put("toLocation", retrievalOrder.getToLocation());
            map.put("carrierName", retrievalOrder.getCarrierName());
            map.put("carrierCar", retrievalOrder.getCarrierCar());
            map.put("toStation", outmap.get(retrievalOrder.getToStation()));
            map.put("coustomName", retrievalOrder.getCoustomName());
            List<Map<String,Object>> mapList=new ArrayList<>();
            for(RetrievalOrderDetail retrievalOrderDetail:retrievalOrder.getRetrievalOrderDetailSet()){
                Map<String ,Object> map2=new HashMap<>();
                map2.put("skuName", Sku.getByCode(retrievalOrderDetail.getItemCode()).getSkuName());
                map2.put("qty", retrievalOrderDetail.getQty());
                map2.put("batch", retrievalOrderDetail.getBatch());
                mapList.add(map2);
            }
            map.put("data", mapList);
            returnObj.setSuccess(true);
            returnObj.setRes(map);
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
