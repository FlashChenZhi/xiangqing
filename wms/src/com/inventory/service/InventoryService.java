package com.inventory.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.inventory.vo.*;
import com.util.common.*;
import com.util.excel.ExcelExportParam;
import com.util.excel.ExcelExportUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.pages.GridPages;
import com.wms.domain.*;
import com.wms.domain.blocks.MCar;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by van on 2018/1/14.
 */
@Service
public class InventoryService {

    public PagerReturnObj<List<InventoryVo>> list(SearchInventoryVo searchVo, GridPages pages) {
        PagerReturnObj<List<InventoryVo>> returnObj = new PagerReturnObj<List<InventoryVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Inventory.class);
            Criteria conCri = criteria.createCriteria(Inventory.COL_CONTAINER);

            if (StringUtils.isNotEmpty(searchVo.getWhCode())) {
                criteria.add(Restrictions.eq(Inventory.COL_WHCODE, searchVo.getWhCode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getSkuCode())) {
                criteria.add(Restrictions.eq(Inventory.COL_SKUCODE, searchVo.getSkuCode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getBarcodes())) {
                criteria.add(Restrictions.eq(Inventory.COL_CASEBARCODE, searchVo.getBarcodes()));
            }
            if (StringUtils.isNotEmpty(searchVo.getContainerBarcode())) {
                conCri.add(Restrictions.eq(Container.__BARCODE, searchVo.getContainerBarcode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getLocationNo())) {
                Criteria loCri = conCri.createCriteria(Container.__LOCATION);
                loCri.add(Restrictions.eq(Location.__LOCATIONNO, searchVo.getLocationNo()));
            }
            if (StringUtils.isNotEmpty(searchVo.getBatchNo())) {
                criteria.add(Restrictions.eq(Inventory.COL_LOTNUM, searchVo.getBatchNo()));
            }
            if (StringUtils.isNotEmpty(searchVo.getStatus())) {
                criteria.add(Restrictions.eq(Inventory.COL_STATUS, searchVo.getStatus()));
            }

            criteria.addOrder(Order.desc(Inventory.__ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据s
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<Inventory> list = criteria.list();

            List<InventoryVo> vos = new ArrayList<InventoryVo>(pages.getPageSize());
            InventoryVo vo;
            for (Inventory inventory : list) {
                vo = new InventoryVo();
                vo.setId(inventory.getId());
                vo.setQty(inventory.getQty());
                vo.setPalletNo(inventory.getContainer().getBarcode());
                vo.setWhCode(inventory.getWhCode());
                vo.setItemCode(inventory.getSkuCode());
                vo.setCaseBarCode(inventory.getCaseBarCode());
                vo.setLotNum(inventory.getLotNum());
                vo.setStatus(inventory.getStatus());
                vo.setLocationNo(inventory.getContainer().getLocation().getLocationNo());
                vo.setItemName(inventory.getSkuName());
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

    public PagerReturnObj<List<InventoryLogVo>> searchLog(SearchInvLogVo searchVo, GridPages pages) {
        PagerReturnObj<List<InventoryLogVo>> returnObj = new PagerReturnObj<List<InventoryLogVo>>();
        try {
            Transaction.begin();
            DateTimeFormatter formatter = new DateTimeFormatter();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(InventoryLog.class);
            if (StringUtils.isNotEmpty(searchVo.getContainerBarcode())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_CONTAINER, searchVo.getContainerBarcode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getFromLocation())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_FROMLOCATION, searchVo.getFromLocation()));
            }
            if (StringUtils.isNotEmpty(searchVo.getToLocation())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_TOLOCATION, searchVo.getToLocation()));
            }
            if (StringUtils.isNotEmpty(searchVo.getSkuCode())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_SKUCODE, searchVo.getSkuCode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getJobType())) {
                if (searchVo.getJobType().equals("03")) {
                    searchVo.setJobType("02");
                }
                criteria.add(Restrictions.eq(InventoryLog.COL_TYPE, searchVo.getJobType()));
            }
            if (StringUtils.isNotEmpty(searchVo.getBeginCreateDate())) {
                criteria.add(Restrictions.ge(InventoryLog.COL_CREATEDATE, formatter.unformat(searchVo.getBeginCreateDate() + " 00:00:00")));
            }
            if (StringUtils.isNotEmpty(searchVo.getEndCreateDate())) {
                criteria.add(Restrictions.le(InventoryLog.COL_CREATEDATE, formatter.unformat(searchVo.getEndCreateDate() + " 23:59:59")));
            }
            if (StringUtils.isNotEmpty(searchVo.getOrderNo())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_ORDERNO, searchVo.getOrderNo()));
            }

            criteria.addOrder(Order.desc(InventoryLog.COL_ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据s
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<InventoryLog> list = criteria.list();

            List<InventoryLogVo> vos = new ArrayList<InventoryLogVo>(pages.getPageSize());
            InventoryLogVo vo;
            DateFormat dateFormat = new DateFormat();
            for (InventoryLog log : list) {
                vo = new InventoryLogVo();
                vo.setContainer(log.getContainer());
                vo.setFromLocation(log.getFromLocation());
                vo.setToLocation(log.getToLocation());
                vo.setQty(log.getQty());
                vo.setWhCode(log.getWhCode());
                vo.setSkuCode(log.getSkuCode());
                vo.setLotNum(log.getLotNum());
                vo.setJobType(log.getType());
                vo.setOrderNo(log.getOrderNo());
                vo.setId(log.getId());
                vo.setSkuName(log.getSkuName());
                vo.setCreateDate(dateFormat.format(new Date(), DateFormat.YYYYMMDDHHMMSS));

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

    public ReturnObj<List<AutoLocationListVo>> inventoryMap() {
        ReturnObj<List<AutoLocationListVo>> returnObj = new ReturnObj<List<AutoLocationListVo>>();
        try {
            Transaction.begin();

            Query locQ = HibernateUtil.getCurrentSession().createQuery("from Location loc order by loc.bank, loc.bay, loc.level asc");
            List<Location> locations = locQ.list();

            Query invQ = HibernateUtil.getCurrentSession().createQuery("select inv.container.location.locationNo,inv.container.barcode,inv.skuCode,inv.lotNum, inv.container.status,sum(inv.qty) from Inventory inv" +
                    " group by inv.container.location.locationNo,inv.container.barcode,inv.skuCode,inv.lotNum, inv.container.status" +
                    " order by inv.container.location.locationNo asc");
            List<Object[]> invs = invQ.list();

            Map<String, List<Object[]>> invMap = new HashMap<String, List<Object[]>>();
            for (Object[] obj : invs) {
                String locationNo = obj[0] == null ? "" : (String) obj[0];
                if (StringUtils.isEmpty(locationNo))
                    continue;

                if (invMap.containsKey(locationNo)) {
                    List<Object[]> objs = invMap.get(locationNo);
                    objs.add(obj);
                    invMap.put(locationNo, objs);
                } else {
                    List<Object[]> objs = new ArrayList<Object[]>();
                    objs.add(obj);
                    invMap.put(locationNo, objs);
                }
            }

            Query emptyPalletQ = HibernateUtil.getCurrentSession().createQuery("select con.location.locationNo,con.barcode from Container con where  " +
                    "   not exists (select 1 from Inventory inv where inv.container.id = con.id)" +
                    " order by con.id asc ");
            List<Object[]> emptyPallets = emptyPalletQ.list();


            Map<String, String> emptyPalletMap = new HashMap<String, String>();
            for (Object[] obj : emptyPallets) {
                String locationNo = obj[0] == null ? "" : (String) obj[0];
                String palletBarcode = obj[1] == null ? "" : (String) obj[1];
                if (StringUtils.isEmpty(locationNo))
                    continue;

                if (!emptyPalletMap.containsKey(locationNo)) {
                    emptyPalletMap.put(locationNo, palletBarcode);
                }
            }

            Query asrsJobQ = HibernateUtil.getCurrentSession().createQuery("from AsrsJob aj where " +
                    " exists (select 1 from Location loc where aj.toLocation = loc.locationNo )" +
                    " order by aj.id asc ");
            List<AsrsJob> asrsJobs = asrsJobQ.list();

            Map<String, String> asrsJobToLocationMap = new HashMap<String, String>();
            for (AsrsJob asrsJob : asrsJobs) {
                String str = asrsJobToLocationMap.get(asrsJob.getToLocation());
                if (str == null) {
                    asrsJobToLocationMap.put(asrsJob.getToLocation(), asrsJob.getMcKey());
                } else {
                    str = str + "、" + asrsJob.getMcKey();
                    asrsJobToLocationMap.put(asrsJob.getToLocation(), str);
                }
            }

            List<AutoLocationListVo> list = new ArrayList<AutoLocationListVo>();
            for (Location location : locations) {
                AutoLocationListVo vo = new AutoLocationListVo();
                vo.setLocationNo(location.getAlias());
                vo.setBank(location.getBank());
                vo.setRealLocation(location.getLocationNo());

                boolean existInvFlag = false;
                List<Object[]> invResList = invMap.get(location.getLocationNo());
                if (invResList != null) {
                    for (Object[] invRes : invResList) {
                        String containerBarcode = invRes[1] == null ? "" : (String) invRes[1];
                        String skuCode = invRes[2] == null ? "" : (String) invRes[2];
                        String productDate = invRes[3] == null ? "" : (String) invRes[3];
                        String qaStatus = invRes[4] == null ? "" : (String) invRes[4];
                        BigDecimal qty = invRes[5] == null ? BigDecimal.ZERO : (BigDecimal) invRes[5];

                        AutoLocationListDetailVo detailVo = new AutoLocationListDetailVo();
                        detailVo.setContainerBarcode(containerBarcode);
                        detailVo.setSkuCode(skuCode);
                        detailVo.setProductDate(productDate);
                        detailVo.setQty(qty.toString());
                        detailVo.setQaStatus(qaStatus);
                        vo.getDetailVos().add(detailVo);

                        existInvFlag = true;
                    }
                }
                if (location.isEmpty() && !existInvFlag) {
                    vo.setStatus(AutoLocationListVo.EMPTY);
                    vo.setRemark(getRemark(location, "空货位"));
                }
                if (!location.isEmpty() && existInvFlag) {
                    vo.setStatus(AutoLocationListVo.NORMAL);
                    vo.setRemark(getRemark(vo, ""));
                }

                if (location.isEmpty() && asrsJobToLocationMap.containsKey(location.getLocationNo()) && !existInvFlag) {
                    String mckey = asrsJobToLocationMap.get(location.getLocationNo());
                    vo.setStatus(AutoLocationListVo.TRANSFER_RESERVED);
                    vo.setRemark(getRemark(location, mckey, "搬送预约"));
                }

                if (!location.isEmpty() && !asrsJobToLocationMap.containsKey(location.getLocationNo()) && !existInvFlag) {
                    vo.setStatus(AutoLocationListVo.WARN);
                    vo.setRemark(getRemark(location, "实货位,无库存"));
                }

                if (location.isEmpty() && existInvFlag) {
                    vo.setStatus(AutoLocationListVo.WARN);
                    vo.setRemark(getRemark(vo, "空货位,有库存"));
                }

                if (location.isEmpty() && location.getReserved()) {
                    vo.setStatus(AutoLocationListVo.WARN);
                    vo.setRemark(getRemark(location, "空货位，限制使用"));
                }


                if (emptyPalletMap.containsKey(vo.getLocationNo())) {
                    vo.setStatus(AutoLocationListVo.EMPTY_PALLET);
                    vo.setRemark(getRemark(location, "空托盘"));
                }

                list.add(vo);
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(list);
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());
            ex.printStackTrace();

        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());
            ex.printStackTrace();

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
            ex.printStackTrace();

        }
        return returnObj;
    }

    private String getRemark(Location location, String memo) {
        StringBuffer sb = new StringBuffer();
        sb.append("<ul style=\"width:400px;\" >");
        sb.append("<li>");
        sb.append("<span class=\"b\">");
        sb.append("货位号 : " + location.getLocationNo() + "<br/>");

        sb.append("备注 : " + memo);
        sb.append("</span>");
        sb.append("</li>");
        sb.append("</ul>");


        return sb.toString();

    }

    private String getRemark(Location location, String mckey, String memo) {
        StringBuffer sb = new StringBuffer();
        sb.append("<ul style=\"width:400px;\" >");
        sb.append("<li>");
        sb.append("<span class=\"b\">");
        sb.append("货位号 : " + location.getLocationNo() + "<br/>");
        if (StringUtils.isNotEmpty(mckey)) {
            sb.append("McKey : " + mckey + "<br/>");
        }
        sb.append("备注 : " + memo);
        sb.append("</span>");
        sb.append("</li>");
        sb.append("</ul>");


        return sb.toString();

    }


    private String getRemark(AutoLocationListVo vo, String memo) {
        StringBuffer sb = new StringBuffer();
        sb.append("<ul style=\"width:400px;\" >");
        sb.append("<li>");
        int index = 1;
        for (AutoLocationListDetailVo detailVo : vo.getDetailVos()) {
            sb.append("<span class=\"b\">");
            if (index == 1) {
                sb.append("货位号 : " + vo.getRealLocation() + "<br/>");
                if (StringUtils.isNotEmpty(vo.getDpsAlias())) {
                    sb.append("DPS : " + vo.getDpsAlias() + "<br/>");
                }
                if (StringUtils.isNotEmpty(vo.getBindSkuCode())) {
                    sb.append("绑定SKU : " + vo.getBindSkuCode() + "<br/>");
                }
            } else {
                sb.append("<br/>");
            }
            sb.append("容器号 : " + detailVo.getContainerBarcode() + "<br/>");
            sb.append("SKU代码 : " + detailVo.getSkuCode() + "<br/>");
            sb.append("库存数量 : " + detailVo.getQty() + "<br/>");
            sb.append("库存单位 : " + detailVo.getEom() + "<br/>");
            if (StringUtils.isNotEmpty(detailVo.getProductDate())) {
                sb.append("生产日期 : " + detailVo.getProductDate() + "<br/>");
            }
            if (StringUtils.isNotEmpty(detailVo.getQaStatus())) {
                sb.append("托盘状态 : " + detailVo.getQaStatus() + "<br/>");
            }
            if (StringUtils.isNotEmpty(memo)) {
                sb.append("备注 : " + memo);
            }
            sb.append("</span>");
            index++;
        }
        sb.append("</li>");
        sb.append("</ul>");


        return sb.toString();

    }

    public BaseReturnObj transfer(String barCode,String toLocation){
        BaseReturnObj returnObj = new BaseReturnObj();
        try {

            Transaction.begin();

            Container container = Container.getByBarcode(barCode);

            // TODO: 2018/4/3 判断是否村咋i

            Location fromLoc = container.getLocation();
            Location toLoc = Location.getByLocationNo(toLocation);
            // TODO: 2018/4/3 判断路劲是否通畅

            MCar srm = MCar.getMCarByPosition(fromLoc.getPosition(),fromLoc.getLevel());

            Job job = new Job();

            job.setStatus("1");
            job.setFromLocation(container.getLocation());
            job.setToLocation(toLoc);
            job.setFromStation(srm.getBlockNo());
            job.setMcKey(Mckey.getNext());
            job.setToStation(srm.getBlockNo());
            job.setContainer(container.getBarcode());
            job.setType(AsrsJobType.LOCATIONTOLOCATION);

            HibernateUtil.getCurrentSession().save(job);

            AsrsJob asrsJob = new AsrsJob();

            asrsJob.setStatus("1");
            asrsJob.setStatusDetail("0");
            asrsJob.setBarcode(job.getContainer());
            asrsJob.setMcKey(job.getMcKey());

            asrsJob.setFromLocation(job.getFromLocation().getLocationNo());
            asrsJob.setToLocation(job.getToLocation().getLocationNo());

            asrsJob.setFromStation(job.getFromStation());
            asrsJob.setToStation(job.getToStation());

            asrsJob.setType(AsrsJobType.LOCATIONTOLOCATION);

            HibernateUtil.getCurrentSession().save(asrsJob);


            Transaction.commit();
            returnObj.setSuccess(false);
            returnObj.setMsg("创建移库作业成功，目标货位：" + toLoc.getLocationNo());

        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());
            ex.printStackTrace();

        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());
            ex.printStackTrace();

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
            ex.printStackTrace();

        }
        return returnObj;

        }


    public BaseReturnObj transfer(String palletNo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {

            Transaction.begin();


            Container container = Container.getByBarcode(palletNo);

            Query query = HibernateUtil.getCurrentSession().createQuery("from Location l " +
                    "  where l.position=:po and l.level=:lv and l.actualArea=:area and l.bay =:bay and l.empty=false and l.seq >:seq ");
            query.setParameter("po", container.getLocation().getPosition());
            query.setParameter("lv", container.getLocation().getLevel());
            query.setParameter("area", container.getLocation().getActualArea());
            query.setParameter("seq", container.getLocation().getSeq());
            query.setParameter("bay", container.getLocation().getBay());
            List<Location> banks = query.list();
            if (!banks.isEmpty()) {
                throw new WmsServiceException("通道被堵住");
            }

            Inventory inv = container.getInventories().iterator().next();

            String batchNo = "20" + inv.getLotNum();
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatter("yyyyMMdd");
            Date date = dateTimeFormatter.unformat(batchNo);
            date.setDate(date.getDate() - 30);
            batchNo = dateTimeFormatter.format(date).substring(2);

            Session session = HibernateUtil.getCurrentSession();

            Location toLocation = null;

            Query qq = session.createQuery("select i.container.location.position,i.container.location.bay,i.container.location.level,i.container.location.actualArea from Inventory i where i.skuCode=:skuCode and i.whCode=:whCode" +
                    " and i.container.location.position=:po and i.status='0' group by i.container.location.position,i.container.location.bay,i.container.location.level,i.container.location.actualArea ");
            qq.setParameter("po", container.getLocation().getPosition());
            qq.setParameter("skuCode", inv.getSkuCode());
            qq.setParameter("whCode", inv.getWhCode());

            List<Object[]> objects = qq.list();
            for (Object[] obj : objects) {
                Integer bay = Integer.parseInt(obj[1].toString());
                Integer level = Integer.parseInt(obj[2].toString());
                String area = obj[3].toString();

                if (bay == container.getLocation().getBay() && area.equals(container.getLocation().getActualArea())) {
                    continue;
                }
                //查看排列层是否有出库任务
                Query reseQ = session.createQuery("from Inventory i where i.container.location.bay=:bay and i.container.location.actualArea=:area and i.container.location.level=:lev and i.container.location.position=:po and i.orderNo is not null ");
                reseQ.setParameter("bay", bay);
                reseQ.setParameter("lev", level);
                reseQ.setParameter("area", area);
                reseQ.setParameter("po", container.getLocation().getPosition());

                if (reseQ.list().isEmpty()) {
                    Query invQ = session.createQuery("from Inventory i where i.container.location.bay=:bay and i.container.location.actualArea=:area and i.container.location.level=:lev and i.container.location.position=:po order by i.lotNum asc");
                    invQ.setParameter("bay", bay);
                    invQ.setParameter("lev", level);
                    invQ.setParameter("area", area);
                    invQ.setParameter("po", container.getLocation().getPosition());
                    invQ.setMaxResults(1);
                    Inventory inventory = (Inventory) invQ.uniqueResult();

                    //该批次减去30天，还大于现有库存的批次，pass,小于等于，进来找空货位
                    if (Integer.parseInt(batchNo) < Integer.parseInt(inventory.getLotNum())) {

                        Query locQ = session.createQuery("from Location l where l.bay=:bay and l.level=:lv and l.actualArea=:area and l.position=:po and l.empty=true and l.reserved=false and l.asrsFlag = true and l.putawayRestricted = false order by l.seq");
                        locQ.setParameter("bay", bay);
                        locQ.setParameter("lv", level);
                        locQ.setParameter("area", area);
                        locQ.setParameter("po", container.getLocation().getPosition());
                        locQ.setMaxResults(1);
                        toLocation = (Location) locQ.uniqueResult();
                        //货位不为空，找到可用货位,break出去
                        if (toLocation != null) {
                            break;
                        }
                    }
                }
            }

            if (toLocation == null) {
                throw new WmsServiceException("找不到合适货位");
            }

            MCar srm = MCar.getMCarByPosition(toLocation.getPosition(),toLocation.getLevel());

            Job job = new Job();

            job.setStatus("1");
            job.setFromLocation(container.getLocation());
            job.setToLocation(toLocation);
            job.setFromStation(srm.getBlockNo());
            job.setMcKey(Mckey.getNext());
            job.setToStation(srm.getBlockNo());
            job.setContainer(container.getBarcode());
            job.setType(AsrsJobType.LOCATIONTOLOCATION);

            HibernateUtil.getCurrentSession().save(job);

            AsrsJob asrsJob = new AsrsJob();

            asrsJob.setStatus("1");
            asrsJob.setStatusDetail("0");
            asrsJob.setBarcode(job.getContainer());
            asrsJob.setMcKey(job.getMcKey());

            asrsJob.setFromLocation(job.getFromLocation().getLocationNo());
            asrsJob.setToLocation(job.getToLocation().getLocationNo());

            asrsJob.setFromStation(job.getFromStation());
            asrsJob.setToStation(job.getToStation());

            asrsJob.setType(AsrsJobType.LOCATIONTOLOCATION);

            HibernateUtil.getCurrentSession().save(asrsJob);

            Transaction.commit();
            returnObj.setSuccess(false);
            returnObj.setMsg("创建移库作业成功，目标货位：" + toLocation.getLocationNo());

        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());
            ex.printStackTrace();

        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());
            ex.printStackTrace();

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
            ex.printStackTrace();

        }
        return returnObj;

    }

    public ReturnObj<List<InventoryWarningVo>> inventoryWarning(String type) {
        ReturnObj<List<InventoryWarningVo>> returnObj = new ReturnObj<>();
        List<InventoryWarningVo> vos = new ArrayList<>();
        try {

            Transaction.begin();
            List<Object[]> objects = null;

            if (type.equals("1")) {
                //预警
                Query query = HibernateUtil.getCurrentSession().createSQLQuery("select i.SKUCODE,s.SKU_NAME,i.LOT_NUM,c.BARCODE,l.LOCATIONNO from inventory i,sku s,container c,location l where i.SKUCODE = s.SKU_CODE\n" +
                        " and i.CONTAINERID = c.id and c.LOCATIONID = l.id and to_date(lot_num,'yymmdd') + s.WARNING < sysdate" +
                        " group by  i.SKUCODE,s.SKU_NAME,i.LOT_NUM,c.BARCODE,l.LOCATIONNO order by l.locationno asc ");
                objects = query.list();
            } else {
                //过期
                Query query = HibernateUtil.getCurrentSession().createSQLQuery("select i.SKUCODE,s.SKU_NAME,i.LOT_NUM,c.BARCODE,l.LOCATIONNO from inventory i,sku s,container c,location l where i.SKUCODE = s.SKU_CODE\n" +
                        " and i.CONTAINERID = c.id and c.LOCATIONID = l.id and to_date(lot_num,'yymmdd') + s.SHELF_LIFE < sysdate" +
                        " group by  i.SKUCODE,s.SKU_NAME,i.LOT_NUM,c.BARCODE,l.LOCATIONNO order by l.locationno asc ");
                objects = query.list();
            }

            InventoryWarningVo warningVo;
            int id = 1;
            List<String> containers = new ArrayList<>();
            for (Object[] obj : objects) {
                warningVo = new InventoryWarningVo();
                warningVo.setId(id);
                id++;
                warningVo.setSkuCode(String.valueOf(obj[0]));
                warningVo.setSkuName(String.valueOf(obj[1]));
                warningVo.setLotNum(String.valueOf(obj[2]));
                warningVo.setContainer(String.valueOf(obj[3]));
                warningVo.setLocation(String.valueOf(obj[4]));
                if (containers.indexOf(warningVo.getContainer()) != -1) {
                    continue;
                }
                containers.add(warningVo.getContainer());

                vos.add(warningVo);
            }

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setRes(vos);


        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());
            ex.printStackTrace();

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
            ex.printStackTrace();
        }

        return returnObj;
    }

    public BaseReturnObj frozen(String barcode, String batchNo, String skuCode) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {

            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Inventory.class);
            Criteria conCri = criteria.createCriteria(Inventory.COL_CONTAINER);

            if (StringUtils.isNotEmpty(barcode)) {
                conCri.add(Restrictions.eq(Container.__BARCODE, barcode));
            }

            if (StringUtils.isNotEmpty(batchNo)) {
                criteria.add(Restrictions.eq(Inventory.COL_LOTNUM, batchNo));
            }

            if (StringUtils.isNotEmpty(skuCode)) {
                criteria.add(Restrictions.eq(Inventory.COL_SKUCODE, skuCode));
            }

            List<Inventory> inventories = criteria.list();
            for (Inventory inventory : inventories) {
                inventory.setStatus("1");
                InventoryLog inventoryLog = new InventoryLog();
                inventoryLog.setVolumn(BigDecimal.ZERO);
                inventoryLog.setSkuCode(inventory.getSkuCode());
                inventoryLog.setSkuName(inventory.getSkuName());
                inventoryLog.setLotNum(inventory.getLotNum());
                inventoryLog.setContainer(inventory.getContainer().getBarcode());
                inventoryLog.setQty(inventory.getQty());
                inventoryLog.setWhCode(inventory.getWhCode());
                inventoryLog.setCreateDate(new Date());
                inventoryLog.setFromLocation(inventory.getContainer().getLocation().getLocationNo());
                inventoryLog.setType(InventoryLog.TYPE_FROZEN);

                HibernateUtil.getCurrentSession().save(inventoryLog);
            }

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("修改成功");

        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());
            ex.printStackTrace();

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
            ex.printStackTrace();
        }

        return returnObj;
    }

    public BaseReturnObj realease(String barcode, String batchNo, String skuCode) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {

            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Inventory.class);
            Criteria conCri = criteria.createCriteria(Inventory.COL_CONTAINER);

            if (StringUtils.isNotEmpty(barcode)) {
                conCri.add(Restrictions.eq(Container.__BARCODE, barcode));
            }

            if (StringUtils.isNotEmpty(skuCode)) {
                criteria.add(Restrictions.eq(Inventory.COL_SKUCODE, skuCode));
            }

            if (StringUtils.isNotEmpty(batchNo)) {
                criteria.add(Restrictions.eq(Inventory.COL_LOTNUM, batchNo));
            }

            List<Inventory> inventories = criteria.list();
            for (Inventory inventory : inventories) {
                inventory.setStatus("0");
                InventoryLog inventoryLog = new InventoryLog();
                inventoryLog.setVolumn(BigDecimal.ZERO);
                inventoryLog.setSkuCode(inventory.getSkuCode());
                inventoryLog.setSkuName(inventory.getSkuName());
                inventoryLog.setLotNum(inventory.getLotNum());
                inventoryLog.setContainer(inventory.getContainer().getBarcode());
                inventoryLog.setQty(inventory.getQty());
                inventoryLog.setWhCode(inventory.getWhCode());
                inventoryLog.setFromLocation(inventory.getContainer().getLocation().getLocationNo());
                inventoryLog.setCreateDate(new Date());
                inventoryLog.setType(InventoryLog.TYPE_REALEASE);

                HibernateUtil.getCurrentSession().save(inventoryLog);

            }

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("修改成功");

        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());
            ex.printStackTrace();

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
            ex.printStackTrace();
        }

        return returnObj;
    }

    public ReturnObj<HSSFWorkbook> generateExcelExportFile(SearchInventoryVo searchVo) {
        ReturnObj<HSSFWorkbook> returnObj = new ReturnObj<HSSFWorkbook>();
        try {
            Transaction.begin();


            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Inventory.class);
            Criteria conCri = criteria.createCriteria(Inventory.COL_CONTAINER);

            if (StringUtils.isNotEmpty(searchVo.getWhCode())) {
                criteria.add(Restrictions.eq(Inventory.COL_WHCODE, searchVo.getWhCode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getSkuCode())) {
                criteria.add(Restrictions.eq(Inventory.COL_SKUCODE, searchVo.getSkuCode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getBarcodes())) {
                criteria.add(Restrictions.eq(Inventory.COL_CASEBARCODE, searchVo.getBarcodes()));
            }
            if (StringUtils.isNotEmpty(searchVo.getContainerBarcode())) {
                conCri.add(Restrictions.eq(Container.__BARCODE, searchVo.getContainerBarcode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getLocationNo())) {
                Criteria loCri = conCri.createCriteria(Container.__LOCATION);
                loCri.add(Restrictions.eq(Location.__LOCATIONNO, searchVo.getLocationNo()));
            }

            criteria.addOrder(Order.desc(Inventory.__ID));

            List<Inventory> inventories = criteria.list();

            List<InventoryVo> vos = new ArrayList<InventoryVo>();
            InventoryVo vo;
            for (Inventory inventory : inventories) {

                vo = new InventoryVo();
                vo.setId(inventory.getId());
                vo.setQty(inventory.getQty());
                vo.setPalletNo(inventory.getContainer().getBarcode());
                vo.setWhCode(inventory.getWhCode());
                vo.setItemCode(inventory.getSkuCode());
                vo.setCaseBarCode(inventory.getCaseBarCode());
                vo.setLotNum(inventory.getLotNum());
                vo.setLocationNo(inventory.getContainer().getLocation().getLocationNo());
                vo.setItemName(inventory.getSkuName());
                vos.add(vo);


            }

            HSSFWorkbook hssfWorkbook = ExcelExportUtils.generateExcelFile(generateExcelExportParams(), vos);

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setRes(hssfWorkbook);

        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());
        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }
        return returnObj;

    }

    public List<ExcelExportParam> generateExcelExportParams() {
        List<ExcelExportParam> list = new ArrayList<ExcelExportParam>();
        list.add(new ExcelExportParam("inventoryeExcel.id", "id"));
        list.add(new ExcelExportParam("inventoryeExcel.whCode", "whCode"));
        list.add(new ExcelExportParam("inventoryeExcel.itemCode", "itemCode"));
        list.add(new ExcelExportParam("inventoryeExcel.itemName", "itemName"));
        list.add(new ExcelExportParam("inventoryeExcel.locationNo", "locationNo"));
        list.add(new ExcelExportParam("inventoryeExcel.caseBarCode", "caseBarCode"));
        list.add(new ExcelExportParam("inventoryeExcel.palletNo", "palletNo"));
        list.add(new ExcelExportParam("inventoryeExcel.lotNum", "lotNum"));
        list.add(new ExcelExportParam("inventoryeExcel.qty", "qty"));
        return list;
    }


    public List<ExcelExportParam> generateDailyExcelExportParams() {
        List<ExcelExportParam> list = new ArrayList<ExcelExportParam>();
        list.add(new ExcelExportParam("inventoryeExcel.id", "id"));
        list.add(new ExcelExportParam("inventoryeExcel.whCode", "whCode"));
        list.add(new ExcelExportParam("inventoryeExcel.itemCode", "itemCode"));
        list.add(new ExcelExportParam("inventoryeExcel.itemName", "itemName"));
        list.add(new ExcelExportParam("inventoryeExcel.locationNo", "locationNo"));
        list.add(new ExcelExportParam("inventoryeExcel.palletNo", "palletNo"));
        list.add(new ExcelExportParam("inventoryeExcel.lotNum", "lotNum"));
        list.add(new ExcelExportParam("inventoryeExcel.qty", "qty"));
        list.add(new ExcelExportParam("inventoryeExcel.daily", "daily"));

        return list;
    }

    /**
     * select * from (
     * select count(1) cct, l.BAY,l.LEV,l.POSITION,l.AREA from container c,location l
     * where c.LOCATIONID = l.id and l.POSITION = '3'
     * group by l.BAY,l.LEV,l.POSITION,l.AREA
     * ) t where t.cct<3;
     *
     * @param position
     * @return
     */
    public ReturnObj<List<TransferVo>> searchTransferSuggest(String position) {
        ReturnObj<List<TransferVo>> returnObj = new ReturnObj<List<TransferVo>>();
        try {
            Transaction.begin();
            List<TransferVo> transferVos = new ArrayList<>();
            Query query = HibernateUtil.getCurrentSession().createQuery("select count(1) as ct, location.bay,location.level,location.actualArea " +
                    " from Container where location.position=:po and status ='整托'  group by location.bay,location.level,location.actualArea order by ct asc");
            query.setParameter("po", position);
            List<Object[]> objects = query.list();
            int id = 1;
            for (Object[] obj : objects) {

                BigDecimal count = new BigDecimal(obj[0].toString());
                Integer bay = (Integer) obj[1];
                Integer level = (Integer) obj[2];
                String area = (String) obj[3];
                if (count.intValue() >= 3)
                    continue;


                Query invQ = HibernateUtil.getCurrentSession().createQuery("from Inventory where container.location.bay=:bay and container.location.level=:lv and container.location.actualArea=:area" +
                        " and container.location.position=:po order by container.location.bank desc ").setMaxResults(1);
                invQ.setParameter("bay", bay);
                invQ.setParameter("lv", level);
                invQ.setParameter("area", area);
                invQ.setParameter("po", position);
                Inventory inventory = (Inventory) invQ.uniqueResult();


                String batchNo = "20" + inventory.getLotNum();
                DateTimeFormatter dateTimeFormatter = new DateTimeFormatter("yyyyMMdd");
                Date date = dateTimeFormatter.unformat(batchNo);
                date.setDate(date.getDate() - 30);
                batchNo = dateTimeFormatter.format(date).substring(2);


                Query qq = HibernateUtil.getCurrentSession().createQuery("select i.container.location.position,i.container.location.bay,i.container.location.level,i.container.location.actualArea " +
                        " from Inventory i where i.skuCode=:skuCode and i.container.location.bay !=:bay and i.whCode=:whCode and i.container.location.position=:po  group by i.container.location.position,i.container.location.bay,i.container.location.level,i.container.location.actualArea ");
                qq.setParameter("po", position);
                qq.setParameter("skuCode", inventory.getSkuCode());
                qq.setParameter("whCode", inventory.getWhCode());
                qq.setParameter("bay", bay);
                List<Object[]> qObjects = qq.list();
                int otherQty = 0;
                for (Object[] othLoc : qObjects) {

                    Integer newBay = Integer.parseInt(othLoc[1].toString());
                    Integer newLev = Integer.parseInt(othLoc[2].toString());


                    Query iq = HibernateUtil.getCurrentSession().createQuery("from Inventory i where i.container.location.bay=:bay and i.container.location.actualArea=:area and i.container.location.level=:lev and i.container.location.position=:po order by i.lotNum asc");
                    iq.setParameter("bay", bay);
                    iq.setParameter("lev", level);
                    iq.setParameter("area", area);
                    iq.setParameter("po", position);
                    iq.setMaxResults(1);
                    Inventory iv = (Inventory) iq.uniqueResult();
                    if(Integer.parseInt(batchNo) < Integer.parseInt(iv.getLotNum())){

                        Query otherQ = HibernateUtil.getCurrentSession().createQuery("from Location l  where l.reserved=false and l.empty=true and l.position=:po and l.level=:lv and l.bay=:bay");
                        otherQ.setParameter("po", position);
                        otherQ.setParameter("lv", newLev);
                        otherQ.setParameter("bay", newBay);
                        List<Location> locations = otherQ.list();
                        otherQty = otherQty + locations.size();

                    }

                }

                if (otherQty < count.intValue()) {
                    continue;
                }

                TransferVo vo = new TransferVo();
                vo.setId(id);
                vo.setBay(bay);
                vo.setLevel(level);
                vo.setArea(area);
                vo.setPosition(position);
                transferVos.add(vo);
                id++;

            }


            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setRes(transferVos);

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }

        return returnObj;
    }



    public ReturnObj<HSSFWorkbook> exportInventoLog(SearchInvLogVo searchVo) {
        ReturnObj<HSSFWorkbook> returnObj = new ReturnObj<HSSFWorkbook>();

        try {
            Transaction.begin();
            DateTimeFormatter formatter = new DateTimeFormatter();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(InventoryLog.class);
            if (StringUtils.isNotEmpty(searchVo.getContainerBarcode())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_CONTAINER, searchVo.getContainerBarcode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getFromLocation())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_FROMLOCATION, searchVo.getFromLocation()));
            }
            if (StringUtils.isNotEmpty(searchVo.getToLocation())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_TOLOCATION, searchVo.getToLocation()));
            }
            if (StringUtils.isNotEmpty(searchVo.getSkuCode())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_SKUCODE, searchVo.getSkuCode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getJobType())) {
                if (searchVo.getJobType().equals("03"))
                    searchVo.setJobType("02");
                criteria.add(Restrictions.eq(InventoryLog.COL_TYPE, searchVo.getJobType()));
            }
            if (StringUtils.isNotEmpty(searchVo.getBeginCreateDate())) {
                criteria.add(Restrictions.ge(InventoryLog.COL_CREATEDATE, formatter.unformat(searchVo.getBeginCreateDate() + " 00:00:00")));
            }
            if (StringUtils.isNotEmpty(searchVo.getEndCreateDate())) {
                criteria.add(Restrictions.le(InventoryLog.COL_CREATEDATE, formatter.unformat(searchVo.getEndCreateDate() + " 23:59:59")));
            }
            if (StringUtils.isNotEmpty(searchVo.getOrderNo())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_ORDERNO, searchVo.getOrderNo()));
            }

            criteria.addOrder(Order.desc(InventoryLog.COL_ID));

            List<InventoryLog> list = criteria.list();

            List<InventoryLogVo> vos = new ArrayList<InventoryLogVo>();
            InventoryLogVo vo;
            for (InventoryLog log : list) {
                vo = new InventoryLogVo();
                vo.setContainer(log.getContainer());
                vo.setFromLocation(log.getFromLocation());
                vo.setToLocation(log.getToLocation());
                vo.setQty(log.getQty());
                vo.setWhCode(log.getWhCode());
                vo.setSkuCode(log.getSkuCode());
                vo.setLotNum(log.getLotNum());
                vo.setOrderNo(log.getOrderNo());
                vo.setId(log.getId());
                vo.setSkuName(log.getSkuName());

                vos.add(vo);
            }

            HSSFWorkbook hssfWorkbook = ExcelExportUtils.generateExcelFile(generateLogDailyExcelExportParams(), vos);

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(hssfWorkbook);

        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());
        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }
        return returnObj;
    }

    public List<ExcelExportParam> generateLogDailyExcelExportParams() {
        List<ExcelExportParam> list = new ArrayList<ExcelExportParam>();
        list.add(new ExcelExportParam("inventorylog.id", "id"));
        list.add(new ExcelExportParam("inventorylog.skuCode", "skuCode"));
        list.add(new ExcelExportParam("inventorylog.skuName", "skuName"));
        list.add(new ExcelExportParam("inventorylog.qty", "qty"));
        list.add(new ExcelExportParam("inventorylog.lotNum", "lotNum"));
        list.add(new ExcelExportParam("inventorylog.whCode", "whCode"));
        list.add(new ExcelExportParam("inventorylog.fromLocation", "fromLocation"));
        list.add(new ExcelExportParam("inventorylog.toLocation", "toLocation"));
        list.add(new ExcelExportParam("inventorylog.container", "container"));
        list.add(new ExcelExportParam("inventorylog.orderNo", "orderNo"));
        return list;
    }


}
