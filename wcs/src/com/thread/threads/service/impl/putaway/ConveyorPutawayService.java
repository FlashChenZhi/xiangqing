package com.thread.threads.service.impl.putaway;

import com.asrs.domain.AsrsJob;
import com.thread.blocks.*;
import com.thread.threads.operator.ConveyorOperator;
import com.thread.threads.service.impl.ConveyorServiceImpl;

/**
 * Created by van on 2017/11/2.
 */
public class ConveyorPutawayService extends ConveyorServiceImpl {

    private Conveyor conveyor;

    public ConveyorPutawayService(Conveyor conveyor) {
        super(conveyor);
        this.conveyor = conveyor;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(conveyor.getReservedMcKey());
        ConveyorOperator operator = new ConveyorOperator(conveyor, asrsJob.getMcKey());
        Block preBlock = conveyor.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());

        if (preBlock instanceof Srm) {
            Srm srm = (Srm) preBlock;
            operator.tryMoveCarryGoodsFromSrm(srm);
        }

    }

    @Override
    public void withMckey() throws Exception {

        AsrsJob aj = AsrsJob.getAsrsJobByMcKey(conveyor.getMcKey());
        Block nextBlock = conveyor.getNextBlock(aj.getType(), aj.getToStation());

        ConveyorOperator operator = new ConveyorOperator(conveyor, aj.getMcKey());

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
