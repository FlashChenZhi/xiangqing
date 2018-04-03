package com.thread.threads.service.impl;

import com.asrs.domain.AsrsJob;
import com.thread.blocks.*;
import com.thread.threads.operator.StationOperator;
import com.thread.threads.service.StationService;

/**
 * Created by van on 2017/10/31.
 */
public class StationServiceImpl implements StationService {

    private StationBlock stationBlock;

    public StationServiceImpl(StationBlock stationBlock) {
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
        }

    }

}
