package com.web.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.msgProc.Msg35Proc;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.*;
import com.asrs.message.*;
import com.thread.blocks.*;
import com.thread.utils.MsgSender;
import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.web.vo.*;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by van on 2017/5/10.
 */
@Service
public class WebService {

    public HttpMessage searchBlock(String blockNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();
            Criteria cri = session.createCriteria(Block.class);
            if (StringUtils.isNotEmpty(blockNo)) {
                cri.add(Restrictions.eq("blockNo", blockNo));
            }
            Long count = (Long) cri.setProjection(Projections.rowCount()).uniqueResult();
            cri.setProjection(null);
            List<Block> blocks = cri.list();
            List<BlockVo> list = new ArrayList<BlockVo>();
            BlockVo blockVo = null;
            for (Block block : blocks) {
                blockVo = new BlockVo();
                blockVo.setBlockNo(block.getBlockNo());
                blockVo.setMcKey(block.getMcKey());
                blockVo.setReservMcKey(block.getReservedMcKey());
                blockVo.setWaitResponse(block.isWaitingResponse());
                blockVo.setStatus(block.getStatus());
                blockVo.setError(block.getError());
                if (block instanceof MCar) {
                    blockVo.setsCarNo(((MCar) block).getsCarBlockNo());
                    blockVo.setBay(((MCar) block).getBay());
                    blockVo.setLevel(((MCar) block).getLevel());
                }
                if (block instanceof SCar) {
                    blockVo.setmCarNo(((SCar) block).getOnMCar());
                    blockVo.setPower(((SCar) block).getPower());
                    blockVo.setBank(((SCar) block).getBank());
                    blockVo.setBay(((SCar) block).getBay());
                    blockVo.setLevel(((SCar) block).getLevel());
                }
                if (block instanceof Srm) {
                    blockVo.setsCarNo(((Srm) block).getsCarBlockNo());
                    blockVo.setBay(((Srm) block).getBay());
                    blockVo.setLevel(((Srm) block).getLevel());
                }
                list.add(blockVo);
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
            httpMessage.setMsg("服务器错误");
            e.printStackTrace();
        }
        return httpMessage;
    }

    public HttpMessage asrsJobQuery(int currentPage,AsrsJob asrsJob321) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Criteria criteria = session.createCriteria(AsrsJob.class);
            //获取总行数
            if (StringUtils.isNotEmpty(asrsJob321.getType())) {
                criteria.add(Restrictions.eq(AsrsJob.__TYPE, asrsJob321.getType()));
            }
            Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            criteria.addOrder(Order.asc(AsrsJob._GENERATETIME));

//            获取分页数据
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
                if(StringUtils.isNotEmpty(job.getGenerateTime())){
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
                    String generateTime = format.format(job.getGenerateTime());
                    onlineTaskVo.setGenerateTime(generateTime);
                }
                if(!"07".equals(job.getType())&&StringUtils.isNotEmpty(job.getBarcode().trim())){
                    Inventory inventory=(Inventory)session.createQuery("from Inventory i where i.skuCode not in ('ktp','yl') and  i.container.barcode=:barcode")
                            .setString("barcode",job.getBarcode()).setMaxResults(1).uniqueResult();
                    if(inventory!=null){
                        Inventory inventory11=(Inventory)session.createQuery("from Inventory i where  i.container.barcode=:barcode")
                                .setString("barcode",job.getBarcode()).setMaxResults(1).uniqueResult();
                        if(StringUtils.isNotEmpty(inventory11.getSkuCode())){
                            Sku sku=(Sku)session.createQuery("from Sku where skuCode=:skuCode")
                                    .setString("skuCode",inventory11.getSkuCode()).setMaxResults(1).uniqueResult();
                            if(StringUtils.isNotEmpty(sku.getSkuSpec())){
                                onlineTaskVo.setSkuSpec(sku.getSkuSpec());
                                onlineTaskVo.setSkuCode(sku.getSkuCode());
                            }
                        }
                    }
                }
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


    public HttpMessage searchMessage(int currentPage, String mcKey ,String machineNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();
            Criteria cri = session.createCriteria(WcsMessage.class);
            if (StringUtils.isNotEmpty(mcKey)) {
                cri.add(Restrictions.eq("mcKey", mcKey));
            }
            if (StringUtils.isNotEmpty(machineNo)) {
                cri.add(Restrictions.eq("machineNo", machineNo));
            }
            Long count = (Long) cri.setProjection(Projections.rowCount()).uniqueResult();
            cri.setProjection(null);
            cri.setFirstResult((currentPage - 1) * 10);
            cri.setMaxResults(10);
            cri.addOrder(Order.desc("id"));

            List<WcsMessage> msg03s = cri.list();

            List<Msg03Vo> list = new ArrayList<Msg03Vo>();
            Msg03Vo msg03Vo = null;
            for (WcsMessage msg : msg03s) {
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

    public HttpMessage searchMsgLog(int currentPage, String type, String beginDate, String endDate) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();
            Criteria cri = session.createCriteria(MessageLog.class);
            if (StringUtils.isNotEmpty(type)) {
                cri.add(Restrictions.eq(MessageLog.__MESSAGETYPE, type));
            }
            if (StringUtils.isNotEmpty(beginDate)) {
                DateTimeFormatter formatter = new DateTimeFormatter();
                cri.add(Restrictions.ge(MessageLog.__CREATEDATE, formatter.unformat(beginDate)));
            }

            if (StringUtils.isNotEmpty(endDate)) {
                DateTimeFormatter formatter = new DateTimeFormatter();
                cri.add(Restrictions.le(MessageLog.__CREATEDATE, formatter.unformat(endDate)));
            }

            Long count = (Long) cri.setProjection(Projections.rowCount()).uniqueResult();
            cri.setProjection(null);
            cri.setFirstResult((currentPage - 1) * 10);
            cri.setMaxResults(10);

            cri.addOrder(Order.desc("id"));

            List<MessageLog> msg03s = cri.list();


            List<MessageLogVo> vos = new ArrayList<MessageLogVo>();

            MessageLogVo vo = null;
            for (MessageLog log : msg03s) {
                vo = new MessageLogVo();
                vo.setId(log.getId());
                vo.setMsg(log.getMsg());
                vo.setType(log.getType());
                vo.setCreateDate(DateFormat.format(log.getCreateDate(), DateFormat.YYYYMMDDHHMMSS));
                vos.add(vo);
            }

            map.clear();
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
        }
        return httpMessage;
    }
    public HttpMessage searchSysLog(int currentPage, String type, String beginDate, String endDate) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();
            Criteria cri = session.createCriteria(SystemLog.class);
            if (StringUtils.isNotEmpty(type)) {
                cri.add(Restrictions.eq(SystemLog.__MESSAGETYPE, type));
            }
            if (StringUtils.isNotEmpty(beginDate)) {
                DateTimeFormatter formatter = new DateTimeFormatter();
                cri.add(Restrictions.ge(SystemLog.__CREATEDATE, formatter.unformat(beginDate)));
            }

            if (StringUtils.isNotEmpty(endDate)) {
                DateTimeFormatter formatter = new DateTimeFormatter();
                cri.add(Restrictions.le(SystemLog.__CREATEDATE, formatter.unformat(endDate)));
            }

            Long count = (Long) cri.setProjection(Projections.rowCount()).uniqueResult();
            cri.setProjection(null);
            cri.setFirstResult((currentPage - 1) * 10);
            cri.setMaxResults(10);

            cri.addOrder(Order.desc("id"));

            List<SystemLog> msg03s = cri.list();


            List<SystemLogVo> vos = new ArrayList<SystemLogVo>();

            SystemLogVo vo = null;
            for (SystemLog log : msg03s) {
                vo = new SystemLogVo();
                vo.setId(log.getId());
                vo.setMessage(log.getMessage());
                vo.setType(log.getType());
                vo.setCreateDate(DateFormat.format(log.getCreateDate(), DateFormat.YYYYMMDDHHMMSS));
                vos.add(vo);
            }

            map.clear();
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
        }
        return httpMessage;
    }

    public HttpMessage sendMsg(int id) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Query query = HibernateUtil.getCurrentSession().createQuery("from WcsMessage where id=:id");
            query.setParameter("id", id);

            WcsMessage msg03 = (WcsMessage) query.uniqueResult();

            if (msg03 != null) {
                Message03 m3 = new Message03();
                m3.setPlcName(msg03.getPlcName());
                m3.IdClassification = "1";
                m3.JobType = msg03.getJobType();
                m3.McKey = msg03.getMcKey();
                m3.MachineNo = msg03.getMachineNo();
                m3.CycleOrder = msg03.getCycleOrder();
                m3.Height = msg03.getHeight() == null ? "0" : msg03.getHeight();
                m3.Width = msg03.getWidth() == null ? "0" : msg03.getWidth();
                m3.Station = msg03.getStation() == null ? "0000" : msg03.getStation();
                m3.Bank = msg03.getBank() == null ? "00" : msg03.getBank();
                m3.Bay = msg03.getBay() == null ? "00" : msg03.getBay();
                m3.Level = msg03.getLevel() == null ? "00" : msg03.getLevel();
                m3.Dock = msg03.getDock();
                MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
                _wcsproxy.addSndMsg(m3);

                msg03.setLastSendDate(new Date());

            }

            //删掉35完成报告
            Query q = HibernateUtil.getCurrentSession().createQuery("delete from WcsMessage where dock=:dock and msgType =:msgType and mcKey=:mckey");
            q.setParameter("dock", msg03.getDock());
            q.setParameter("msgType", WcsMessage.MSGTYPE_35);
            q.setParameter("mckey", msg03.getMcKey());
            q.executeUpdate();

            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("发送成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }

    public HttpMessage sendMessageHand(String message) {
        HttpMessage httpMessage = new HttpMessage();

        try {
            Transaction.begin();

            if (message.startsWith("40")) {

                Message40 m4 = new Message40(message.substring(2));
                StationBlock station = (StationBlock) StationBlock.getByStationNo(m4.Station);

                m4.setPlcName(station.getPlcName());
                MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
                _wcsproxy.addSndMsg(m4);

            } else {
                Message03 m3 = new Message03(message);
                Block block = Block.getByBlockNo(m3.MachineNo);
                m3.setPlcName(block.getPlcName());
                MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
                _wcsproxy.addSndMsg(m3);

            }

            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("发送成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }

    public HttpMessage getMsg(int id) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Query query = HibernateUtil.getCurrentSession().createQuery("from WcsMessage where id=:id");
            query.setParameter("id", id);

            WcsMessage msg03 = (WcsMessage) query.uniqueResult();


            //删掉35完成报告
            Query q = HibernateUtil.getCurrentSession().createQuery("delete from WcsMessage where dock=:dock and msgType =:msgType and mcKey=:mckey");
            q.setParameter("dock", msg03.getDock());
            q.setParameter("msgType", WcsMessage.MSGTYPE_35);
            q.setParameter("mckey", msg03.getMcKey());
            q.executeUpdate();

            Transaction.commit();

            if (msg03 != null) {
                Message35 m3 = new Message35();
                m3.setPlcName(msg03.getPlcName());
                m3.IdClassification = "1";
                m3.JobType = msg03.getJobType();
                m3.McKey = msg03.getMcKey();
                m3.MachineNo = msg03.getMachineNo();
                m3.CycleOrder = msg03.getCycleOrder();
                m3.Height = msg03.getHeight();
                m3.Width = msg03.getWidth();
                m3.Station = StringUtils.isEmpty(msg03.getStation()) ? "0000" : msg03.getStation();
                m3.Bank = msg03.getBank();
                m3.Bay = msg03.getBay();
                m3.Level = msg03.getLevel();
                m3.Dock = msg03.getDock();
                if (StringUtils.isEmpty(m3.Bay)) {
                    m3.Bay = "00";
                }
                if (StringUtils.isEmpty(m3.Bank)) {
                    m3.Bank = "00";
                }
                if (StringUtils.isEmpty(m3.Level)) {
                    m3.Level = "00";
                }
                Msg35Proc proc = new Msg35Proc();
                proc.Do(m3);
            }

            httpMessage.setSuccess(true);
            httpMessage.setMsg("发送成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }

    public HttpMessage onLine(String blockNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Block block = Block.getByBlockNo(blockNo);
            if (block == null) {
                throw new Exception("block不存在");
            }

            block.setStatus("1");

            Transaction.commit();

            httpMessage.setSuccess(true);
            httpMessage.setMsg("修改成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }

    public HttpMessage offLine(String blockNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Block block = Block.getByBlockNo(blockNo);
            if (block == null) {
                throw new Exception("block不存在");
            }

            block.setStatus("2");

            Transaction.commit();

            httpMessage.setSuccess(true);
            httpMessage.setMsg("修改成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }

    public HttpMessage changeLevel(String blockNo, String level) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Block block = Block.getByBlockNo(blockNo);
            if (block == null) {
                throw new Exception("block不存在");
            }


            if (block instanceof SCar) {

                SCar sCar = (SCar) block;

                Query charQuery = HibernateUtil.getCurrentSession().createQuery("from AsrsJob a where (a.type=:tp or a.type=:ttp or " +
                        "a.type=:tttp) and exists(select 1 from MCar m where m.blockNo=a.fromStation and " +
                        "m.position = :position )");
                charQuery.setParameter("tp", AsrsJobType.RECHARGED);
                charQuery.setParameter("ttp", AsrsJobType.RECHARGEDOVER);
                charQuery.setParameter("tttp", AsrsJobType.CHANGELEVEL);
                /*charQuery.setParameter("status", AsrsJobStatus.DONE);*/
                charQuery.setParameter("position", sCar.getPosition());
                List<AsrsJob> charQuerys = charQuery.list();

                /*Query jobQ = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type=:jtype ");
                jobQ.setParameter("jtype", AsrsJobType.CHANGELEVEL);
                List<AsrsJob> jobs = jobQ.list();*/
                if (charQuerys.size()!=0) {
                    throw new Exception("有换层作业或充电作业！");
                }

                if (StringUtils.isEmpty(sCar.getOnMCar())) {
                    throw new Exception("子车不在母车上");
                }
                if (StringUtils.isNotEmpty(sCar.getMcKey()) || StringUtils.isNotEmpty(sCar.getReservedMcKey())) {
                    throw new Exception("子车有任务，不能执行换层操作");
                }

                Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where level=:level and position=:position");
                query.setParameter("level", Integer.parseInt(level));
                query.setParameter("position", sCar.getPosition());
                query.setMaxResults(1);
                SCar levlScar = (SCar) query.uniqueResult();
                if (levlScar != null) {

                    Transaction.commit();
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("目标层有子车");

                } else {

                    Query q = HibernateUtil.getCurrentSession().createQuery("from MCar where level=:level and position=:po");
                    q.setParameter("level", Integer.parseInt(level));
                    //q.setParameter("po", Location.LEFT);
                    q.setParameter("po", sCar.getPosition());
                    q.setMaxResults(1);
                    MCar toMcar = (MCar) q.uniqueResult();


                    AsrsJob asrsJob = new AsrsJob();
                    asrsJob.setType(AsrsJobType.CHANGELEVEL);
                    asrsJob.setStatus(AsrsJobStatus.RUNNING);
                    asrsJob.setStatusDetail(AsrsJobStatusDetail.WAITING);
                    asrsJob.setFromStation(sCar.getOnMCar());
                    asrsJob.setToStation(toMcar.getBlockNo());
                    asrsJob.setGenerateTime(new Date());
                    asrsJob.setMcKey(Mckey.getNext());
                    HibernateUtil.getCurrentSession().save(asrsJob);


                    toMcar.setReservedMcKey(asrsJob.getMcKey());

                    MCar fromMcar = (MCar) Block.getByBlockNo(sCar.getOnMCar());
                    fromMcar.setMcKey(asrsJob.getMcKey());
                    sCar.setReservedMcKey(asrsJob.getMcKey());

                    Transaction.commit();

                    httpMessage.setSuccess(true);
                    httpMessage.setMsg("修改成功");
                }
            } else {
                Transaction.commit();

                httpMessage.setSuccess(false);
                httpMessage.setMsg("非子车，不能换层");

            }


        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return httpMessage;

    }

    public HttpMessage moveScar(String blockNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Block block = Block.getByBlockNo(blockNo);
            if (block == null) {
                throw new Exception("block不存在");
            }


            SCar sCar = (SCar) block;

            if (StringUtils.isNotEmpty(sCar.getOnMCar())) {
                MCar mCar = (MCar) Block.getByBlockNo(sCar.getOnMCar());
                mCar.setsCarBlockNo(null);
            }

            sCar.setLevel(0);
            sCar.setOnMCar(null);

            Transaction.commit();

            httpMessage.setSuccess(true);
            httpMessage.setMsg("修改成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }

    public HttpMessage addScar(String blockNo, String level) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Block block = Block.getByBlockNo(blockNo);
            if (block == null) {
                throw new Exception("block不存在");
            }
            if (block instanceof SCar) {
                Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where level=:level");
                query.setParameter("level", Integer.parseInt(level));
                query.setMaxResults(1);
                SCar levlScar = (SCar) query.uniqueResult();
                if (levlScar != null) {

                    Transaction.commit();
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("目标层有子车");

                } else {

                    SCar sCar = (SCar) block;
                    if (sCar.getLevel() != 0) {
                        throw new Exception("子车不在0层。");
                    }
                    sCar.setLevel(Integer.parseInt(level));

                    Query mq = HibernateUtil.getCurrentSession().createQuery("from MCar where level=:level and position=:po").setMaxResults(1);
                    mq.setParameter("level", Integer.parseInt(level));
                    mq.setParameter("po", Location.RIGHT);
                    MCar mCar = (MCar) mq.uniqueResult();

                    sCar.setOnMCar(mCar.getBlockNo());
                    sCar.setBay(mCar.getBay());
                    mCar.setsCarBlockNo(sCar.getBlockNo());
                    sCar.setActualArea(mCar.getActualArea());
                    sCar.setPosition(mCar.getPosition());

                }


                Transaction.commit();

                httpMessage.setSuccess(true);
                httpMessage.setMsg("修改成功");
            } else {
                Transaction.commit();

                httpMessage.setSuccess(false);
                httpMessage.setMsg("非子车。不能添加设备");

            }

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;

    }

    public HttpMessage recovryException(String blockNo) {
        HttpMessage httpMessage = new HttpMessage();

        try {
            Transaction.begin();

            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            Block block = Block.getByBlockNo(blockNo);

            if (block instanceof MCar) {
                MCar mcar = (MCar) block;
                mcar.setCheckLocation(false);
            }

            Plc plc = Plc.getPlcByPlcName(block.getPlcName());

            Message06 message06 = new Message06();
            message06.setPlcName(plc.getPlcName());
            message06.MachineNo = block.getBlockNo();
            message06.Status = "3";

            _wcsproxy.addSndMsg(message06);

            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("发送成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;

    }

    public HttpMessage cancelWaiting(String blockNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Block block = Block.getByBlockNo(blockNo);
            if (block == null) {
                throw new Exception("block不存在");
            }
            block.setWaitingResponse(false);

            Transaction.commit();

            httpMessage.setSuccess(true);
            httpMessage.setMsg("修改成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }

    public HttpMessage deleteJob(String mckey) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();


            AsrsJob job = AsrsJob.getAsrsJobByMcKey(mckey);
            if (job != null) {

                Query query = HibernateUtil.getCurrentSession().createQuery("from Block  where mcKey=:mk or reservedMcKey =:mk");
                query.setParameter("mk", mckey);
                List<Block> blocks = query.list();
                for (Block block : blocks) {

                    block.setWaitingResponse(false);
                    block.setMcKey("");
                    block.setStatus("2");
                    block.setReservedMcKey("");

                    if (block instanceof SCar) {
                        SCar sCar = (SCar) block;
                        Query srmQuery = HibernateUtil.getCurrentSession().createQuery("from Srm where position=:po");
                        srmQuery.setParameter("po", sCar.getPosition());
                        srmQuery.setMaxResults(1);
                        Srm srm = (Srm) srmQuery.uniqueResult();
                        if (srm != null) {
                            srm.setsCarBlockNo(sCar.getBlockNo());
                            sCar.setOnMCar(srm.getBlockNo());
                        }
                    }

                    if (block instanceof StationBlock) {
                        StationBlock stationBlock = (StationBlock) block;
                        stationBlock.setLoad("0");
                    }
                }
            }

            HibernateUtil.getCurrentSession().delete(job);

            Transaction.commit();

            httpMessage.setSuccess(true);
            httpMessage.setMsg("删除成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;

    }

    public HttpMessage chargeFinish(String blockNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Block block = Block.getByBlockNo(blockNo);
            if (block instanceof SCar) {

                SCar sCar = (SCar) block;
                if (!sCar.getStatus().equals(SCar.STATUS_CHARGE)) {
                    Transaction.rollback();
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车非充电中");
                    return httpMessage;
                }
                if (sCar.getPower() < 50) {
                    Transaction.rollback();
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车电量不足");
                    return httpMessage;

                }


                //Location location = Location.getByLocationNo(sCar.getChargeLocation());
                MCar mCarByGroupNo = MCar.getMCarByGroupNo(sCar.getGroupNo());
                Srm toSrm = Srm.getSrmByGroupNo(sCar.getGroupNo());

                //if (mCarByGroupNo.getBlockNo().equals(toSrm.getBlockNo())) {
                    sCar.setStatus(SCar.STATUS_RUN);
                    //欧普适用
                    MsgSender.send03(Message03._CycleOrder.chargeFinish, "9999", sCar, sCar.getChargeLocation(), "", AsrsJobType.RECHARGEDOVER);

//                } else {
//
//                    AsrsJob newJob = new AsrsJob();
//
//                    newJob.setWareHouse(sCar.getWareHouse());
//                    newJob.setType(AsrsJobType.RECHARGEDOVER);
//                    newJob.setMcKey(Mckey.getNext());
//                    newJob.setGenerateTime(new Date());
//                    newJob.setStatus(AsrsJobStatus.RUNNING);
//                    newJob.setStatusDetail(AsrsJobStatusDetail.WAITING);
//                    newJob.setFromLocation(sCar.getChargeLocation());
//                    newJob.setFromStation(mCarByGroupNo.getBlockNo());
//                    newJob.setToStation(mCarByGroupNo.getBlockNo());
//
//                    HibernateUtil.getCurrentSession().save(newJob);
//                    sCar.setMcKey(newJob.getMcKey());
//                    sCar.setStatus(SCar.STATUS_RUN);
//
//                }

            } else {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("设备非子车");
                return httpMessage;
            }

            Transaction.commit();

            httpMessage.setSuccess(true);
            httpMessage.setMsg("成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }

    public HttpMessage chargeStart(String blockNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Block block = Block.getByBlockNo(blockNo);
            if (block instanceof SCar) {

                SCar sCar = (SCar) block;
                if (sCar.getStatus().equals(SCar.STATUS_CHARGE)) {
                    Transaction.rollback();
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车充电中");
                    return httpMessage;
                }


                //Location location = Location.getByLocationNo(sCar.getChargeLocation());
                MCar fromSrm = MCar.getMCarByGroupNo(sCar.getGroupNo());

                if (StringUtils.isNotEmpty(sCar.getMcKey()) || StringUtils.isNotEmpty(sCar.getReservedMcKey())) {
                    Transaction.rollback();
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车存在任务");
                    return httpMessage;
                }

                if (StringUtils.isNotEmpty(fromSrm.getMcKey()) || StringUtils.isNotEmpty(fromSrm.getReservedMcKey())) {
                    Transaction.rollback();
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("母车存在任务");
                    return httpMessage;
                }

                if (StringUtils.isEmpty(sCar.getOnMCar())) {
                    Transaction.rollback();
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车不在母车上");
                    return httpMessage;
                }

                AsrsJob asrsJob = new AsrsJob();
                asrsJob.setMcKey(Mckey.getNext());
                asrsJob.setToLocation(sCar.getChargeLocation());
                asrsJob.setFromStation(fromSrm.getBlockNo());
                //Srm chargeSrm = fromSrm.getSrmByPosition(location.getPosition());
                asrsJob.setToStation(fromSrm.getBlockNo());
                asrsJob.setStatus(AsrsJobStatus.RUNNING);
                asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                asrsJob.setType(AsrsJobType.RECHARGED);
                asrsJob.setWareHouse(fromSrm.getWareHouse());
                HibernateUtil.getCurrentSession().save(asrsJob);

                fromSrm.setMcKey(asrsJob.getMcKey());
                sCar.setMcKey(asrsJob.getMcKey());


            } else {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("设备非子车");
                return httpMessage;
            }

            Transaction.commit();

            httpMessage.setSuccess(true);
            httpMessage.setMsg("成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;

    }

    public HttpMessage onTheMLCar(String blockNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Block block = Block.getByBlockNo(blockNo);
            if (block instanceof SCar) {
                SCar sCar = (SCar) block;
                if (sCar.getStatus().equals(SCar.STATUS_CHARGE)) {
                    Transaction.rollback();
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车充电中");
                    return httpMessage;
                }
                session.saveOrUpdate(sCar);
                MCar mCar = MCar.getMCarByGroupNo(sCar.getGroupNo());
                sCar.setOnMCar(mCar.getBlockNo());
                sCar.setBank(0);
                sCar.setBay(mCar.getBay());
                sCar.setLevel(mCar.getLevel());
                sCar.setPosition(mCar.getPosition());
            } else {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("设备非子车");
                return httpMessage;
            }
            Transaction.commit();

            httpMessage.setSuccess(true);
            httpMessage.setMsg("成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }
    public HttpMessage getTheSCCar(String blockNo) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Block block = Block.getByBlockNo(blockNo);
            if (block instanceof MCar) {
                MCar mCar = (MCar) block;
                session.saveOrUpdate(mCar);
                SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());
                mCar.setsCarBlockNo(sCar.getBlockNo());
            } else {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("设备非母车");
                return httpMessage;
            }
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("成功");

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }



}
