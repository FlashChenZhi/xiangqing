package com.thread.threads.service.impl.changelev;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.thread.blocks.MCar;
import com.thread.threads.operator.MCarOperator;
import com.thread.threads.service.impl.MCarServiceImpl;

/**
 * Created by van on 2018/3/15.
 */
public class MCarChangeLevService extends MCarServiceImpl {

    private MCar mCar;

    public MCarChangeLevService(MCar mCar) {
        super(mCar);
        this.mCar = mCar;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
        MCarOperator operator = new MCarOperator(mCar, mCar.getReservedMcKey());
        Block block = mCar.getPreBlock(asrsJob.getMcKey(), AsrsJobType.CHANGELEVEL);
        operator.tryLoadFromLift( block);
    }

    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
        MCarOperator operator = new MCarOperator(mCar, mCar.getMcKey());
        Block block = mCar.getNextBlock(AsrsJobType.CHANGELEVEL, asrsJob.getToStation());
        operator.tryLoadToConvery( block);
    }
}
