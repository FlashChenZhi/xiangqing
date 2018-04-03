package com.thread.threads.service.impl.changelev;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.thread.blocks.Lift;
import com.thread.threads.operator.LiftOperator;
import com.thread.threads.service.impl.LiftServiceImpl;

/**
 * Created by van on 2018/3/15.
 */
public class LiftChangeLevService extends LiftServiceImpl {

    private Lift lift;

    public LiftChangeLevService(Lift lift) {
        super(lift);
        this.lift = lift;
    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(lift.getMcKey());
        LiftOperator operator = new LiftOperator(lift, lift.getMcKey());
        Block block = lift.getNextBlock(AsrsJobType.CHANGELEVEL, asrsJob.getToStation());
        operator.tryUnLoadCarToConveyor((Conveyor) block);

    }
}
