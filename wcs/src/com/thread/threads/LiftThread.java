package com.thread.threads;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.RouteDetail;
import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.threads.service.LiftService;
import com.thread.threads.service.MCarService;
import com.thread.threads.service.SrmService;
import com.thread.threads.service.impl.LiftServiceImpl;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import com.thread.threads.service.impl.changelev.LiftChangeLevService;
import com.thread.threads.service.impl.putaway.LiftPutawayService;
import com.thread.threads.service.impl.putaway.MCarAndSCarPutawayService;
import com.thread.threads.service.impl.retrieval.LiftRetrievalService;
import com.thread.threads.service.impl.retrieval.MCarAndScarRetrievalService;
import com.thread.threads.service.impl.sts.MCarAndScarStsService;
import com.thread.utils.MsgSender;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */
public class LiftThread extends BlockThread<Lift> {

    public LiftThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                Lift lift = getBlock();
//                Message26Proc(mCar);
                if (lift.isWaitingResponse()) {

                } else if (!lift.getStatus().equals("1")) {


                } else {

                    if (com.util.common.StringUtils.isEmpty(lift.getReservedMcKey()) && com.util.common.StringUtils.isEmpty(lift.getMcKey())) {
                        //移动提升机无任务
                        LiftService srmService = new LiftServiceImpl(lift) {
                            @Override
                            public void withReserveMckey() throws Exception {

                            }

                            @Override
                            public void withMckey() throws Exception {

                            }
                        };
                        if(srmService!=null)
                        srmService.withOutJob();

                    } else if (com.util.common.StringUtils.isNotEmpty(lift.getReservedMcKey())) {
                        //移动提升机有预约任务
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(lift.getReservedMcKey());
                        LiftService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new LiftPutawayService(lift);
                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new LiftRetrievalService(lift);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {

                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {

                        } else if (asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)) {

                        } else if (asrsJob.getType().equals(AsrsJobType.ST2ST)) {

                        }else if (asrsJob.getType().equals(AsrsJobType.CHANGELEVEL)) {
                            service = new LiftChangeLevService(lift);
                        }
                        if(service!=null)
                        service.withReserveMckey();

                    } else if (com.util.common.StringUtils.isNotEmpty(lift.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(lift.getMcKey());
                        LiftService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new LiftPutawayService(lift);
                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new LiftRetrievalService(lift);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {

                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {

                        } else if (asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)) {

                        } else if (asrsJob.getType().equals(AsrsJobType.ST2ST)) {

                        } else if (asrsJob.getType().equals(AsrsJobType.CHANGELEVEL)) {
                            service = new LiftChangeLevService(lift);
                        }
                        service.withMckey();
                    }

                }
                Transaction.commit();
            } catch (Exception ex) {
                Transaction.rollback();
                ex.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
