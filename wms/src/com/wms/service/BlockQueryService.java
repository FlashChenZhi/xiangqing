package com.wms.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.common.HttpMessage;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.AsrsJob;
import com.wms.domain.blocks.Block;
import com.wms.domain.blocks.Crane;
import com.wms.domain.blocks.SCar;
import com.wms.vo.BaseReturnObj;
import com.wms.vo.BlockVo;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
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
public class BlockQueryService {


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
                if (block instanceof Crane)
                    blockVo.setsCarNo(((Crane) block).getsCarNo());
                if (block instanceof SCar)
                    blockVo.setmCarNo(((SCar) block).getOnMCar());
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
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;
    }


    public HttpMessage onLine(String blockNo, boolean b) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from Block where blockNo=:blockNo");
            query.setParameter("blockNo", blockNo);
            query.setMaxResults(1);
            Block block = (Block) query.uniqueResult();
            if (b) {
                block.setStatus("1");
            } else {
                block.setStatus("9");
            }
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("设定成功");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;
    }

    public BaseReturnObj init() {
        LogWriter.info(LoggerType.WMS, "初始化设备");

        BaseReturnObj httpMessage = new BaseReturnObj();
        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from Block");
            List<Block> blocks = query.list();

            for (Block block : blocks) {
                block.setReservedMcKey(null);
                block.setMcKey(null);
                block.setWaitingResponse(false);
                if (block instanceof Crane) {
                    ((Crane) block).setBay(2);
                    ((Crane) block).setLevel(0);
                    ((Crane) block).setsCarNo("SC01");
                } else if (block instanceof SCar) {
                    ((SCar) block).setOnMCar("ML01");
                    ((SCar) block).setLevel(0);
                    ((SCar) block).setBay(2);
                }
            }

            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("设定成功");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;
    }

    public HttpMessage onCar(String blockNo) {
        LogWriter.info(LoggerType.WMS, "上车：" + blockNo);

        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            SCar block = (SCar) Block.getByBlockNo(blockNo);
            if (!(block instanceof SCar)) {
                httpMessage.setMsg("不是小车无法使用上车命令");
            } else {
                Crane crane = (Crane) Block.getByBlockNo("ML01");
                crane.setsCarNo(block.getBlockNo());
                crane.setWaitingResponse(false);

                block.setOnMCar(crane.getBlockNo());
                block.setWaitingResponse(false);
                httpMessage.setMsg("设定成功");
            }
            Transaction.commit();
            httpMessage.setSuccess(true);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;
    }

    public HttpMessage cancelWaiting(String blockNo) {
        LogWriter.info(LoggerType.WMS, "取消等待：" + blockNo);

        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from Block where blockNo=:blockNo");
            query.setParameter("blockNo", blockNo);
            query.setMaxResults(1);
            Block block = (Block) query.uniqueResult();
            block.setWaitingResponse(false);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("设定成功");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;
    }

    public HttpMessage charge(String blockNo) {
        LogWriter.info(LoggerType.WMS, "充电：" + blockNo);

        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            if (blockNo.equals("SC01")) {

                SCar sCar = (SCar) Block.getByBlockNo(blockNo);
                if(sCar.getStatus().equals("6")){
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车充电中");

                }else{
//
//                    Query q = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type =:jtype or  type =:jobtype");
//                    q.setParameter("jtype", AsrsJobType.RECHARGE);
//                    q.setParameter("jobtype", AsrsJobType.RECHARGEOVER);
//                    List<AsrsJob> jobs = q.list();
//                    if (jobs.isEmpty()) {
//
//                        AsrsJob job = new AsrsJob();
//                        job.setType(AsrsJobType.RECHARGE);
//                        job.setFromStation("ML01");
//                        job.setToStation("ML01");
//                        job.setToLocation("120101");
//                        job.setFromLocation("120101");
//                        job.setStatus("1");
//                        job.setStatusDetail("0");
//                        job.setMcKey(Mckey.getNext());
//                        HibernateUtil.getCurrentSession().save(job);
//
//                        httpMessage.setSuccess(true);
//                        httpMessage.setMsg("设定成功");
//
//                    } else {
//                        httpMessage.setSuccess(false);
//                        httpMessage.setMsg("系统存在充电任务");
//
//                    }

                }

            } else {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("非SC01不能充电");
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;

    }

    public HttpMessage chargeOver(String blockNo) {
        LogWriter.info(LoggerType.WMS, "充电结束：" + blockNo);

        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            if (blockNo.equals("SC01")) {


                SCar sCar = (SCar) Block.getByBlockNo(blockNo);
                if(!sCar.getStatus().equals("6")){
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车非充电中");

                }else{

//                    Query q = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type =:jtype or  type =:jobtype");
//                    q.setParameter("jtype", AsrsJobType.RECHARGE);
//                    q.setParameter("jobtype", AsrsJobType.RECHARGEOVER);
//                    List<AsrsJob> jobs = q.list();
//                    if (jobs.isEmpty()) {
//
//                        AsrsJob job = new AsrsJob();
//                        job.setType(AsrsJobType.RECHARGEOVER);
//                        job.setFromStation("ML01");
//                        job.setToStation("ML01");
//                        job.setFromLocation("120101");
//                        job.setStatus("1");
//                        job.setStatusDetail("0");
//                        job.setMcKey(Mckey.getNext());
//                        HibernateUtil.getCurrentSession().save(job);
//
//                        httpMessage.setSuccess(true);
//                        httpMessage.setMsg("设定成功");
//
//                    } else {
//                        httpMessage.setSuccess(false);
//                        httpMessage.setMsg("存在充电任务");
//
//                    }
                }


            } else {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("非SC01不能充电");
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;

    }
}
