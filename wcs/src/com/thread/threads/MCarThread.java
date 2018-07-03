package com.thread.threads;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.thread.blocks.*;
import com.thread.threads.service.MCarService;
import com.thread.threads.service.impl.MCarServiceImpl;
import com.thread.threads.service.impl.changelev.MCarChangeLevService;
import com.thread.threads.service.impl.charage.MCarCharageService;
import com.thread.threads.service.impl.charageover.MCarCharageOverService;
import com.thread.threads.service.impl.putaway.MCarAndSCarPutawayService;
import com.thread.threads.service.impl.retrieval.MCarAndScarRetrievalService;
import com.thread.threads.service.impl.sts.MCarAndScarStsService;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;


/**
 * Created by Administrator on 2016/11/1.
 */
public class MCarThread extends BlockThread<MCar> {

    public MCarThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                MCar mCar = getBlock();
//                Message26Proc(mCar);
                if (mCar.isWaitingResponse()) {

                } else if (!mCar.getStatus().equals("1")) {


                } else {

                    if (com.util.common.StringUtils.isEmpty(mCar.getReservedMcKey()) && com.util.common.StringUtils.isEmpty(mCar.getMcKey())) {
                        //移动提升机无任务
                        MCarService srmService = new MCarServiceImpl(mCar) {
                            @Override
                            public void withReserveMckey() throws Exception {

                            }

                            @Override
                            public void withMckey() throws Exception {

                            }
                        };
                        srmService.withOutJob();

                    } else if (com.util.common.StringUtils.isNotEmpty(mCar.getReservedMcKey())) {
                        //移动提升机有预约任务
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
                        MCarService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new MCarAndSCarPutawayService(mCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new MCarAndScarRetrievalService(mCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                            service =  new MCarCharageService(mCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                            service = new MCarCharageOverService(mCar);
                        }else if(asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)){
                            service = new MCarAndScarStsService(mCar);
                        }else if(asrsJob.getType().equals(AsrsJobType.ST2ST)){

                        }else if(asrsJob.getType().equals(AsrsJobType.CHANGELEVEL)){
                            service = new MCarChangeLevService(mCar);
                        }
                        if(service!=null)
                        service.withReserveMckey();

                    } else if (com.util.common.StringUtils.isNotEmpty(mCar.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
                        MCarService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new MCarAndSCarPutawayService(mCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new MCarAndScarRetrievalService(mCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                            service = new MCarCharageService(mCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                            service = new MCarCharageOverService(mCar);
                        }else if(asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)){
                            service = new MCarAndScarStsService(mCar);
                        }else if(asrsJob.getType().equals(AsrsJobType.ST2ST)){

                        }else if(asrsJob.getType().equals(AsrsJobType.CHANGELEVEL)){
                            service = new MCarChangeLevService(mCar);
                        }
                        if(service!=null)
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
