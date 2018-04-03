package com.wms.service;

import com.asrs.Barcode;
import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.TransportType;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.TransportOrder;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.asrs.xml.util.XMLUtil;
import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.Block;
import com.wms.domain.blocks.SCar;
import com.wms.vo.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;


/**
 * Created by Administrator on 2016/10/19.
 */
public class InventoryService {

    public HttpMessage inputAreaQuery(String batchNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            List<ReceivingPlan> receivingPlans = session.createCriteria(ReceivingPlan.class)
                    .add(Restrictions.and(Restrictions.eq("batchNo", batchNo),
                            Restrictions.eq("status", "1"))).list();
            if (receivingPlans.isEmpty()) {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("数据出错");
            } else {
                ReceivingPlan receivingPlan = receivingPlans.get(0);
                InputAreaQueryVo inputAreaQueryVo = new InputAreaQueryVo();
                inputAreaQueryVo.setBatchNo(receivingPlan.getBatchNo());
                inputAreaQueryVo.setLotNum(receivingPlan.getLotNum());
                inputAreaQueryVo.setOrderNo(receivingPlan.getOrderNo());
                inputAreaQueryVo.setProviderName(receivingPlan.getProviderName());
                inputAreaQueryVo.setRecvQty(receivingPlan.getRecvedQty());

                httpMessage.setSuccess(true);
                httpMessage.setMsg(inputAreaQueryVo);
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("数据出错");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS, e);

        }
        return httpMessage;
    }

    //入库
    public HttpMessage inputAreaSet(InputAreaQueryVo inputAreaQueryVo, HttpSession httpSession) {
        HttpMessage httpMessage = new HttpMessage();
        String userName = (String) httpSession.getAttribute("userName");
        try {
            Transaction.begin();

            Query q = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type=:jobtype ");
            q.setParameter("jobtype", AsrsJobType.RETRIEVAL);
            List<AsrsJob> jobs = q.list();
            if (!jobs.isEmpty()) {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("请等作业全部完成，在创建新的作业");
            } else {
                Session session = HibernateUtil.getCurrentSession();

                SCar sCar = (SCar) Block.getByBlockNo("SC01");
                if (!sCar.getStatus().equals("1")) {
                    Transaction.rollback();
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车非运行状态");
                    return httpMessage;
                }


                ReceivingPlan receivingPlan = (ReceivingPlan) session.createCriteria(ReceivingPlan.class)
                        .add(Restrictions.and(Restrictions.eq("batchNo", inputAreaQueryVo.getBatchNo()),
                                Restrictions.eq("status", "1"))).setMaxResults(1).uniqueResult();
                if (receivingPlan != null) {
                    receivingPlan.setRecvedQty(receivingPlan.getRecvedQty().add(inputAreaQueryVo.getQty()));

                    //暂时将托盘放入虚拟货位
                    Location locationNo = Location.getByLocationNo(Const.RECV_TEMP_LOCATION);
                    if (locationNo == null) {
                        Transaction.rollback();
                        httpMessage.setSuccess(false);
                        httpMessage.setMsg("没有找到虚拟货位");
                        return httpMessage;
                    }
                    String barcode = Barcode.getNext();
                    Container container = Container.getByBarcode(barcode);
                    if (container != null) {
                        Transaction.rollback();
                        httpMessage.setSuccess(false);
                        httpMessage.setMsg("托盘号重复");
                        return httpMessage;
                    }
                    //获取一个托盘对象
                    container = new Container();
                    container.setBarcode(barcode);
                    container.setLocation(locationNo);
                    container.setReserved(false);
                    container.setCreateDate(new Date());
                    container.setCreateUser(userName);
                    session.save(container);
                    Sku sku = Sku.getByCode(inputAreaQueryVo.getSkuCode());
                    //生成一个库存
                    Inventory inventory = new Inventory();
                    inventory.setProviderName(inputAreaQueryVo.getProviderName());
                    inventory.setOrderNo(inputAreaQueryVo.getOrderNo());
                    inventory.setLotNum(inputAreaQueryVo.getLotNum());
                    inventory.setQty(inputAreaQueryVo.getQty());
                    inventory.setStoreDate(DateFormat.format(new Date(), DateFormat.YYYYMMDD));
                    inventory.setStoreTime(DateFormat.format(new Date(), DateFormat.HHMMSS));
                    inventory.setContainer(container);
                    session.save(inventory);

                    //生成一个入库作业
                    Job job = new Job();
                    job.setMcKey(Mckey.getNext());
                    //设置来自哪个站台
                    job.setFromStation("1101");
                    //设置托盘号
                   // job.setContainer(container);
                    job.setStatus(AsrsJobStatus.WAITING);
                    job.setType(AsrsJobType.PUTAWAY);
                    //托盘暂时先放入虚拟货位
                    job.setFromLocation(locationNo);
                    //从httpSession中获取操作员名字
                    job.setCreateUser(userName);
                    //设置作业创建时间
                    job.setCreateDate(new Date());
                    JobDetail jobDetail = new JobDetail();
                    jobDetail.setInventory(inventory);
                    jobDetail.setQty(inventory.getQty());
                    job.addJobDetail(jobDetail);
                    session.save(job);
                    httpMessage.setSuccess(true);
                    httpMessage.setMsg("入库任务成功生成");
                } else {
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("批次号不存在");
                }
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("数据出错。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS, e);

        }
        return httpMessage;
    }

    public HttpMessage getInventorys(int currentPage, String locationNo, String barcode, String skuCode) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();
            StringBuffer hql = new StringBuffer("select new com.wms.vo.InventoryExcelVo(l.locationNo,c.barcode,i.skuCode,i.qty) from In i,Container c,Location l where i.container.id=c.id and c.location.id=l.id");
            if (StringUtils.isNotBlank(locationNo)) {
                hql.append(" and l.locationNo=:locationNo");
                map.put("locationNo", locationNo);
            }
            if (StringUtils.isNotBlank(barcode)) {
                hql.append(" and c.barcode=:barcode");
                map.put("barcode", barcode);
            }
            if (StringUtils.isNotBlank(skuCode)) {
                hql.append(" and i.skuCode=:skuCode");
                map.put("skuCode", skuCode);
            }
            Query query = session.createQuery(hql.toString());
            Query countQuery = session.createQuery("select count(*) " + hql.substring(hql.indexOf("from In"), hql.length()));
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }
            List<InventoryExcelVo> list = query.setFirstResult((currentPage - 1) * 10).setMaxResults(10).list();
            long total = (long) countQuery.uniqueResult();
            map.clear();
            map.put("total", total);
            map.put("data", list);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS, e);

        }
        return httpMessage;
    }

    /**
     * 出库
     *
     * @param retrievalOrderVos
     * @return
     */
    public HttpMessage rerieval(List<RetrievalOrderVo> retrievalOrderVos) {
        HttpMessage httpMessage = new HttpMessage();
        boolean flag;
        String failNos = "";
        for (RetrievalOrderVo retrievalOrderVo : retrievalOrderVos) {
            try {
                Transaction.begin();

                RetrievalOrder retrievalOrder = RetrievalOrder.getByOrderNo(retrievalOrderVo.getOrderNo());
                if (retrievalOrder == null) {
                    Transaction.rollback();
                    failNos = failNos + retrievalOrder.getOrderNo();
                    continue;
                }

                if (!retrievalOrder.getStatus().equals(RetrievalOrder.STATUS_WAIT)) {
                    Transaction.rollback();
                    failNos = failNos + retrievalOrder.getOrderNo();
                    continue;
                }

                Set<RetrievalOrderDetail> details = retrievalOrder.getRetrievalOrderDetailSet();
                Iterator<RetrievalOrderDetail> itor = details.iterator();
                while (itor.hasNext()) {
                    RetrievalOrderDetail detail = itor.next();
                    //按照出库顺序找库存
                    Query query = HibernateUtil.getCurrentSession().createQuery("select container.barcode,i.lotNum, container.location.seq from Inventory i where i.skuCode =:skuCode and i.whCode =:whCode and i.orderNo!=null group by  container.barcode,i.lotNum, container.location.seq  order by i.lotNum, container.location.seq  ");
                    query.setParameter("skuCode", detail.getItemCode());
                    query.setParameter("whCode", retrievalOrder.getWhCode());
                    List<Object[]> barCodes = query.list();
                    BigDecimal remindQty = detail.getQty();
                    for (Object[] barCode : barCodes) {

                        if (remindQty.compareTo(BigDecimal.ZERO) == -1) {
                            break;
                        }

                        Container container = Container.getByBarcode(barCode[0].toString());
                        List<Inventory> inventories = (List<Inventory>) container.getInventories();
                        for (Inventory inventory : inventories) {
                            remindQty = remindQty.subtract(inventory.getQty());
                            inventory.setOrderNo(retrievalOrder.getOrderNo());

                        }

                        WmsJobInventory wmsJobInventory = new WmsJobInventory();
                        wmsJobInventory.setPalletNo(container.getBarcode());
                        HibernateUtil.getCurrentSession().save(wmsJobInventory);

                    }

                    if (remindQty.compareTo(BigDecimal.ZERO) == 1) {
                        Transaction.rollback();
                        failNos = failNos + retrievalOrder.getOrderNo();
                        break;
                    }

                }

                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();
                failNos = failNos + retrievalOrderVo.getOrderNo();
                LogWriter.error(LoggerType.WMS, e);

            }
        }

        httpMessage.setSuccess(true);
        httpMessage.setMsg("失败订单：" + failNos);
        return httpMessage;
    }



    public HttpMessage searchRetrieval(SearchRetrievalVo vo, int currentPage) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(RetrievalOrder.class);
            if (StringUtils.isNotBlank(vo.getOrderNo())) {
                criteria.add(Restrictions.eq(RetrievalOrder.COL_ORDER_NO, vo.getOrderNo()));
            }
            if (StringUtils.isNotBlank(vo.getStatus())) {
                criteria.add(Restrictions.eq(RetrievalOrder.COL_STATUS, vo.getStatus()));
            }
            if (StringUtils.isNotBlank(vo.getWhCode())) {
                criteria.add(Restrictions.eq(RetrievalOrder.COL_WHCODE, vo.getWhCode()));
            }

            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult((currentPage - 1) * 20);
            criteria.setMaxResults(currentPage * 20);

            List<RetrievalOrder> retrievalOrders = criteria.list();


            List<RetrievalOrderVo> vos = new ArrayList<>();
            RetrievalOrderVo orderVo;

            for (RetrievalOrder order : retrievalOrders) {
                orderVo = new RetrievalOrderVo();
                orderVo.setStatus(order.getStatus());
                orderVo.setCarrierName(order.getCarrierName());
                orderVo.setBoxQty(order.getBoxQty());
                orderVo.setWhCode(order.getWhCode());
                orderVo.setArea(order.getArea());
                orderVo.setCoustomName(order.getCoustomName());
                orderVo.setDesc(order.getDesc());
                orderVo.setJobType(order.getJobType());
                orderVo.setOrderNo(order.getOrderNo());
                orderVo.setToLocation(order.getToLocation());
                vos.add(orderVo);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("total", count);
            map.put("data", vos);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS, e);

        }
        return httpMessage;
    }

    public HttpMessage outStore(List<Integer> invIds) {
        HttpMessage httpMessage = new HttpMessage();

        try {
            Transaction.begin();

            Query bq = HibernateUtil.getCurrentSession().createQuery("from SCar ").setMaxResults(1);
            SCar sCar = (SCar) bq.uniqueResult();
            if (sCar.getPower() < 30) {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("子车电量低");

            } else if (sCar.getStatus().equals("6")) {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("子车充电中");

            } else {

                Query q = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type='01' ");
                List<AsrsJob> jobs = q.list();
                if (!jobs.isEmpty()) {
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("请等作业全部完成，在创建新的作业");
                } else {

                    Query query = HibernateUtil.getCurrentSession().createQuery("from Inventory where id in(:ids)");
                    query.setParameterList("ids", invIds);
                    List<Inventory> inventories = query.list();
                    Map<String, Integer> leftInvs = new HashedMap();//orientation = 1
                    Map<String, Integer> rightInvs = new HashedMap();//orientation = 2
                    for (Inventory inv : inventories) {
                        Query qq = HibernateUtil.getCurrentSession().createQuery("from JobDetail  where inventory.id=:invId");
                        qq.setParameter("invId", inv.getId());
                        if (!qq.list().isEmpty()) {
                            throw new Exception("库存不存在");
                        }

                        Location location = inv.getContainer().getLocation();
                        if (location.getOrientation().equals("2")) {
                            Integer currBank = rightInvs.get(location.getBay() + "-" + location.getLevel());
                            if (currBank == null) {
                                rightInvs.put(location.getBay() + "-" + location.getLevel(), location.getBank());
                            } else {
                                if (currBank < location.getBank()) {
                                    rightInvs.put(location.getBay() + "-" + location.getLevel(), location.getBank());
                                }
                            }
                        } else {
                            Integer currBank = leftInvs.get(location.getBay() + "-" + location.getLevel());
                            if (currBank == null) {
                                leftInvs.put(location.getBay() + "-" + location.getLevel(), location.getBank());
                            } else {
                                if (currBank > location.getBank()) {
                                    leftInvs.put(location.getBay() + "-" + location.getLevel(), location.getBank());
                                }
                            }
                        }
                    }

                    for (Map.Entry<String, Integer> entry : leftInvs.entrySet()) {
                        String[] result = entry.getKey().split("-");
                        Integer bay = Integer.parseInt(result[0]);
                        Integer bank = entry.getValue();
                        Integer level = Integer.parseInt(result[1]);

                        Query invQ = HibernateUtil.getCurrentSession().createQuery("from Inventory where container.location.bay=:bay" +
                                " and container.location.orientation='1' and container.location.bank>=:bank and container.location.level=:level order by container.location.seq desc");
                        invQ.setParameter("bank", bank);
                        invQ.setParameter("bay", bay);
                        invQ.setParameter("level", level);

                        List<Inventory> invs = invQ.list();

                        for (Inventory inventory : invs) {
                            createOutJob(inventory);
                        }
                    }

                    for (Map.Entry<String, Integer> entry : rightInvs.entrySet()) {
                        String[] result = entry.getKey().split("-");
                        Integer bay = Integer.parseInt(result[0]);
                        Integer bank = entry.getValue();
                        Integer level = Integer.parseInt(result[1]);

                        Query invQ = HibernateUtil.getCurrentSession().createQuery("from Inventory where container.location.bay=:bay" +
                                " and container.location.orientation='2' and container.location.bank<=:bank and container.location.level=:level order by container.location.seq desc");
                        invQ.setParameter("bank", bank);
                        invQ.setParameter("bay", bay);
                        invQ.setParameter("level", level);
                        List<Inventory> invs = invQ.list();

                        for (Inventory inventory : invs) {
                            createOutJob(inventory);
                        }

                    }
                    httpMessage.setMsg("出库任务已生成");
                    httpMessage.setSuccess(true);
                }
            }

            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("发生错误，请检查是否已经生成任务");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS, e);

        }
        return httpMessage;
    }

    private void createOutJob(Inventory inventory) throws Exception {
        Location location = inventory.getContainer().getLocation();
        location.setRetrievalRestricted(true);
        //获取容器Container
        Container container = inventory.getContainer();
        container.setReserved(true);
        HibernateUtil.getCurrentSession().update(container);
        //创建一条job
        Job job = new Job();
        job.setType(AsrsJobType.RETRIEVAL);
        String mcKey = Mckey.getNext();
        job.setMcKey(mcKey);
        //job.setContainer(container);
        job.setFromLocation(location);
        job.setToStation("1201");
        job.setStatus(AsrsJobStatus.WAITING);
        job.setCreateDate(new Date());

        JobDetail jobDetail = new JobDetail();
        jobDetail.setJob(job);
        jobDetail.setQty(inventory.getQty());
        jobDetail.setInventory(inventory);
        HibernateUtil.getCurrentSession().save(jobDetail);
        job.addJobDetail(jobDetail);

        HibernateUtil.getCurrentSession().save(job);

        //创建FromLocation对象
        FromLocation fromLocation = new FromLocation();
//        fromLocation.setRack(location.getLocationNo());
//        fromLocation.setX(String.valueOf(location.getBank()));
//        fromLocation.setY(String.valueOf(location.getBay()));
//        fromLocation.setZ(String.valueOf(location.getLevel()));
        fromLocation.setMHA("1201");
        StUnit stUnit = new StUnit();

        stUnit.setStUnitID(container.getBarcode());
        //创建TransportOrderDA对象
        TransportOrderDA transportOrderDA = new TransportOrderDA();
        transportOrderDA.setTransportType(TransportType.RETRIEVAL);
        transportOrderDA.setFromLocation(fromLocation);
        transportOrderDA.setStUnit(stUnit);

        //创建sender对象
        Sender sender = new Sender();
        sender.setDivision(XMLConstant.COM_DIVISION);

        RefId refId = new RefId();
        refId.setReferenceId(mcKey);

        //创建ControlArea对象
        ControlArea controlArea = new ControlArea();
        controlArea.setRefId(refId);
        controlArea.setCreationDateTime(DateFormat.format(new Date(), DateFormat.YYYYMMDDHHMMSS));
        controlArea.setSender(sender);

        //wms发送给wcs的运输命令
        TransportOrder transportOrder = new TransportOrder();
        transportOrder.setDataArea(transportOrderDA);
        transportOrder.setControlArea(controlArea);

        Envelope envelope = new Envelope();
        envelope.setTransportOrder(transportOrder);

        XMLUtil.sendEnvelope(envelope);
    }

    public HttpMessage closeRecvPlan(String batchNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            List<ReceivingPlan> receivingPlans = HibernateUtil.getCurrentSession().createCriteria(ReceivingPlan.class)
                    .add(Restrictions.and(Restrictions.eq("batchNo", batchNo),
                            Restrictions.eq("status", "1"))).list();
            for (ReceivingPlan plan : receivingPlans) {
                plan.setStatus("3");
            }

            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("入库单成功关闭");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("系统异常");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS, e);

        }

        return httpMessage;
    }

    public HttpMessage recePlanSearch(SearchRetrievalVo searchRetrievalVo) {

        HttpMessage httpMessage = new HttpMessage();

        return httpMessage;
    }

    public HttpMessage createReceivingPlan(InputAreaQueryVo inputAreaQueryVo, HttpSession httpSession) {
        HttpMessage httpMessage = new HttpMessage();

        try {

            Transaction.begin();

            Query query = HibernateUtil.getCurrentSession().createQuery("from ReceivingPlan where batchNo=:batchNo");
            query.setParameter("batchNo", inputAreaQueryVo.getBatchNo());
            query.setMaxResults(1);
            ReceivingPlan plan = (ReceivingPlan) query.uniqueResult();
            if (plan == null) {

                plan = new ReceivingPlan();

                Sku sku = Sku.getByCode(inputAreaQueryVo.getSkuCode());
                if (sku != null) {

                    plan.setStatus("1");
                    plan.setProviderName(inputAreaQueryVo.getProviderName());
                    plan.setLotNum(inputAreaQueryVo.getLotNum());
                    plan.setRecvedQty(BigDecimal.ZERO);
                    plan.setQty(inputAreaQueryVo.getQty());
                    plan.setOrderNo(inputAreaQueryVo.getOrderNo());
                    plan.setBatchNo(inputAreaQueryVo.getBatchNo());

                    HibernateUtil.getCurrentSession().save(plan);

                    httpMessage.setSuccess(true);
                    httpMessage.setMsg("新建入库单成功");

                } else {
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("商品不存在");

                }

            } else {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("入库单批次已经存在");

            }

            Transaction.commit();


        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS, e);

        }

        return httpMessage;
    }

    public HttpMessage checkSku(String skuCode) {

        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from Sku  where skuCode=:skuCode");
            query.setParameter("skuCode", skuCode);
            query.setMaxResults(1);
            Sku sku = (Sku) query.uniqueResult();
            if (sku != null) {
                InputAreaQueryVo inputAreaQueryVo = new InputAreaQueryVo();
                inputAreaQueryVo.setCustName(sku.getCustName());
                inputAreaQueryVo.setCustSkuName(sku.getCustSkuName());
                inputAreaQueryVo.setSkuCode(sku.getSkuCode());
                inputAreaQueryVo.setSkuEom(sku.getSkuEom());
                inputAreaQueryVo.setSkuName(sku.getSkuName());
                inputAreaQueryVo.setSkuSpec(sku.getSkuSpec());

                httpMessage.setSuccess(true);
                httpMessage.setMsg(inputAreaQueryVo);

            } else {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("数据出错");

            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("数据出错");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS, e);

        }
        return httpMessage;

    }
}
