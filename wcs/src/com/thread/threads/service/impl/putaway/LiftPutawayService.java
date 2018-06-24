package com.thread.threads.service.impl.putaway;

import com.asrs.domain.AsrsJob;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.thread.blocks.Lift;
import com.thread.blocks.MCar;
import com.thread.threads.operator.LiftOperator;
import com.thread.threads.service.impl.LiftServiceImpl;

/**
 * Created by van on 2018/2/22.
 */
public class LiftPutawayService extends LiftServiceImpl {

    private Lift lift;

    public LiftPutawayService(Lift lift) {
        super(lift);
        this.lift = lift;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(lift.getReservedMcKey());
        Block preBlock = lift.getPreBlock(lift.getReservedMcKey(), asrsJob.getType(), asrsJob.getFromStation());
        LiftOperator operator = new LiftOperator(lift, lift.getReservedMcKey());

        operator.tryCarryFromConvery(preBlock);
    }

    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(lift.getMcKey());
        Block nextBlock = lift.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
        LiftOperator operator = new LiftOperator(lift, lift.getMcKey());
        if(nextBlock instanceof Conveyor){
            operator.tryUnloadGoodsToConvery(nextBlock);
        }else if(nextBlock instanceof MCar){
            operator.tryUnloadGoodsToMCar(nextBlock);
        }

    }

}
