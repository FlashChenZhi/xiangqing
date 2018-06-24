package com.thread.threads.service.impl.putaway;

import com.asrs.domain.AsrsJob;
import com.thread.blocks.*;
import com.thread.threads.operator.StationOperator;
import com.thread.threads.service.impl.StationServiceImpl;

/**
 * Created by van on 2017/11/9.
 */
public class StationPutawayService extends StationServiceImpl {

    private StationBlock stationBlock;

    public StationPutawayService(StationBlock stationBlock) {
        super(stationBlock);
        this.stationBlock = stationBlock;
    }

    @Override
    public void withOutJob() throws Exception {

    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {

        AsrsJob aj = AsrsJob.getAsrsJobByMcKey(stationBlock.getMcKey());
        Block nextBlock = stationBlock.getNextBlock(aj.getType(), aj.getToStation());

        StationOperator operator = new StationOperator(stationBlock, aj.getMcKey());

        if (nextBlock instanceof Conveyor) {
            operator.tryMoveToConveyor((Conveyor) nextBlock);
        } else if (nextBlock instanceof Srm) {
            operator.tryMoveToSrm((Srm)nextBlock);
        }else if(nextBlock instanceof Lift){
            operator.tryMoveToLift((Lift)nextBlock);
        }

    }

}
