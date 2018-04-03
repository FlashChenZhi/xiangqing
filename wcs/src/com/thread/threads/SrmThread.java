package com.thread.threads;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.threads.service.SrmService;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import com.thread.threads.service.impl.charage.SrmCharageService;
import com.thread.threads.service.impl.charageover.SrmCharageOverService;
import com.thread.threads.service.impl.emp.SrmAndScarEmpService;
import com.thread.threads.service.impl.putaway.SrmAndScarPutawayService;
import com.thread.threads.service.impl.retrieval.SrmAndScarRetrievalService;
import com.thread.threads.service.impl.sts.SrmAndScarStsService;
import com.thread.utils.MsgSender;
import com.util.common.StringUtils;
import com.util.hibernate.Transaction;

/**
 * Created by van on 2017/8/29.
 */
public class SrmThread extends BlockThread<Srm> {

    public static void main(String[] args) throws Exception {

        Transaction.begin();

        MCar mCar = (MCar) Block.getByBlockNo("MC05");
        SCar sCar = (SCar) Block.getByBlockNo("SC06");

        Conveyor conveyor1 = (Conveyor) Block.getByBlockNo("0058");//输送机
        Conveyor conveyor2 = (Conveyor) Block.getByBlockNo("0059");//输送机

//        MsgSender.send03(Message03._CycleOrder.move, "9999", mCar, "201007001", "", "", "");
//
//
//        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods  , "9999", conveyor1, "", mCar.getBlockNo(), "", "");
//
//        MsgSender.send03(Message03._CycleOrder.unloadCar, "8758", mCar, "101015001",sCar.getBlockNo(), "", "");
//        MsgSender.send03(Message03._CycleOrder.offCar, "8758", sCar, "101015001",mCar.getBlockNo(), "", "");

        //MsgSender.send03(Message03._CycleOrder.unloadGoods, "4406", sCar, "209008002","", "", "");
        MsgSender.send03(Message03._CycleOrder.move, "4433", mCar, "","0059", "", "");
//
//        MsgSender.send03(Message03._CycleOrder.unloadGoods, "9999", sCar, "201007001",  "", "", "");

        Transaction.commit();

    }

    public SrmThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {

        while (true) {

            try {

                Transaction.begin();

                Srm srm = getBlock();
                if (srm.isWaitingResponse()) {
                    //移动提升机等待回复

                } else if (!srm.getStatus().equals("1")) {
                    //移动提升机状态不可用

                } else {

                    if (StringUtils.isEmpty(srm.getReservedMcKey()) && StringUtils.isEmpty(srm.getMcKey())) {
                        //移动提升机无任务
                        SrmService srmService = new SrmAndScarServiceImpl(srm) {
                            @Override
                            public void withReserveMckey() throws Exception {

                            }

                            @Override
                            public void withMckey() throws Exception {

                            }
                        };

                        srmService.withOutJob();

                    } else if (StringUtils.isNotEmpty(srm.getReservedMcKey())) {
                        //移动提升机有预约任务
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getReservedMcKey());
                        SrmService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new SrmAndScarPutawayService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new SrmAndScarRetrievalService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                            service = new SrmCharageService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                            service = new SrmCharageOverService(srm);
                        }else if(asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)){
                            service = new SrmAndScarStsService(srm);
                        }else if(asrsJob.getType().equals(AsrsJobType.ST2ST)){
                            service = new SrmAndScarEmpService(srm);
                        }

                        service.withReserveMckey();

                    } else if (StringUtils.isNotEmpty(srm.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getMcKey());
                        SrmService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new SrmAndScarPutawayService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new SrmAndScarRetrievalService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                            service = new SrmCharageService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                            service = new SrmCharageOverService(srm);
                        }else if(asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)){
                            service = new SrmAndScarStsService(srm);
                        }else if(asrsJob.getType().equals(AsrsJobType.ST2ST)){
                            service = new SrmAndScarEmpService(srm);
                        }
                        service.withMckey();
                    }

                }

                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
