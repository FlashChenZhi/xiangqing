package com.wms.service;

import com.util.common.HttpMessage;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Job;
import com.wms.domain.Location;
import com.wms.domain.Message;
import com.wms.domain.blocks.*;
import com.wms.vo.*;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2016/12/28.
 */
@Service
public class LocationService {

    public ReturnObj<List<AutoLocationListVo>> list() {
        ReturnObj<List<AutoLocationListVo>> returnObj = new ReturnObj<List<AutoLocationListVo>>();
        try {
            Transaction.begin();

            Query locQ = HibernateUtil.getCurrentSession().createQuery("from Location loc where asrsFlag = true order by loc.level,loc.bank, loc.bay asc");
            List<Location> locations = locQ.list();

            Query invQ = HibernateUtil.getCurrentSession().createQuery("select inv.container.location.locationNo,inv.container.barcode,inv.sku.skuCode,sum(inv.qty),inv.batchNo from Inventory inv" +
                    " group by inv.container.location.locationNo,inv.container.barcode,inv.sku.skuCode,inv.batchNo" +
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

            Query emptyPalletQ = HibernateUtil.getCurrentSession().createQuery("select con.location.locationNo,con.barcode from Container con where con.location.asrsFlag =true " +
                    " and not exists (select 1 from Inventory inv where inv.container.id = con.id)" +
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

            Query asrsJobQ = HibernateUtil.getCurrentSession().createQuery("from Job aj where " +
                    " exists (select 1 from Location loc where aj.toLocation.locationNo = loc.locationNo and loc.asrsFlag = true )" +
                    " order by aj.id asc ");

            List<Job> asrsJobs = asrsJobQ.list();

            Map<String, String> asrsJobToLocationMap = new HashMap<String, String>();
            for (Job asrsJob : asrsJobs) {
                String str = asrsJobToLocationMap.get(asrsJob.getToLocation());
                if (str == null) {
                    asrsJobToLocationMap.put(asrsJob.getToLocation().getLocationNo(), asrsJob.getMcKey());
                } else {
                    str = str + "、" + asrsJob.getMcKey();
                    asrsJobToLocationMap.put(asrsJob.getToLocation().getLocationNo(), str);
                }
            }

            List<AutoLocationListVo> list = new ArrayList<AutoLocationListVo>();
            for (Location location : locations) {
                AutoLocationListVo vo = new AutoLocationListVo();
                vo.setLocationNo(location.getLocationNo());
                vo.setBank(location.getBank());

                boolean existInvFlag = false;
                List<Object[]> invResList = invMap.get(location.getLocationNo());
                if (invResList != null) {
                    for (Object[] invRes : invResList) {
                        String containerBarcode = invRes[1] == null ? "" : (String) invRes[1];
                        String skuCode = invRes[2] == null ? "" : (String) invRes[2];
                        BigDecimal qty = invRes[3] == null ? BigDecimal.ZERO : BigDecimal.valueOf((Double) invRes[3]);
                        String batchNo = invRes[4] == null ? "" : (String) invRes[4];
                        AutoLocationListDetailVo detailVo = new AutoLocationListDetailVo();
                        detailVo.setContainerBarcode(containerBarcode);
                        detailVo.setSkuCode(skuCode);
                        detailVo.setQty(qty.toString());
                        detailVo.setBatchNo(batchNo);
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

                if (asrsJobToLocationMap.containsKey(location.getLocationNo()) && !existInvFlag) {
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

                if (location.isAbnormal() && location.isRetrievalRestricted() && location.isPutawayRestricted()) {
                    vo.setStatus(AutoLocationListVo.SYSTEM_UN_AVAIL);
                }

                if (emptyPalletMap.containsKey(vo.getLocationNo())) {
                    vo.setStatus(AutoLocationListVo.EMPTY_PALLET);
                    vo.setRemark(getRemark(location, "空托盘"));
                }
                if (location.isEmpty() && location.getReserved()) {
                    vo.setStatus(AutoLocationListVo.RESTRICTED);
                    vo.setRemark(getRemark(location, "限制使用"));
                }

                list.add(vo);
            }

            Criteria blockCri = HibernateUtil.getCurrentSession().createCriteria(Block.class);
            List<Block> blocks = blockCri.list();

            for (Block block : blocks) {
                AutoLocationListVo vo = new AutoLocationListVo();
                vo.setMachineNo(block.getBlockNo());
                if (block instanceof SCar) {
                    SCar scar = (SCar) block;
                    if (StringUtils.isNotEmpty(scar.getMcKey()) || StringUtils.isNotEmpty(scar.getReservedMcKey())) {
                        vo.setMckey(StringUtils.isNotEmpty(scar.getMcKey()) ? scar.getMcKey() : scar.getReservedMcKey());
                        vo.setRemark(getBlockRemark(block, vo.getMckey()));
                    }
                    if (StringUtils.isNotEmpty(scar.getOnMCar())) {
                        int bay = scar.getBay();
                        int level = scar.getLevel();
                        String locationNo = "0" + 8 + "0" + bay + "0" + level;
                        vo.setLocationNo(locationNo);
                        vo.setBank(8);
                        vo.setStatus(AutoLocationListVo.STATUS_SCAR);
                        list.add(vo);
                    } else {
                        int bay = scar.getBay();
                        int level = scar.getLevel();
                        String locationNo = "0" + 7 + "0" + bay + "0" + level;
                        vo.setLocationNo(locationNo);
                        vo.setBank(7);
                        vo.setStatus(AutoLocationListVo.STATUS_SCAR);
                        list.add(vo);
                    }
                } else if (block instanceof StationBlock) {
                    StationBlock station = (StationBlock) block;
                    if (station.getBlockNo().equals("0001")) {
                        String locationNo = "0001";
                        vo.setLocationNo(locationNo);
                        vo.setStatus(AutoLocationListVo.STATUS_STATION_01);
                        list.add(vo);

                    } else if (station.getBlockNo().equals("0003")) {
                        String locationNo = "0003";
                        vo.setLocationNo(locationNo);
                        vo.setStatus(AutoLocationListVo.STATUS_STATION_02);
                        list.add(vo);

                    }
                } else if (block instanceof Crane) {
                    Crane crane = (Crane) block;
                    if (crane.getLevel() == 0) {
                        vo.setLocationNo("ML0" + crane.getBay());
                    } else {
                        String locationNo = "ML01" + crane.getBay() + crane.getLevel();
                        vo.setLocationNo(locationNo);
                    }
                    vo.setStatus(AutoLocationListVo.STATUS_ML01);
                    list.add(vo);
                }
//                else if (block instanceof Dock) {
//                    Dock dock = (Dock) block;
//                    String locationNo = "0002";
//                    vo.setLocationNo(locationNo);
//                    vo.setStatus(AutoLocationListVo.STATUS_DOCK);
//                    list.add(vo);
//                }
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(list);
        } catch (JDBCConnectionException ex) {
            ex.printStackTrace();
            returnObj.setSuccess(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            Transaction.rollback();
            returnObj.setSuccess(false);
        }
        return returnObj;
    }

    private String getBlockRemark(Block block, String mckey) {
        StringBuffer sb = new StringBuffer();
        sb.append("<ul style=\"width:400px;\" >");
        sb.append("<li>");
        sb.append("<span class=\"b\">");
        sb.append("机器号 : " + block.getBlockNo() + "<br/>");
        sb.append("设备状态: " + block.getStatus() + "<br/>");
        sb.append("mckey : " + mckey);
        sb.append("</span>");
        sb.append("</li>");
        sb.append("</ul>");


        return sb.toString();

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
                sb.append("货位号 : " + vo.getLocationNo() + "<br/>");
            } else {
                sb.append("<br/>");
            }
            sb.append("容器号 : " + detailVo.getContainerBarcode() + "<br/>");
            sb.append("SKU代码 : " + detailVo.getSkuCode() + "<br/>");
            sb.append("库存数量 : " + detailVo.getQty() + "<br/>");
            sb.append("批次：" + detailVo.getBatchNo() + "<br/>");
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

    /**
     * 设备切离
     *
     * @param blockNo
     * @return
     */
    public BaseReturnObj offline(String blockNo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Message message = new Message();
            message.setMachineNo(blockNo);
            message.setType(Message.TYPE_OFFLINE);
            message.setStatus(Message.STATUS_WAIT);
            message.setCreateDate(new Date());
            HibernateUtil.getCurrentSession().save(message);

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("设备已切离");
        } catch (Exception e) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统异常");
        }

        return returnObj;
    }


    /**
     * 异常解除
     *
     * @param blockNo
     * @return
     */
    public BaseReturnObj removeAbnormal(String blockNo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Message message = new Message();
            Block block = Block.getByBlockNo(blockNo);
            message.setMachineNo(block.getPlcName());
            message.setType(Message.TYPE_REMOVE_ABNORMAL);
            message.setStatus(Message.STATUS_WAIT);
            message.setCreateDate(new Date());
            HibernateUtil.getCurrentSession().save(message);

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("设备异常解除");

        } catch (Exception e) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统异常");
        }

        return returnObj;
    }

    /**
     * 清除数据
     *
     * @return
     */
    public BaseReturnObj clearData(String blockNo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("状态已经重置");

        } catch (Exception e) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统异常");
        }

        return returnObj;
    }

    /**
     * 设备恢复
     *
     * @param blockNo
     * @return
     */
    public BaseReturnObj onLine(String blockNo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Message message = new Message();
            message.setMachineNo(blockNo);
            message.setType(Message.TYPE_ONLINE);
            message.setStatus(Message.STATUS_WAIT);
            message.setCreateDate(new Date());
            HibernateUtil.getCurrentSession().save(message);
            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("设备已恢复");

        } catch (Exception e) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统异常");
        }

        return returnObj;
    }

    /**
     * 删除数据
     *
     * @param blockNo
     * @return
     */
    public BaseReturnObj deleteDate(String blockNo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Message message = new Message();
            message.setMachineNo(blockNo);
            message.setType(Message.TYPE_DELDATE);
            message.setStatus(Message.STATUS_WAIT);
            message.setCreateDate(new Date());
            HibernateUtil.getCurrentSession().save(message);
            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("任务已发送");

        } catch (Exception e) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统异常");
        }

        return returnObj;

    }

    /**
     * 强制完成
     *
     * @param blockNo
     * @return
     */
    public BaseReturnObj finish(String blockNo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Message message = new Message();
            message.setMachineNo(blockNo);
            message.setType(Message.TYPE_FINISH);
            message.setStatus(Message.STATUS_WAIT);
            message.setCreateDate(new Date());
            HibernateUtil.getCurrentSession().save(message);
            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("任务已发送");

        } catch (Exception e) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统异常");
        }

        return returnObj;

    }

    /**
     * 母车 装载小车
     *
     * @param blockNo
     * @param nextBlock
     * @return
     */
    public BaseReturnObj loadCar(String blockNo, String nextBlock) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Message message = new Message();
            message.setMachineNo(blockNo);
            message.setType(Message.TYPE_LOADCAR);
            message.setNextBlock(nextBlock);
            message.setStatus(Message.STATUS_WAIT);
            message.setCreateDate(new Date());
            HibernateUtil.getCurrentSession().save(message);
            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("任务已发送");

        } catch (Exception e) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统异常");
        }

        return returnObj;
    }

    /**
     * 上车
     *
     * @param blockNo
     * @param nextBlock
     * @return
     */
    public BaseReturnObj onCar(String blockNo, String nextBlock) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Block nextB = Block.getByBlockNo(nextBlock);

            if (nextB == null) {
                Transaction.rollback();
                returnObj.setSuccess(false);
                returnObj.setMsg("目标设备不能为空");
            }

            Message message = new Message();
            message.setMachineNo(blockNo);
            message.setType(Message.TYPE_ONCAR);
            message.setStatus(Message.STATUS_WAIT);
            message.setNextBlock(nextBlock);
            message.setCreateDate(new Date());
            HibernateUtil.getCurrentSession().save(message);
            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("任务已发送");

        } catch (Exception e) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统异常");
        }

        return returnObj;

    }

    public HttpMessage searchLocation(int currentPage, String locationNo, String bank, String bay, String level) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();
            Criteria cri = session.createCriteria(Location.class);
            if (StringUtils.isNotEmpty(locationNo)) {
                cri.add(Restrictions.eq("locationNo", locationNo));
            }
            if (StringUtils.isNotEmpty(bank)) {
                cri.add(Restrictions.eq("bank", Integer.parseInt(bank)));
            }
            if (StringUtils.isNotEmpty(bay)) {
                cri.add(Restrictions.eq("bay", Integer.parseInt(bay)));
            }
            if (StringUtils.isNotEmpty(level)) {
                cri.add(Restrictions.eq("level", Integer.parseInt(level)));
            }
            Long count = (Long) cri.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            cri.setProjection(null);
            cri.setFirstResult((currentPage - 1) * 10);
            cri.setMaxResults(10);
            List<Location> locations = cri.list();
            List<LocationVo> list = new ArrayList<LocationVo>();
            LocationVo locationVo = null;
            for (Location location : locations) {
                locationVo = new LocationVo();
                locationVo.setBank(location.getBank() + "");
                locationVo.setBay(location.getBay() + "");
                locationVo.setLevel(location.getLevel() + "");
                locationVo.setLocationNo(location.getLocationNo());
                locationVo.setEmptyFlag(location.isEmpty());
                locationVo.setReserved(location.getReserved());
                list.add(locationVo);
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

    public HttpMessage frozenLocation(String locationNo, boolean b) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from Location where locationNo=:locationNo");
            query.setParameter("locationNo", locationNo);
            query.setMaxResults(1);
            Location location = (Location) query.uniqueResult();
            location.setReserved(b);
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

    public HttpMessage changeLocation(String locationNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from Location where locationNo=:locationNo");
            query.setParameter("locationNo", locationNo);
            query.setMaxResults(1);
            Location location = (Location) query.uniqueResult();
            if (location.isEmpty()) {
                location.setEmpty(false);
            } else {
                location.setEmpty(true);
            }
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

    public HttpMessage chagneLocationType(String locationNo, String skuType) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from Location where locationNo=:locationNo");
            query.setParameter("locationNo", locationNo);
            query.setMaxResults(1);
            Location location = (Location) query.uniqueResult();
            location.setSkuType(skuType);
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

    public HttpMessage searchFirstLocation(int currentPage, String locationNo, String bay, String level, String skuType) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();
            Criteria cri = session.createCriteria(Location.class);
            cri.add(Restrictions.in("bank", new Integer[]{1, 12}));
            if (StringUtils.isNotEmpty(locationNo)) {
                cri.add(Restrictions.eq("locationNo", locationNo));
            }
            if (StringUtils.isNotEmpty(bay)) {
                cri.add(Restrictions.eq("bay", Integer.parseInt(bay)));
            }
            if (StringUtils.isNotEmpty(level)) {
                cri.add(Restrictions.eq("level", Integer.parseInt(level)));
            }
            if (StringUtils.isNotEmpty(skuType)) {
                cri.add(Restrictions.eq("skuType", skuType));
            }
            Long count = (Long) cri.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            cri.setProjection(null);
            cri.setFirstResult((currentPage - 1) * 10);
            cri.setMaxResults(10);
            List<Location> locations = cri.list();
            List<LocationVo> list = new ArrayList<LocationVo>();
            LocationVo locationVo = null;
            for (Location location : locations) {
                locationVo = new LocationVo();
                locationVo.setBank(location.getBank() + "");
                locationVo.setBay(location.getBay() + "");
                locationVo.setLevel(location.getLevel() + "");
                locationVo.setLocationNo(location.getLocationNo());
                locationVo.setEmptyFlag(location.isEmpty());
                locationVo.setReserved(location.getReserved());
                locationVo.setSkuType(location.getSkuType());
                list.add(locationVo);
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
}
