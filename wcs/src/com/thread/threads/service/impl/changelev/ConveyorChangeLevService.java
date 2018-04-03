package com.thread.threads.service.impl.changelev;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.thread.blocks.Lift;
import com.thread.blocks.MCar;
import com.thread.threads.operator.ConveyorOperator;
import com.thread.threads.service.impl.ConveyorServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2018/3/15.
 */
public class ConveyorChangeLevService extends ConveyorServiceImpl {

    private Conveyor conveyor;

    public ConveyorChangeLevService(Conveyor conveyor) {
        super(conveyor);
        this.conveyor = conveyor;
    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {
        ConveyorOperator operator = new ConveyorOperator(conveyor, conveyor.getMcKey());
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(conveyor.getMcKey());
        Block block = conveyor.getNextBlock(AsrsJobType.CHANGELEVEL, asrsJob.getToStation());
        if (StringUtils.isBlank(block.getMcKey()) && StringUtils.isNotBlank(block.getReservedMcKey())
                && block.getReservedMcKey().equals(conveyor.getMcKey())) {
            if (block instanceof Lift) {
                operator.tryUnLoadCarToLift((Lift) block);
            } else {
                operator.tryUnLoadCarToMCar((MCar) block);
            }
        }
    }
}
