package com.order.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.order.vo.*;
import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.pages.GridPages;
import com.wms.domain.*;
import com.wms.domain.blocks.MCar;
import com.wms.domain.blocks.StationBlock;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by van on 2018/1/15.
 */
@Service
public class AsrsService {

    public PagerReturnObj<List<AsrsVo>> searchAsrsjob(SearchAsrsVo searchAsrsVo, GridPages pages) {
        PagerReturnObj<List<AsrsVo>> returnObj = new PagerReturnObj<List<AsrsVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Job.class);

            if (StringUtils.isNotBlank(searchAsrsVo.getType())) {
                criteria.add(Restrictions.eq(Job.__TYPE, searchAsrsVo.getType()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getBarcode())) {
                criteria.add(Restrictions.eq(Job.__CONTAINER, searchAsrsVo.getBarcode()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getFromLocation())) {
                Criteria locCri = criteria.createCriteria(Job.__FROMLOCATION);
                locCri.add(Restrictions.eq(Location.__LOCATIONNO, searchAsrsVo.getFromLocation()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getToLocation())) {
                Criteria locCri = criteria.createCriteria(Job.__TOLOCATION);
                locCri.add(Restrictions.eq(Location.__LOCATIONNO, searchAsrsVo.getToLocation()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getFromStation())) {
                criteria.add(Restrictions.eq(Job.__FROMSTATION, searchAsrsVo.getFromStation()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getToStation())) {
                criteria.add(Restrictions.eq(Job.__TOSTATION, searchAsrsVo.getToStation()));
            }


            criteria.addOrder(Order.desc(Job.__ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<Job> list = criteria.list();

            List<AsrsVo> vos = new ArrayList<AsrsVo>(pages.getPageSize());
            AsrsVo vo;
            for (Job job : list) {
                vo = new AsrsVo();
                vo.setId(job.getId());
                vo.setType(job.getType());
                vo.setStatus(job.getStatus());
                vo.setMcKey(job.getMcKey());
                vo.setCreateDate(DateFormat.format(job.getCreateDate(), DateFormat.YYYYMMDDHHMMSS));
                vo.setFromStation(job.getFromStation());
                vo.setToStation(job.getToStation());
                if (job.getFromLocation() != null)
                    vo.setFromLocation(job.getFromLocation().getLocationNo());
                if (job.getToLocation() != null)
                    vo.setToLocation(job.getToLocation().getLocationNo());
                vo.setBarCode(job.getContainer());
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

    public PagerReturnObj<List<AsrsLogVo>> searchAsrsjobLog(SearchAsrsLogVo searchAsrsVo, GridPages pages) {
        PagerReturnObj<List<AsrsLogVo>> returnObj = new PagerReturnObj<List<AsrsLogVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(JobLog.class);

            if (StringUtils.isNotBlank(searchAsrsVo.getType())) {
                criteria.add(Restrictions.eq(JobLog.__TYPE, searchAsrsVo.getType()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getBarcode())) {
                criteria.add(Restrictions.eq(JobLog.__CONTAINER, searchAsrsVo.getBarcode()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getFromLocation())) {
                criteria.add(Restrictions.eq(JobLog.__FROMLOCATIONNO, searchAsrsVo.getBarcode()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getToLocation())) {
                criteria.add(Restrictions.eq(JobLog.__TOLOCATIONNO, searchAsrsVo.getBarcode()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getFromStation())) {
                criteria.add(Restrictions.eq(JobLog.__FROMSTATION, searchAsrsVo.getFromStation()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getToStation())) {
                criteria.add(Restrictions.eq(JobLog.__TOSTATION, searchAsrsVo.getToStation()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getBeginDate())) {
                DateTimeFormatter formatter = new DateTimeFormatter("yyyy-MM-dd");
                criteria.add(Restrictions.ge(JobLog.__CREATEDATE, formatter.unformat(searchAsrsVo.getBeginDate())));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getEndDate())) {
                DateTimeFormatter formatter = new DateTimeFormatter("yyyy-MM-dd");
                criteria.add(Restrictions.le(JobLog.__CREATEDATE, formatter.unformat(searchAsrsVo.getEndDate())));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getOrderNo())) {
                criteria.add(Restrictions.eq(JobLog.__ORDERNO, searchAsrsVo.getOrderNo()));
            }

            criteria.addOrder(Order.desc(JobLog.COL_ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<JobLog> list = criteria.list();

            List<AsrsLogVo> vos = new ArrayList<AsrsLogVo>(pages.getPageSize());
            AsrsLogVo vo;
            for (JobLog job : list) {
                vo = new AsrsLogVo();
                vo.setId(job.getId());
                vo.setType(job.getType());
                vo.setStatus(job.getStatus());
                vo.setMcKey(job.getMckey());
                vo.setCreateDate(DateFormat.format(job.getCreateDate(), DateFormat.YYYYMMDDHHMMSS));
                vo.setFromStation(job.getFromStation());
                vo.setToStation(job.getToStation());
                vo.setFromLocation(job.getFromLocation());
                vo.setToLocation(job.getToLocation());
                vo.setBarCode(job.getContainer());
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


    public PagerReturnObj<List<SystemLogVo>> searchSystemLog(SearchAsrsLogVo searchVo, GridPages pages) {
        PagerReturnObj<List<SystemLogVo>> returnObj = new PagerReturnObj<List<SystemLogVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(SystemLog.class);


            criteria.addOrder(Order.desc(JobLog.COL_ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<SystemLog> list = criteria.list();

            List<SystemLogVo> vos = new ArrayList<SystemLogVo>(pages.getPageSize());
            SystemLogVo vo;
            for (SystemLog job : list) {
                vo = new SystemLogVo();
                vo.setId(job.getId());
                vo.setMessage(job.getMessage());
                vo.setType(job.getType());
                vo.setCreateDate(DateFormat.format(job.getCreateDate(), DateFormat.YYYYMMDDHHMMSS));
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

    public BaseReturnObj delete(String mcKey) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {

            Transaction.begin();

            Job job = Job.getByMcKey(mcKey);

            if (job != null) {
                job.asrsCancel();
            }

            if (job.getType().equals(AsrsJobType.RETRIEVAL)) {
                Query query = HibernateUtil.getCurrentSession().createQuery("update Inventory set orderNo=null where orderNo=:orderNo ");
                query.setParameter("orderNo", job.getOrderNo());
                query.executeUpdate();
            }

            Location location = job.getFromLocation();
            if (location != null) {
                location.setReserved(false);
            }

            Location toLocation = job.getToLocation();
            if (toLocation != null) {
                toLocation.setReserved(false);
            }


            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("删除成功");

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

    public BaseReturnObj finish(String mcKey) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {

            Transaction.begin();

            Job job = Job.getByMcKey(mcKey);
            job.setStatus(AsrsJobStatus.DONE);

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("删除成功");

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

    public BaseReturnObj exceptionRetrieval(String locationNo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {

            Transaction.begin();


            Location location = Location.getByLocationNo(locationNo);

            Query query = HibernateUtil.getCurrentSession().createQuery("from Location where (reserved=true or empty=false) and position=:po and level=:lv and bay =:bay and actualArea=:area and seq >:seq");
            query.setParameter("po", location.getPosition());
            query.setParameter("lv", location.getLevel());
            query.setParameter("bay", location.getBay());
            query.setParameter("area", location.getActualArea());
            query.setParameter("seq", location.getSeq());
            List<Location> locationList = query.list();
            if (!locationList.isEmpty()) {
                throw new Exception("出库路劲不通");
            }
//
//            MCar srm = MCar.getMCarByPosition(location.getPosition(),location.getLevel());
//            Configuration configuration = Configuration.getConfig(srm.getBlockNo());
//
//            AsrsJob asrsJob = new AsrsJob();
//            asrsJob.setBarcode("TEMP0001");
//            asrsJob.setFromLocation(locationNo);
//            asrsJob.setMcKey(Mckey.getNext());
//            asrsJob.setStatus("1");
//            asrsJob.setStatusDetail("0");
//            asrsJob.setPriority(1);
//            asrsJob.setSendReport(false);
//            asrsJob.setWmsMckey(asrsJob.getMcKey());
//            asrsJob.setIndicating(false);
//            StationBlock stationBlock = StationBlock.getByStationNo(configuration.getValue());
//            asrsJob.setToStation(stationBlock.getBlockNo());
//            asrsJob.setFromStation(srm.getBlockNo());
//            asrsJob.setType(AsrsJobType.RETRIEVAL);
//            asrsJob.setWareHouse("1");
//
//            HibernateUtil.getCurrentSession().save(asrsJob);

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

    /**
     * 重复存放
     *
     * @param mckey
     * @return
     */
    public BaseReturnObj duplicatedStorage(String mckey) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {

            Transaction.begin();
//
//            Job job = Job.getByMcKey(mckey);
//            Container container = Container.getByBarcode(job.getContainer());
//            Location oldLocation = job.getToLocation();
//            Inventory inventory = container.getInventories().iterator().next();
//
//
//            Location newLocation = Location.duplicatedStorageLocation(inventory.getSkuCode(), inventory.getLotNum(), oldLocation.getPosition(), inventory.getWhCode());
//            if (newLocation == null) {
//                throw new WmsServiceException("找不到合适的存储货位");
//            }
//
//            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
//
//            job.setToLocation(newLocation);
//            asrsJob.setToLocation(newLocation.getLocationNo());
//
//            newLocation.setReserved(true);
//            oldLocation.setReserved(true);

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("重新分配货位");

        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (WmsServiceException ex) {
            ex.printStackTrace();
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

    public ReturnObj<List<StatisticVo>> statistic(String beginDate, String endDate, String jobType) {
        ReturnObj<List<StatisticVo>> returnObj = new ReturnObj<List<StatisticVo>>();
        try {

            Transaction.begin();
            List<StatisticVo> statisticVos = new ArrayList<>();

            if (StringUtils.isNotBlank(jobType)) {
                Long palletQty = getPalletQTy(beginDate, endDate, jobType);
                BigDecimal volumn = getVolumn(beginDate, endDate, jobType);
                StatisticVo statisticVo = new StatisticVo();
                statisticVo.setType(jobType.equals(AsrsJobType.PUTAWAY) ? "入库" : "出库");
                statisticVo.setQty(new BigDecimal(palletQty));
                statisticVo.setVolumn(volumn);
                statisticVos.add(statisticVo);

            } else {

                Long putawayPaleltQty = getPalletQTy(beginDate, endDate, AsrsJobType.PUTAWAY);
                Long retrievalPalletQty = getPalletQTy(beginDate, endDate, AsrsJobType.RETRIEVAL);

                BigDecimal putawayVolumn = getVolumn(beginDate, endDate, AsrsJobType.PUTAWAY);
                BigDecimal retrievalVolumn = getVolumn(beginDate, endDate, AsrsJobType.RETRIEVAL);

                StatisticVo statisticVo = new StatisticVo();
                statisticVo.setType("入库");
                statisticVo.setQty(new BigDecimal(putawayPaleltQty));
                statisticVo.setVolumn(putawayVolumn);
                statisticVos.add(statisticVo);

                StatisticVo retirevalVo = new StatisticVo();
                retirevalVo.setType("出库");
                retirevalVo.setQty(new BigDecimal(retrievalPalletQty));
                retirevalVo.setVolumn(retrievalVolumn);
                statisticVos.add(retirevalVo);

            }

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setRes(statisticVos);

        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (WmsServiceException ex) {
            ex.printStackTrace();
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

    private Long getPalletQTy(String beginDate, String endDate, String jobType) {

        Criteria jobLogCri = HibernateUtil.getCurrentSession().createCriteria(JobLog.class);
        if (StringUtils.isNotBlank(beginDate)) {
            DateTimeFormatter formatter = new DateTimeFormatter("yyyy-MM-dd");
            jobLogCri.add(Restrictions.ge(JobLog.__CREATEDATE, formatter.unformat(beginDate)));
        }

        if (StringUtils.isNotBlank(endDate)) {
            DateTimeFormatter formatter = new DateTimeFormatter();
            jobLogCri.add(Restrictions.le(JobLog.__CREATEDATE, formatter.unformat(endDate + " 23:59:59")));
        }

        jobLogCri.add(Restrictions.eq(JobLog.__TYPE, jobType));


        Long palletQty = (Long) jobLogCri.setProjection(Projections.rowCount()).uniqueResult();

        return palletQty;

    }

    private BigDecimal getVolumn(String beginDate, String endDate, String jobType) {
        Query query = HibernateUtil.getCurrentSession().createQuery("select sum(volumn) from InventoryLog where createDate>=:begingDate and createDate<=:endDate and type =:jobType ");
        DateTimeFormatter formatter = new DateTimeFormatter("yyyy-MM-dd");
        DateTimeFormatter endformatter = new DateTimeFormatter();
        query.setParameter("begingDate", formatter.unformat(beginDate));
        query.setParameter("endDate", endformatter.unformat(endDate + " 23:59:59"));
        if (jobType.equals("03"))
            jobType = "02";
        query.setParameter("jobType", jobType);
        BigDecimal qty = (BigDecimal) query.uniqueResult();
        return qty;
    }

}
