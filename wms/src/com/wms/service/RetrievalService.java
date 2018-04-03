package com.wms.service;

import com.util.common.HttpMessage;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.OutSeaBatch;
import com.wms.domain.RetrievalOrder;
import com.wms.domain.RetrievalOrderDetail;
import com.wms.vo.AddBatchVo;
import com.wms.vo.OutSeaRetrievalVo;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by van on 2017/12/14.
 */
@Service
public class RetrievalService {

    public HttpMessage searchOutRetrieval(String orderNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            List<OutSeaRetrievalVo> vos = new ArrayList<>();

            Transaction.begin();

            Query query = HibernateUtil.getCurrentSession().createQuery(" from RetrievalOrder where orderNo=:orderNo ");
            query.setParameter("orderNo", orderNo);
            RetrievalOrder retrievalOrder = (RetrievalOrder) query.uniqueResult();
            if (retrievalOrder != null) {
                Set<RetrievalOrderDetail> details = retrievalOrder.getRetrievalOrderDetailSet();
                Iterator<RetrievalOrderDetail> iterator = details.iterator();
                OutSeaRetrievalVo retrievalVo;
                while (iterator.hasNext()) {
                    retrievalVo = new OutSeaRetrievalVo();
                    RetrievalOrderDetail detail = iterator.next();
                    retrievalVo.setOrderNo(orderNo);
                    retrievalVo.setItemCode(detail.getItemCode());
                    retrievalVo.setQty(detail.getQty());
                    vos.add(retrievalVo);
                }
            }

            Transaction.commit();
            httpMessage.setSuccess(true);
            Map<String, Object> map = new HashMap<>();
            map.put("data", vos);
            httpMessage.setMsg(map);

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("系统异常");
        }

        return httpMessage;
    }

    public HttpMessage addBatch(AddBatchVo addBatchVo) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Query query = HibernateUtil.getCurrentSession().createQuery("from RetrievalOrderDetail where retrievalOrder.orderNo=:orderNo and itemCode=:itemCode");
            query.setParameter("orderNo", addBatchVo.getOrderNo());
            query.setParameter("itemCode", addBatchVo.getItemCode());
            query.setMaxResults(1);
            RetrievalOrderDetail detail = (RetrievalOrderDetail) query.uniqueResult();

            if (detail == null) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("订单明细不存在");
                return httpMessage;
            }

            if (!detail.getRetrievalOrder().getStatus().equals(RetrievalOrder.STATUS_WAIT)) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("订单状态不允许修改出库批次");
                return httpMessage;
            }

            Query batchQuery = HibernateUtil.getCurrentSession().createQuery("from OutSeaBatch where retrievalOrderDetail.id=:did and batchNo=:batchNo");
            batchQuery.setParameter("did", detail.getId());
            batchQuery.setParameter("batchNo", addBatchVo.getBatchNo());
            batchQuery.setMaxResults(1);
            OutSeaBatch batch = (OutSeaBatch) batchQuery.uniqueResult();
            if (batch != null) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("该批次已绑定");
                return httpMessage;
            }

            batch = new OutSeaBatch();
            batch.setRetrievalOrderDetail(detail);
            batch.setBatchNo(addBatchVo.getBatchNo());
            HibernateUtil.getCurrentSession().save(batch);

            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("添加成功");

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("系统异常");
        }

        return httpMessage;
    }

    public HttpMessage deleteBatch(AddBatchVo addBatchVo) {
        HttpMessage httpMessage = new HttpMessage();

        try {

            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from RetrievalOrderDetail where retrievalOrder.orderNo=:orderNo and itemCode=:itemCode");
            query.setParameter("orderNo", addBatchVo.getOrderNo());
            query.setParameter("itemCode", addBatchVo.getItemCode());
            query.setMaxResults(1);
            RetrievalOrderDetail detail = (RetrievalOrderDetail) query.uniqueResult();

            if (detail == null) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("订单明细不存在");
                return httpMessage;
            }

            if (!detail.getRetrievalOrder().getStatus().equals(RetrievalOrder.STATUS_WAIT)) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("订单状态不允许修改出库批次");
                return httpMessage;

            }

            Query batchQuery = HibernateUtil.getCurrentSession().createQuery("from OutSeaBatch where retrievalOrderDetail.id=:did and batchNo=:batchNo");
            batchQuery.setParameter("did", detail.getId());
            batchQuery.setParameter("batchNo", addBatchVo.getBatchNo());
            batchQuery.setMaxResults(1);
            OutSeaBatch batch = (OutSeaBatch) batchQuery.uniqueResult();
            if (batch == null) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("批次绑定不存在");
                return httpMessage;
            }
            HibernateUtil.getCurrentSession().delete(batch);

            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("删除");

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("系统异常");
        }

        return httpMessage;
    }


    public HttpMessage searchOrderBatch(String orderNo, String itemCode) {
        HttpMessage httpMessage = new HttpMessage();

        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from OutSeaBatch where retrievalOrderDetail.retrievalOrder.orderNo=:orderNo and retrievalOrderDetail.itemCode=:itemCode");
            query.setParameter("orderNo",orderNo);
            query.setParameter("itemCode",itemCode);
            List<OutSeaBatch> batches = (List<OutSeaBatch>) query.list();

            List<AddBatchVo> addBatchVos = new ArrayList<>();
            AddBatchVo vo;
            for(OutSeaBatch outSeaBatch : batches){
                vo = new AddBatchVo();
                vo.setOrderNo(outSeaBatch.getRetrievalOrderDetail().getRetrievalOrder().getOrderNo());
                vo.setItemCode(outSeaBatch.getRetrievalOrderDetail().getItemCode());
                vo.setBatchNo(outSeaBatch.getBatchNo());
                addBatchVos.add(vo);
            }

            Transaction.commit();
            httpMessage.setSuccess(true);
            Map<String, Object> map = new HashMap<>();
            map.put("data", addBatchVos);
            httpMessage.setMsg(map);

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("系统异常");

        }

        return httpMessage;
    }


}
