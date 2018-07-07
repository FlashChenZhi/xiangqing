package com.thread.threads.service.impl.putaway;

import com.asrs.business.consts.StationMode;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Station;
import com.thread.blocks.*;
import com.thread.threads.operator.ConveyorOperator;
import com.thread.threads.service.impl.ConveyorServiceImpl;
import org.apache.commons.lang.StringUtils;

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

        if (conveyor.getBlockNo().equals("0007")) {
            //如果当前输送机是0007
            //检查1005上是否有托盘
            Station station1302 =Station.getStation("1302");
            Station station1301 =Station.getStation("1301");
            Conveyor conveyor0003 = (Conveyor) Conveyor.getByBlockNo("0003");
            if (StringUtils.isNotBlank(conveyor0003.getMcKey()) || StringUtils.isNotBlank(conveyor0003.getReservedMcKey())) {
                if(StationMode.RETRIEVAL.equals(station1301.getDirection())){
                    return;
                }
            }
        }

        if (conveyor.getBlockNo().equals("0002")) {
            //如果当前输送机是0002
            Station station1302 =Station.getStation("1302");
            Station station1301 =Station.getStation("1301");
            Conveyor conveyor0008 = (Conveyor) Conveyor.getByBlockNo("0008");
            if (StringUtils.isNotBlank(conveyor0008.getMcKey()) || StringUtils.isNotBlank(conveyor0008.getReservedMcKey())) {
                if(StationMode.PUTAWAY.equals(station1302.getDirection())){
                    return;
                }
            }
        }

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
