package com.wms.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.WorkStartEndDA;
import com.asrs.domain.XMLbean.XMLList.WorkStartEnd;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.asrs.xml.util.XMLUtil;
import com.util.common.HttpMessage;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.vo.InventoryVo;
import com.wms.vo.OnlineTaskVo;
import com.wms.vo.PerformanceVo;
import com.wms.vo.SkuVo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by van on 2017/12/15.
 */
@Service
public class QueryService {

    public HttpMessage onlineTask(int currentPage) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Criteria criteria = session.createCriteria(Job.class).addOrder(Order.asc(Job.__ID));
            //获取总行数
            Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult((currentPage - 1) * 10);
            criteria.setMaxResults(10);
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
            List<Job> jobs = criteria.list();

            List<OnlineTaskVo> onlineTaskVos = new ArrayList<>();
            OnlineTaskVo onlineTaskVo;
            for (Job job : jobs) {
                onlineTaskVo = new OnlineTaskVo();
               // onlineTaskVo.setBarcode(job.getContainer().getBarcode());
                onlineTaskVo.setJobStatus(AsrsJobStatus.map.get(job.getStatus()));
                onlineTaskVo.setJobType(AsrsJobType.map.get(job.getType()));
                onlineTaskVo.setMcKey(job.getMcKey());
                if (job.getFromStation() != null)
                    onlineTaskVo.setFromStation(job.getFromStation());
                if (job.getToStation() != null)
                    onlineTaskVo.setToStation(job.getToStation());
                if (job.getFromLocation() != null)
                    onlineTaskVo.setFromLocation(job.getFromLocation().getLocationNo());
                if (job.getToLocation() != null)
                    onlineTaskVo.setToLocation(job.getToLocation().getLocationNo());
                onlineTaskVos.add(onlineTaskVo);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("data", onlineTaskVos);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("系统异常");
            e.printStackTrace();
        }
        return httpMessage;

    }


    public HttpMessage asrsJobQuery(int currentPage) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Criteria criteria = session.createCriteria(AsrsJob.class).addOrder(Order.asc(Job.__ID));
            //获取总行数
            Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult((currentPage - 1) * 10);
            criteria.setMaxResults(10);
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
            List<AsrsJob> jobs = criteria.list();

            List<OnlineTaskVo> onlineTaskVos = new ArrayList<>();
            OnlineTaskVo onlineTaskVo;
            for (AsrsJob job : jobs) {
                onlineTaskVo = new OnlineTaskVo();
                onlineTaskVo.setBarcode(job.getBarcode());
                onlineTaskVo.setJobStatus(AsrsJobStatus.map.get(job.getStatus()));
                onlineTaskVo.setJobType(AsrsJobType.map.get(job.getType()));
                onlineTaskVo.setMcKey(job.getMcKey());
                onlineTaskVo.setFromStation(job.getFromStation());
                onlineTaskVo.setToStation(job.getToStation());
                onlineTaskVo.setFromLocation(job.getFromLocation());
                onlineTaskVo.setToLocation(job.getToLocation());
                onlineTaskVos.add(onlineTaskVo);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("data", onlineTaskVos);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了");
            e.printStackTrace();
        }
        return httpMessage;

    }


    public HttpMessage startTask() {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Sender sender = new Sender();
            sender.setDivision(XMLConstant.COM_DIVISION);
            sender.setConfirmation(XMLConstant.COM_CONFIRMATION);

            RefId refId = new RefId();
            Transaction.begin();
            refId.setReferenceId(Mckey.getNext());
            Transaction.commit();
            ControlArea controlArea = new ControlArea();
            controlArea.setSender(sender);
            controlArea.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            WorkStartEndDA workStartEndDA = new WorkStartEndDA();
            workStartEndDA.setRequestId("");//TODO
            workStartEndDA.setType("start");//TODO

            WorkStartEnd workStartEnd = new WorkStartEnd();
            workStartEnd.setControlArea(controlArea);
            workStartEnd.setDataArea(workStartEndDA);
            workStartEnd.setDate(new Date());

            Envelope envelope = new Envelope();
            envelope.setWorkStartEnd(workStartEnd);

            XMLUtil.sendEnvelope(envelope);
            httpMessage.setSuccess(true);
            httpMessage.setMsg("作业已开始");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("作业出现异常");
            e.printStackTrace();
        }
        return httpMessage;

    }

    public HttpMessage endTask() {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Sender sender = new Sender();
            sender.setDivision(XMLConstant.COM_DIVISION);
            sender.setConfirmation(XMLConstant.COM_CONFIRMATION);

            RefId refId = new RefId();
            Transaction.begin();
            refId.setReferenceId(Mckey.getNext());
            Transaction.commit();
            ControlArea controlArea = new ControlArea();
            controlArea.setSender(sender);
            controlArea.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            WorkStartEndDA workStartEndDA = new WorkStartEndDA();
            workStartEndDA.setRequestId("");//TODO
            workStartEndDA.setType("end");//TODO

            WorkStartEnd workStartEnd = new WorkStartEnd();
            workStartEnd.setControlArea(controlArea);
            workStartEnd.setDataArea(workStartEndDA);
            workStartEnd.setDate(new Date());

            Envelope envelope = new Envelope();
            envelope.setWorkStartEnd(workStartEnd);

            XMLUtil.sendEnvelope(envelope);
            httpMessage.setSuccess(true);
            httpMessage.setMsg("作业已结束");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("作业出现异常");
            e.printStackTrace();
        }
        return httpMessage;

    }

    public HttpMessage inventoryQuery(PerformanceVo performanceVo) {

        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Map<String, Object> map = new HashMap<>();
            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Inventory.class);
            if (StringUtils.isNotBlank(performanceVo.getSkuCode())) {
                criteria.add(Restrictions.eq(Inventory.COL_SKUCODE, performanceVo.getSkuCode()));
            }
            if (StringUtils.isNotBlank(performanceVo.getLotNumBeginDate())) {
                criteria.add(Restrictions.ge(Inventory.COL_LOTNUM, performanceVo.getLotNumBeginDate()));
            }
            if (StringUtils.isNotBlank(performanceVo.getLotNumEndDate())) {
                criteria.add(Restrictions.le(Inventory.COL_LOTNUM, performanceVo.getLotNumEndDate()));
            }
            Criteria conCri = null;
            if (StringUtils.isNotBlank(performanceVo.getPalletBarcode())) {
                conCri = criteria.createCriteria(Inventory.COL_CONTAINER);
                conCri.add(Restrictions.eq(Container.__BARCODE, performanceVo.getPalletBarcode()));
            }
            if (StringUtils.isNotBlank(performanceVo.getLocationNo())) {
                if (conCri == null) {
                    conCri = criteria.createCriteria(Inventory.COL_CONTAINER);
                }
                Criteria loCri = conCri.createCriteria(Container.__LOCATION);
                loCri.add(Restrictions.eq(Location.__LOCATIONNO, performanceVo.getLocationNo()));
            }

            //获取总行数
            Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult((performanceVo.getCurrentPage() - 1) * 10);
            criteria.setMaxResults(10);
            List<Inventory> inventories = criteria.list();

            List<InventoryVo> inventoryVos = new ArrayList<>();
            InventoryVo inventoryVo = null;
            for (Inventory inv : inventories) {
                inventoryVo = new InventoryVo();
                inventoryVo.setId(inv.getId());
                inventoryVo.setQty(inv.getQty());
                inventoryVo.setLotNum(inv.getLotNum());
                inventoryVo.setOrderNo(inv.getOrderNo());
                inventoryVo.setLocationNo(inv.getContainer().getLocation().getLocationNo());
                inventoryVo.setPalletBarcode(inv.getContainer().getBarcode());
                inventoryVo.setSkuCode(inv.getSkuCode());
                inventoryVo.setCaseBarCode(inv.getCaseBarCode());

                inventoryVos.add(inventoryVo);
            }

            map.clear();
            map.put("total", total);
            map.put("data", inventoryVos);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了");
            e.printStackTrace();
        }
        return httpMessage;

    }

    public HttpMessage outInventory(PerformanceVo performanceVo) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();
            StringBuffer hql = new StringBuffer("select new com.wms.vo.InventoryVo(sr.id,sr.sku.skuCode,sr.sku.custName,sr.sku.skuSpec,sr.sku.custSkuName," +
                    "sr.sku.skuName,sr.providerName,sr.orderNo,sr.batchNo,sr.lotNum,sr.qty,sr.sku.skuEom,sr.storeDate,sr.storeTime,sr.container.location.locationNo," +
                    "sr.container.barcode) from Inventory sr where sr.container.location.asrsFlag=true ");
            if (StringUtils.isNotBlank(performanceVo.getSkuCode())) {
                hql.append(" and sr.sku.skuCode like :skuCode");
                map.put("skuCode", performanceVo.getSkuCode());
            }
            if (StringUtils.isNotBlank(performanceVo.getLocationNo())) {
                hql.append(" and sr.container.location.locationNo like :locationNo");
                map.put("locationNo", performanceVo.getLocationNo());
            }
            if (StringUtils.isNotBlank(performanceVo.getPalletBarcode())) {
                hql.append(" and sr.palletBarcode like :palletBarcode");
                map.put("palletBarcode", performanceVo.getPalletBarcode());
            }

            if (StringUtils.isNotBlank(performanceVo.getLotNumBeginDate())) {
                hql.append(" and sr.lotNum >= :lotNumBeginDate and sr.lotNum<=:lotNumEndDate");
                map.put("lotNumBeginDate", performanceVo.getLotNumBeginDate());
                map.put("lotNumEndDate", performanceVo.getLotNumEndDate());
            }
            hql.append(" order by sr.id asc");
            Query query = session.createQuery(hql.toString());
            Query countQuery = session.createQuery("select count(*) " + hql.substring(hql.indexOf("from Inventory"), hql.length()));
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }
            List<InventoryVo> inventoryVos = query.setFirstResult((performanceVo.getCurrentPage() - 1) * 10).setMaxResults(10).list();
            long total = (long) countQuery.uniqueResult();
            map.clear();
            map.put("total", total);
            map.put("data", inventoryVos);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了");
            e.printStackTrace();
        }
        return httpMessage;

    }

    public HttpMessage searchSku(String skuCode, int currentPage) {
        HttpMessage httpMessage = new HttpMessage();
        List<SkuVo> skuVos = new ArrayList<SkuVo>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Sku.class);
            if (com.util.common.StringUtils.isNotEmpty(skuCode)) {
                criteria.add(Restrictions.eq("skuCode", skuCode));
            }
            criteria.addOrder(Order.desc("id"));

            Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

            criteria.setProjection(null);
            criteria.setFirstResult((currentPage - 1) * 10);
            criteria.setMaxResults(10);

            List<Sku> skus = criteria.list();
            SkuVo skuVo;
            for (Sku sku : skus) {

                skuVo = new SkuVo();
                skuVo.setSkuName(sku.getSkuName());
                skuVo.setCustSkuName(sku.getCustSkuName());
                skuVo.setSkuCode(sku.getSkuCode());
                skuVo.setCustName(sku.getCustName());
                skuVo.setPackageQty(sku.getPackageQty());
                skuVo.setPalletLoadQTy(sku.getPalletLoadQTy());
                skuVo.setSkuEom(sku.getSkuEom());
                skuVo.setSkuSpec(sku.getSkuSpec());
                skuVo.setSkuType(sku.getSkuType());

                skuVos.add(skuVo);

            }

            map.clear();
            map.put("total", total);
            map.put("data", skuVos);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了");
            e.printStackTrace();
        }
        return httpMessage;

    }
}
