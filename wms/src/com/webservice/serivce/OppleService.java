package com.webservice.serivce;

import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.webservice.vo.PushOrderDetail;
import com.webservice.vo.PushOrderResultVo;
import com.webservice.vo.PushOrderVo;
import com.webservice.vo.RetrievalCloseVo;
import com.wms.domain.RetrievalOrder;
import com.wms.domain.RetrievalOrderDetail;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by van on 2017/11/22.
 */
@Service
public class OppleService {

    public static void main(String[] args) {
        Transaction.begin();

        Transaction.commit();
    }

    public List<PushOrderResultVo> pushOrder(List<PushOrderVo> pushOrderVos) {
        List<PushOrderResultVo> httpMessage = new ArrayList<>();
        PushOrderResultVo vo;
        for (PushOrderVo pushOrderVo : pushOrderVos) {
            vo = new PushOrderResultVo();
            vo.setOrderCode(pushOrderVo.getOrderCode());
            vo.setWhCode(pushOrderVo.getWhCode());
            vo.setOrderType(pushOrderVo.getOrderType());
            try {
                Transaction.begin();

                RetrievalOrder retrievalOrder = new RetrievalOrder();
                retrievalOrder.setOrderNo(pushOrderVo.getOrderCode());
                retrievalOrder.setWhCode(pushOrderVo.getWhCode());
                retrievalOrder.setArea(pushOrderVo.getAreaCode());
                retrievalOrder.setBoxQty(pushOrderVo.getCaseQty());
                retrievalOrder.setCarrierName(pushOrderVo.getCarrierName());
                retrievalOrder.setCoustomName(pushOrderVo.getBpName());
                retrievalOrder.setStatus(RetrievalOrder.STATUS_WAIT);
                retrievalOrder.setJobType(pushOrderVo.getOrderType());
                retrievalOrder.setToLocation(pushOrderVo.getLocCode());
                Set<RetrievalOrderDetail> detailSet = new HashSet<>();
                for (PushOrderDetail detail : pushOrderVo.getItems()) {
                    RetrievalOrderDetail orderDetail = new RetrievalOrderDetail();
                    orderDetail.setItemCode(detail.getItemCode());
                    orderDetail.setQty(detail.getQty());
                    orderDetail.setBatch(pushOrderVo.getBatch());
                    orderDetail.setRetrievalOrder(retrievalOrder);
                    detailSet.add(orderDetail);
                }

                retrievalOrder.setRetrievalOrderDetailSet(detailSet);
                HibernateUtil.getCurrentSession().save(retrievalOrder);

                Transaction.commit();
                vo.setSuccess(true);
                vo.setMsg("成功");
            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
                vo.setSuccess(true);
                vo.setMsg("失败");
                LogWriter.error(LoggerType.WMS, e);
            }
            httpMessage.add(vo);
        }
        return httpMessage;
    }


    public HttpMessage cancelOrder(String orderNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();

            RetrievalOrder retrievalOrder = RetrievalOrder.getByOrderNo(orderNo);
            if (retrievalOrder == null) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("订单不存在");
                return httpMessage;
            }

            if (!retrievalOrder.getStatus().equals(RetrievalOrder.STATUS_WAIT)) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("订单不允许取消");
                return httpMessage;
            }

            RetrievalCloseVo closeVo = new RetrievalCloseVo();
            closeVo.setWhCode(retrievalOrder.getWhCode());
            closeVo.setOrderType(retrievalOrder.getJobType());
            closeVo.setOrderCode(retrievalOrder.getOrderNo());
            String param = JSONObject.fromObject(closeVo).toString();
            String result = ContentUtil.getResultJsonType(Const.OPPLE_OUT_CLOSE_WMS_URL, param);
            JSONObject jsonObject = JSONObject.fromObject(result);
            if (!(Boolean) jsonObject.get("success")) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("订单取消失败");
                return httpMessage;
            } else {
                retrievalOrder.setStatus(RetrievalOrder.STATUS_CANCEL);
            }

            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("取消成功");

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("系统异常");
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;
    }
}
