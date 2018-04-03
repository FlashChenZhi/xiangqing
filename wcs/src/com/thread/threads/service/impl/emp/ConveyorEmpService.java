package com.thread.threads.service.impl.emp;

import com.asrs.domain.AsrsJob;
import com.thread.blocks.*;
import com.thread.threads.operator.ConveyorOperator;
import com.thread.threads.service.impl.ConveyorServiceImpl;

/**
 * Created by van on 2017/11/2.
 */
public class ConveyorEmpService extends ConveyorServiceImpl {

    private Conveyor conveyor;

    public ConveyorEmpService(Conveyor conveyor) {
        super(conveyor);
        this.conveyor = conveyor;
    }

    @Override
    public void withReserveMckey() throws Exception {

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
