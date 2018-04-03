package com.order.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.inventory.vo.InventoryVo;
import com.inventory.vo.RetrievalOrderIds;
import com.order.vo.*;
import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.pages.GridPages;
import com.webservice.vo.RetrievalCloseVo;
import com.wms.domain.*;
import com.wms.domain.blocks.MCar;
import net.sf.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by van on 2018/1/13.
 */
@Service
public class OrderService {


    public PagerReturnObj<List<OrderVo>> list(SearchOrderVo searchOrderVo, GridPages pages) {
        PagerReturnObj<List<OrderVo>> returnObj = new PagerReturnObj<List<OrderVo>>();
        try {
            Transaction.begin();

            DateTimeFormatter formatter = new DateTimeFormatter();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(RetrievalOrder.class);

            if (StringUtils.isNotEmpty(searchOrderVo.getOrderNo())) {
                criteria.add(Restrictions.eq(RetrievalOrder.COL_ORDER_NO, searchOrderVo.getOrderNo()));
            }
            if (StringUtils.isNotEmpty(searchOrderVo.getStatus())) {
                criteria.add(Restrictions.eq(RetrievalOrder.COL_STATUS, searchOrderVo.getStatus()));
            }
            if (StringUtils.isNotEmpty(searchOrderVo.getWhCode())) {
                criteria.add(Restrictions.eq(RetrievalOrder.COL_WHCODE, searchOrderVo.getWhCode()));
            }

            if (StringUtils.isNotEmpty(searchOrderVo.getBeginDate())) {
                criteria.add(Restrictions.ge(RetrievalOrder.COL_CREATEDATE, formatter.unformat(searchOrderVo.getBeginDate() + " 00:00:00")));
            }
            if (StringUtils.isNotEmpty(searchOrderVo.getEndDate())) {
                criteria.add(Restrictions.le(RetrievalOrder.COL_CREATEDATE, formatter.unformat(searchOrderVo.getEndDate() + " 23:59:59")));
            }

            criteria.addOrder(Order.desc(RetrievalOrder.COL_ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<RetrievalOrder> list = criteria.list();

            List<OrderVo> vos = new ArrayList<OrderVo>(pages.getPageSize());
            OrderVo vo;
            for (RetrievalOrder order : list) {
                vo = new OrderVo();
                vo.setId(order.getId());
                vo.setOrderNo(order.getOrderNo());
                vo.setStatus(order.getStatus());
                vo.setArea(order.getArea());
                vo.setBoxQty(order.getBoxQty());
                vo.setCarrierName(order.getCarrierName());
                vo.setCoustomName(order.getCoustomName());
                vo.setWhCode(order.getWhCode());
                vo.setJobType(order.getJobType());
                vo.setToLocation(order.getToLocation());
                vo.setDesc(order.getDesc());
                vos.add(vo);
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(vos);
            returnObj.setCount(count);
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

    public ReturnObj<List<OrderDetailVo>> searchDetail(Integer orderId, String keyword) {

        ReturnObj<List<OrderDetailVo>> returnObj = new ReturnObj<>();
        try {

            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(RetrievalOrderDetail.class);
            Criteria orderCri = criteria.createCriteria(RetrievalOrderDetail.COL_RETRIEVALORDER);
            orderCri.add(Restrictions.eq(RetrievalOrder.COL_ID, orderId));
            if (StringUtils.isNotEmpty(keyword)) {
                criteria.add(Restrictions.eq(RetrievalOrderDetail.COL_ITEMCODE, keyword));
            }
            List<RetrievalOrderDetail> orderDetails = criteria.list();
            List<OrderDetailVo> vos = new ArrayList<>();
            OrderDetailVo vo;
            for (RetrievalOrderDetail detail : orderDetails) {
                vo = new OrderDetailVo();

                vo.setId(detail.getId());
                vo.setItemCode(detail.getItemCode());
                vo.setPalletNo(detail.getPalletNo());
                vo.setBatch(detail.getBatch());
                vo.setQty(detail.getQty());

                vos.add(vo);
            }
            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(vos);

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

    public BaseReturnObj retrieval(List<RetrievalOrderIds> orderIds, int priority) {
        BaseReturnObj returnObj = new BaseReturnObj();
        String failNos = "";

        try {

            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type!=:jt and type !=:ojt");
            query.setParameter("jt", AsrsJobType.PUTAWAY);
            query.setParameter("ojt", AsrsJobType.RETRIEVAL);
            List<AsrsJob> asrsJobs = query.list();

            Transaction.commit();

            if (!asrsJobs.isEmpty()) {
                returnObj.setSuccess(false);
                returnObj.setMsg("系统正在进行其他出库任务");
                return returnObj;
            }

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统异常");
            return returnObj;
        }

        for (RetrievalOrderIds orderId : orderIds) {
            try {
                Transaction.begin();

                RetrievalOrder retrievalOrder = RetrievalOrder.getById(orderId.getId());
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

                List<String> containers = new ArrayList<>();

                boolean flag = true;

                if ("4".equals(retrievalOrder.getJobType())) {
                    //指定托盘出库

                    List<String> currentBarCods = new ArrayList<>();
                    List<RetrievalOrderDetail> orderDetails = new ArrayList<>(retrievalOrder.getRetrievalOrderDetailSet());
                    for (RetrievalOrderDetail detail : orderDetails) {
                        currentBarCods.add(detail.getBatch());
                    }

                    Set<RetrievalOrderDetail> details = retrievalOrder.getRetrievalOrderDetailSet();
                    Iterator<RetrievalOrderDetail> itor = details.iterator();
                    while (itor.hasNext()) {

                        RetrievalOrderDetail detail = itor.next();

                        if (org.apache.commons.lang.StringUtils.isNotEmpty(detail.getPalletNo())) {
                            //指定托盘出库
                            Container container = Container.getByBarcode(detail.getPalletNo());
                            if (container != null) {
                                Location location = container.getLocation();
                                Query query = HibernateUtil.getCurrentSession().createQuery(" from Container where location.position=:po and location.level=:lv " +
                                        "and location.bay=:b and location.seq>:s and location.actualArea=:ar and barcode not in (:barcodes)");
                                query.setParameter("po", location.getPosition());
                                query.setParameter("lv", location.getLevel());
                                query.setParameter("b", location.getBay());
                                query.setParameter("s", location.getSeq());
                                query.setParameter("ar", location.getActualArea());
                                query.setParameterList("barcodes", currentBarCods);
                                List<Container> cis = query.list();
                                if (cis.isEmpty()) {
                                    if (containers.indexOf(container.getBarcode()) == -1)
                                        containers.add(container.getBarcode());
                                } else {
                                    flag = false;
                                    break;
                                }

                            } else {
                                flag = false;
                                break;
                            }
                        }
                    }

                } else {

                    Set<RetrievalOrderDetail> details = retrievalOrder.getRetrievalOrderDetailSet();
                    Iterator<RetrievalOrderDetail> itor = details.iterator();
                    while (itor.hasNext()) {
                        RetrievalOrderDetail detail = itor.next();

                        BigDecimal remindQty = detail.getQty();

                        Query q = HibernateUtil.getCurrentSession().createQuery("select i.lotNum, i.container.location.bay,i.container.location.level,i.container.location.position,i.container.location.actualArea " +
                                " from Inventory i where i.container.status = '整托' and i.status='0' and i.skuCode=:skuCode and i.whCode=:whCode " +
                                " group by i.container.location.bay,i.container.location.level,i.container.location.position,i.container.location.actualArea,i.lotNum order by i.lotNum");
                        q.setParameter("skuCode", detail.getItemCode());
                        q.setParameter("whCode", retrievalOrder.getWhCode());
                        List<Object[]> objects = q.list();

                        if (remindQty.compareTo(BigDecimal.ZERO) <= 0) {
                            break;
                        }

                        List<String> locations = new ArrayList<>();
                        for (Object[] obj : objects) {

                            Query jobQ = HibernateUtil.getCurrentSession().createQuery("from Job where type=:jType and toLocation.position=:po and toLocation.bay=:bay and toLocation.level=:lev and toLocation.actualArea=:area");
                            jobQ.setParameter("bay", Integer.parseInt(obj[1].toString()));
                            jobQ.setParameter("lev", Integer.parseInt(obj[2].toString()));
                            jobQ.setParameter("po", obj[3].toString());
                            jobQ.setParameter("area", obj[4].toString());
                            jobQ.setParameter("jType", AsrsJobType.PUTAWAY);
                            if (!jobQ.list().isEmpty()) {
                                //如果该巷道有入库作业，放弃该巷道
                                continue;
                            }

                            if (remindQty.compareTo(BigDecimal.ZERO) <= 0) {
                                break;
                            }

                            String position = obj[1] + "-" + obj[2] + "-" + obj[3] + "-" + obj[4];
                            if (locations.indexOf(position) != -1) {
                                continue;
                            }
                            Query qq = HibernateUtil.getCurrentSession().createQuery(
                                    " select i.container.barcode, i.container.location.seq,i.container.location.level,i.container.location.bay,i.container.location.position " +
                                            " from Inventory i where i.container.location.bay=:bay and i.container.location.level=:lev and i.container.location.position=:po" +
                                            " and i.container.location.actualArea=:area and i.orderNo is null group by i.container.barcode, i.container.location.seq,i.container.location.level,i.container.location.bay,i.container.location.position " +
                                            " order by i.container.location.seq desc");
                            qq.setParameter("bay", Integer.parseInt(obj[1].toString()));
                            qq.setParameter("lev", Integer.parseInt(obj[2].toString()));
                            qq.setParameter("po", obj[3].toString());
                            qq.setParameter("area", obj[4].toString());

                            List<Object[]> barCodes = qq.list();

                            for (Object[] barCode : barCodes) {

                                if (remindQty.compareTo(BigDecimal.ZERO) <= 0) {
                                    break;
                                }

                                Container container = Container.getByBarcode(barCode[0].toString());
                                List<Inventory> inventories = (List<Inventory>) container.getInventories();
                                if (!inventories.get(0).getStatus().equals("0")) {
                                    break;
                                }
                                for (Inventory inventory : inventories) {
                                    remindQty = remindQty.subtract(inventory.getQty());
                                    inventory.setOrderNo(retrievalOrder.getOrderNo());

                                }
                                if (containers.indexOf(container.getBarcode()) == -1)
                                    containers.add(container.getBarcode());

                            }


                            locations.add(position);

                        }

                        if (remindQty.compareTo(BigDecimal.ZERO) == 1) {
                            Transaction.rollback();
                            failNos = failNos + retrievalOrder.getOrderNo();
                            flag = false;
                            break;
                        }

                    }

                }

                if (!flag) {
                    break;
                }

                for (String barCode : containers) {
                    Container container = Container.getByBarcode(barCode);
                    Location location = container.getLocation();
                    MCar srm = MCar.getMCarByPosition(location.getPosition(),location.getLevel());
                    Configuration configuration = Configuration.getConfig(srm.getBlockNo());
                    Job job = new Job();
                    job.setStatus("1");
                    job.setFromLocation(location);
                    job.setFromStation(srm.getBlockNo());
                    job.setMcKey(Mckey.getNext());
                    job.setToStation(configuration.getValue());
                    job.setOrderNo(retrievalOrder.getOrderNo());
                    job.setContainer(barCode);
                    job.setType(AsrsJobType.RETRIEVAL);
                    HibernateUtil.getCurrentSession().save(job);

                }

                retrievalOrder.setStatus("1");

                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();
                failNos = failNos + orderId.getId() + ";";
                LogWriter.error(LoggerType.WMS, e);

            }
        }

        returnObj.setSuccess(true);
        if (failNos.equals("")) {
            returnObj.setMsg("全部出库成功 ");
        } else {
            returnObj.setMsg(failNos + "出库失败");
        }

        return returnObj;
    }

    public BaseReturnObj close(String orderNo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            RetrievalOrder retrievalOrder = RetrievalOrder.getByOrderNo(orderNo);
            if (retrievalOrder == null) {
                throw new WmsServiceException("订单不存在");
            }

            RetrievalCloseVo closeVo = new RetrievalCloseVo();
            closeVo.setWhCode(retrievalOrder.getWhCode());
            closeVo.setOrderType(retrievalOrder.getJobType());
            closeVo.setOrderCode(retrievalOrder.getOrderNo());
            String param = JSONObject.fromObject(closeVo).toString();
            String result = ContentUtil.getResultJsonType(Const.OPPLE_OUT_CLOSE_WMS_URL, param);
            JSONObject jsonObject = JSONObject.fromObject(result);
            if (!(Boolean) jsonObject.get("success")) {
                throw new WmsServiceException("订单取消失败");
            } else {
                retrievalOrder.setStatus(RetrievalOrder.STATUS_CANCEL);
            }

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("关闭成功");

        } catch (WmsServiceException e) {
            Transaction.rollback();
            e.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg(e.getMessage());
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统错误");
        }

        return returnObj;

    }

    public PagerReturnObj<List<InventoryVo>> invList(OrderSearchInvVo searchVo, GridPages pages) {
        PagerReturnObj<List<InventoryVo>> returnObj = new PagerReturnObj<List<InventoryVo>>();
        try {
            Transaction.begin();

            StringBuffer sb = new StringBuffer("select whCode,container.barcode,container.location.locationNo,skuCode,skuName,sum(qty),lotNum,container.location.bank,container.location.bay,container.location.level,status from Inventory where 1=1  ");
            if (StringUtils.isNotEmpty(searchVo.getWhCode())) {
                sb.append(" and whCode=:whCode ");
            }
            if (StringUtils.isNotEmpty(searchVo.getSkuCode())) {
                sb.append("  and  skuCode=:skuCode ");
            }
            if (StringUtils.isNotEmpty(searchVo.getContainerBarcode())) {
                sb.append(" and  container.barcode=:barCode ");
            }
            if (StringUtils.isNotEmpty(searchVo.getLocationNo())) {
                sb.append(" and  container.location.locationNo=:locationNo ");
            }
            if (StringUtils.isNotEmpty(searchVo.getPosition())) {
                sb.append(" and  container.location.position=:po ");
            }
            if (StringUtils.isNotEmpty(searchVo.getBayNo())) {
                sb.append(" and  container.location.bay=:bay  ");
            }
            if (StringUtils.isNotEmpty(searchVo.getLevelNo())) {
                sb.append(" and  container.location.level=:lev ");
            }
            sb.append(" group by whCode,container.barcode,container.location.locationNo,skuCode,skuName,lotNum,container.location.bank,container.location.bay,container.location.level,status order by container.location.locationNo asc ");


            Query query = HibernateUtil.getCurrentSession().createQuery(sb.toString());

            if (StringUtils.isNotEmpty(searchVo.getWhCode())) {
                query.setParameter("whCode", searchVo.getWhCode());
            }
            if (StringUtils.isNotEmpty(searchVo.getSkuCode())) {
                query.setParameter("skuCode", searchVo.getSkuCode());
            }
            if (StringUtils.isNotEmpty(searchVo.getContainerBarcode())) {
                query.setParameter("barCode", searchVo.getContainerBarcode());
            }
            if (StringUtils.isNotEmpty(searchVo.getLocationNo())) {
                query.setParameter("locationNo", searchVo.getLocationNo());
            }
            if (StringUtils.isNotEmpty(searchVo.getPosition())) {
                query.setParameter("po", searchVo.getPosition());
            }
            if (StringUtils.isNotEmpty(searchVo.getBayNo())) {
                query.setParameter("bay", Integer.parseInt(searchVo.getBayNo()));
            }
            if (StringUtils.isNotEmpty(searchVo.getLevelNo())) {
                query.setParameter("lev", Integer.parseInt(searchVo.getLevelNo()));
            }

            List<Object[]> counts = query.list();

            //获取分页数据s
            query.setFirstResult(pages.getFirstRow());
            query.setMaxResults(pages.getPageSize());

            List<Object[]> list = query.list();

            List<InventoryVo> vos = new ArrayList<InventoryVo>(pages.getPageSize());

            InventoryVo vo;
            int i = 1;
            for (Object[] object : list) {
                vo = new InventoryVo();
                vo.setId(i);
                i++;
                vo.setWhCode(String.valueOf(object[0]));
                vo.setPalletNo(String.valueOf(object[1]));
                vo.setLocationNo(String.valueOf(object[2]));
                vo.setItemCode(String.valueOf(object[3]));
                vo.setItemName(String.valueOf(object[4]));
                vo.setQty(new BigDecimal(String.valueOf(object[5])));
                vo.setLotNum(String.valueOf(object[6]));
                vo.setBank((Integer) object[7]);
                vo.setBay((Integer) object[8]);
                vo.setLev((Integer) object[9]);
                vo.setStatus((String) object[10]);
                vos.add(vo);
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(vos);
            returnObj.setCount(counts.size());
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());
            ex.printStackTrace();

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
            ex.printStackTrace();

        }

        return returnObj;
    }


    public BaseReturnObj directRetrieval(List<ContainerIds> ids) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {

            Transaction.begin();
            List<String> locations = new ArrayList<>();

            for (ContainerIds containerIds : ids) {
                Container container = Container.getByBarcode(containerIds.getBarCode());
                locations.add(container.getLocation().getLocationNo());
            }

            for (String locationNo : locations) {

                Location location = Location.getByLocationNo(locationNo);

                Query query = HibernateUtil.getCurrentSession().createQuery("from Location l where (l.reserved=true or l.empty=false) and l.position=:po and l.level=:lv " +
                        " and l.bay =:bay and l.actualArea=:area and l.seq >:seq and l.locationNo not in (:locationNos) ");
                query.setParameter("po", location.getPosition());
                query.setParameter("lv", location.getLevel());
                query.setParameter("bay", location.getBay());
                query.setParameter("area", location.getActualArea());
                query.setParameter("seq", location.getSeq());
                query.setParameterList("locationNos", locations);
                List<Location> locationList = query.list();
                if (!locationList.isEmpty()) {
                    throw new Exception("出库路劲不通");
                }

            }

            for (String locationNo : locations) {

                Query query = HibernateUtil.getCurrentSession().createQuery("from Container where location.locationNo=:locationNo");
                query.setParameter("locationNo", locationNo);
                Container container = (Container) query.uniqueResult();

                Location location = container.getLocation();
                MCar srm = MCar.getMCarByPosition(location.getPosition(),location.getLevel());
                Configuration configuration = Configuration.getConfig(srm.getBlockNo());
                Job job = new Job();
                job.setStatus("1");
                job.setFromLocation(location);
                job.setFromStation(srm.getBlockNo());
                job.setMcKey(Mckey.getNext());
                job.setToStation(configuration.getValue());
                Date date = new Date();
                DateTimeFormatter dateTimeFormatter = new DateTimeFormatter("yyyyMMddHH24mmss");
                job.setOrderNo(dateTimeFormatter.format(date));
                job.setContainer(container.getBarcode());
                job.setType(AsrsJobType.RETRIEVAL);
                HibernateUtil.getCurrentSession().save(job);

                location.setReserved(true);

                List<Inventory> inventories = new ArrayList<>(container.getInventories());
                for (Inventory inventory : inventories) {
                    inventory.setOrderNo(job.getOrderNo());
                }

            }


            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("出库成功");

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
