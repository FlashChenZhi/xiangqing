package com.wms.service;

import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Msg03;
import com.wms.vo.Msg03Vo;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangfan
 * Created on 2017/3/11.
 */
@Service
public class MessageQueryService {


    public HttpMessage searchMessage(int currentPage, String mcKey) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();
            Criteria cri = session.createCriteria(Msg03.class);
            if (StringUtils.isNotEmpty(mcKey)) {
                cri.add(Restrictions.eq("mcKey", mcKey));
            }
            cri.addOrder(Order.desc("id"));
            Long count = (Long) cri.setProjection(Projections.rowCount()).uniqueResult();
            cri.setProjection(null);
            cri.setFirstResult((currentPage - 1) * 10);
            cri.setMaxResults(10);

            List<Msg03> msg03s = cri.list();

            List<Msg03Vo> list = new ArrayList<Msg03Vo>();
            Msg03Vo msg03Vo = null;
            for (Msg03 msg : msg03s) {
                msg03Vo = new Msg03Vo();
                msg03Vo.setId(msg.getId());
                msg03Vo.setCycleOrder(msg.getCycleOrder());
                msg03Vo.setSendDate(DateFormat.format(msg.getCreateDate(), DateFormat.YYYYMMDDHHMMSS));
                msg03Vo.setReceived(msg.isReceived());
                msg03Vo.setMachineNo(msg.getMachineNo());
                msg03Vo.setStation(msg.getStation());
                msg03Vo.setMcKey(msg.getMcKey());
                msg03Vo.setDock(msg.getDock());
                msg03Vo.setBank(msg.getBank());
                msg03Vo.setBay(msg.getBay());
                msg03Vo.setLevel(msg.getLevel());
                msg03Vo.setType(msg.getMsgType());
                list.add(msg03Vo);
            }

            map.clear();
            map.put("total", count);
            map.put("data", list);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }


    public HttpMessage sendMsg(int id) {
        LogWriter.info(LoggerType.WMS, "重新发送03消息ID：" + id );

        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from Msg03 where id=:msgId");
            query.setParameter("msgId", id);
            query.setMaxResults(1);
            Msg03 msg03 = (Msg03) query.uniqueResult();
            msg03.setSendStatus("1");
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("设定成功");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }
}
