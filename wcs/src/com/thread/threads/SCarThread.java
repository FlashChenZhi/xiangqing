package com.thread.threads;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.CreateAsrsJob;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.ScarChargeLocation;
import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.threads.service.ScarService;
import com.thread.threads.service.impl.ScarAndMCarServiceImpl;
import com.thread.threads.service.impl.ScarAndSrmServiceImpl;
import com.thread.threads.service.impl.changelev.ScarChangeLev;
import com.thread.threads.service.impl.charage.ScarCharageService;
import com.thread.threads.service.impl.charageover.ScarCharageOverService;
import com.thread.threads.service.impl.putaway.ScarAndSrmPutawayServcie;
import com.thread.threads.service.impl.retrieval.ScarAndSrmRetrievalService;
import com.thread.threads.service.impl.sts.ScarAndSrmStsService;
import com.thread.utils.MsgSender;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */
public class SCarThread extends BlockThread<SCar> {

    public SCarThread(String blockNo) {
        super(blockNo);
    }

    public static void main(String[] args) {
        SCar scar = (SCar) SCar.getByBlockNo("SC07");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();
                SCar sCar = getBlock();

                if (sCar.isWaitingResponse()) {

                } else if (!(sCar.getStatus().equals(SCar.STATUS_RUN)|| sCar.getStatus().equals(SCar.STATUS_CHARGE_OVER))) {
                    if (sCar.getStatus().equals(SCar.STATUS_CHARGE)) {
                        if (sCar.getPower() > 95) {
                            boolean hasJob=false;
                            CreateAsrsJob createAsrsJob = new CreateAsrsJob(sCar);
                            hasJob = createAsrsJob.createChargeOver(hasJob);
                            /*sCar.setStatus(SCar.STATUS_RUN);
                            //充电完成简单动作，子车开车到巷道口，然后上车，
                            MsgSender.send03(Message03._CycleOrder.chargeFinish, "9999", sCar, sCar.getChargeLocation(), "", AsrsJobType.RECHARGEDOVER);*/
                            //获取一层母车
                            /*MCar srm = MCar.getMCarByPosition(sCar.getPosition(), sCar.getLevel());
                            if (srm.getGroupNo() == null) {
                                //小车在一层，并且一层母车没有绑定小车，
                                // 并且此母车没有mckey和reservedmckey（即没有换层任务到这个母车）

                                Session session = HibernateUtil.getCurrentSession();
                                Query charQuery = session.createQuery("from AsrsJob a where a.type=:tp and " +
                                        "exists(select 1 from MCar m where (m.blockNo=a.fromStation or m.blockNo=a.toStation) and " +
                                        "m.blockNo = :blockNo )");
                                charQuery.setParameter("tp", AsrsJobType.CHANGELEVEL);
                                charQuery.setParameter("blockNo", srm.getBlockNo());
                                List<AsrsJob> charQuerys = charQuery.list();

                                srm.setGroupNo(sCar.getGroupNo());
                                sCar.setStatus(SCar.STATUS_RUN);
                                //充电完成简单动作，子车开车到巷道口，然后上车，
                                MsgSender.send03(Message03._CycleOrder.chargeFinish, "9999", sCar, sCar.getChargeLocation(), "", AsrsJobType.RECHARGEDOVER);
                            } else {

                            }*/
                            /*MCar srm = MCar.getMCarByPosition(sCar.getPosition(),sCar.getLevel());
                            Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where (toStation=:st or fromStation=:st) and type<>:tp and status=:stat");
                            query.setParameter("st", srm.getBlockNo());
                            query.setParameter("tp",AsrsJobType.RECHARGED);
                            query.setParameter("stat",AsrsJobStatus.RUNNING);
                            List<AsrsJob> jobs = query.list();
                            if (!jobs.isEmpty()&&sCar.getPower() > 35) {
                                sCar.setStatus(SCar.STATUS_RUN);
                                //充电完成简单动作，子车开车到巷道口，然后上车，
                                MsgSender.send03(Message03._CycleOrder.chargeFinish, "9999", sCar, sCar.getChargeLocation(), "", AsrsJobType.RECHARGEDOVER);
                            }*/
                        }
                    }
                }else {
                    //小车在运行状态
                    if (StringUtils.isEmpty(sCar.getReservedMcKey()) && StringUtils.isEmpty(sCar.getMcKey())) {

                        ScarAndMCarServiceImpl service = new ScarAndMCarServiceImpl(sCar);
                        if (sCar != null)
                            service.withOutJob();

                    } else if (StringUtils.isNotEmpty(sCar.getMcKey())) {

                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                        ScarService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new ScarAndSrmPutawayServcie(sCar);

                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new ScarAndSrmRetrievalService(sCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                            service = new ScarCharageService(sCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                            service = new ScarCharageOverService(sCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)) {
                            service = new ScarAndSrmStsService(sCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.CHANGELEVEL)) {
                            service = new ScarChangeLev(sCar);
                        }
                        service.withMckey();

                    } else if (StringUtils.isNotEmpty(sCar.getReservedMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                        ScarService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new ScarAndSrmPutawayServcie(sCar);

                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new ScarAndSrmRetrievalService(sCar);

                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                            service = new ScarCharageService(sCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                            service = new ScarCharageOverService(sCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)) {
                            service = new ScarAndSrmStsService(sCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.CHANGELEVEL)) {
                            service = new ScarChangeLev(sCar);
                        }
                        service.withReserveMckey();

                    }
                }
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
