package com.thread.threads.service.impl.changelev;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.threads.operator.LiftOperator;
import com.thread.threads.service.impl.LiftServiceImpl;
import com.thread.utils.MsgSender;
import com.util.hibernate.HibernateUtil;

import javax.management.Query;

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

        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(lift.getReservedMcKey());
        LiftOperator operator = new LiftOperator(lift, lift.getReservedMcKey());
        Block block = lift.getPreBlock(asrsJob.getMcKey(), AsrsJobType.CHANGELEVEL);
        if(block instanceof MCar ){
            operator.tryLoadCarFromMCar(block);
        }

    }

    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(lift.getMcKey());
        LiftOperator operator = new LiftOperator(lift, lift.getMcKey());
        Block block = lift.getNextBlock(AsrsJobType.CHANGELEVEL, asrsJob.getToStation());
        //operator.tryUnLoadCarToConveyor((Conveyor) block);
        operator.tryUnLoadCarToMCar(block);
    }
}
