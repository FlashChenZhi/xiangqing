package com.thread.threads;


import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.thread.blocks.*;
import com.thread.threads.operator.ConveyorOperator;
import com.thread.threads.service.Service;
import com.thread.threads.service.impl.changelev.ConveyorChangeLevService;
import com.thread.threads.service.impl.emp.ConveyorEmpService;
import com.thread.threads.service.impl.putaway.ConveyorPutawayService;
import com.thread.threads.service.impl.retrieval.ConveyorRetrievalService;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;

/**
 * Created by van on 2017/5/2.
 */
public class ConveyorThread extends BlockThread<Conveyor> {

    public static void main(String[] args) {
        ConveyorThread thread = new ConveyorThread("0065");
        thread.run();
    }

    public ConveyorThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {

        while (true) {
            try {

                Transaction.begin();
                Conveyor crane = getBlock();

                if (crane.isWaitingResponse()) {

                    //输送机等待35回复
                    if (crane.isManty()) {
                        //如果是多个输送机的Block,检查第二个waing状态
                        if (!crane.isMantWaiting()) {
                            if (StringUtils.isNotBlank(crane.getMcKey())) {
                                AsrsJob aj = AsrsJob.getAsrsJobByMcKey(crane.getMcKey());
                                Block nextBlock = crane.getNextBlock(aj.getType(), aj.getToStation());

                                ConveyorOperator operator = new ConveyorOperator(crane, aj.getMcKey());

                                //输送机运行
                                if (nextBlock instanceof Conveyor) {
                                    operator.tryMoveToAnotherCrane(nextBlock);
                                } else if (nextBlock instanceof Lift) {
                                    operator.tryMoveUnloadGoodsToLift((Lift) nextBlock);
                                } else if (nextBlock instanceof Srm) {
                                    operator.tryMoveUnloadGoodsToSrm((Srm) nextBlock);
                                } else if (nextBlock instanceof MCar) {
                                    operator.tryMoveUnloadGoodsToMCar((MCar) nextBlock);
                                } else if (nextBlock instanceof StationBlock) {
                                    operator.tryMoveUnloadGoodsToStation((StationBlock) nextBlock);
                                }


                            }
                        }

                    }

                } else {
                    if (StringUtils.isBlank(crane.getMcKey()) && StringUtils.isBlank(crane.getReservedMcKey())) {

                    } else if (StringUtils.isNotEmpty(crane.getMcKey())) {
                        AsrsJob aj = AsrsJob.getAsrsJobByMcKey(crane.getMcKey());
                        Service service = null;
                        if (aj.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new ConveyorPutawayService(crane);

                        } else if (aj.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new ConveyorRetrievalService(crane);

                        } else if (aj.getType().equals(AsrsJobType.RECHARGED)) {

                        }else if(aj.getType().equals(AsrsJobType.ST2ST)){
                            service = new ConveyorEmpService(crane);
                        }else if(aj.getType().equals(AsrsJobType.CHANGELEVEL)){
                            service = new ConveyorChangeLevService(crane);
                        }
                        service.withMckey();

                    } else if (StringUtils.isNotBlank(crane.getReservedMcKey())) {

                        AsrsJob aj = AsrsJob.getAsrsJobByMcKey(crane.getReservedMcKey());
                        Service service = null;
                        if (aj.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new ConveyorRetrievalService(crane);

                        } else if (aj.getType().equals(AsrsJobType.RECHARGED)) {

                        }else if(aj.getType().equals(AsrsJobType.ST2ST)){

                            service = new ConveyorEmpService(crane);

                        } else if (aj.getType().equals(AsrsJobType.CHANGELEVEL)) {
                            service = new ConveyorChangeLevService(crane);
                        }

                        service.withReserveMckey();
                    }

                }

                Transaction.commit();

            } catch (Exception e) {
                System.out.println(_blockNo);
                e.printStackTrace();
                Transaction.rollback();
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
