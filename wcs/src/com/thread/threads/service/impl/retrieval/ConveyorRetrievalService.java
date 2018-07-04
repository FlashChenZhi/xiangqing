package com.thread.threads.service.impl.retrieval;

import com.asrs.domain.AsrsJob;
import com.thread.blocks.*;
import com.thread.threads.operator.ConveyorOperator;
import com.thread.threads.service.impl.ConveyorServiceImpl;

import java.util.List;

/**
 * Created by van on 2017/11/2.
 */
public class ConveyorRetrievalService extends ConveyorServiceImpl {

    private Conveyor conveyor;

    public ConveyorRetrievalService(Conveyor conveyor) {
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
            StationBlock toStationBlock = (StationBlock) nextBlock;
            if(toStationBlock.isOutLoad()){
                //到达站台有载荷
                List<StationBlock> stationBlocks =StationBlock.getByGroupNo(toStationBlock.getGroupNo());
                if(stationBlocks.size()!=0){
                    for(StationBlock stationBlock : stationBlocks){
                        if(!stationBlock.getBlockNo().equals(toStationBlock.getBlockNo())){
                            //同一groupNo，的站台没有载荷，移载卸货，
                            if(!stationBlock.isOutLoad()){
                                operator.tryMoveUnloadGoodsToStation( stationBlock);
                            }
                        }
                    }
                }
            }else{
                boolean hasOutLoad=false;
                //到达站台有载荷
                List<StationBlock> stationBlocks =StationBlock.getByGroupNo(toStationBlock.getGroupNo());
                if(stationBlocks.size()!=0){
                    for(StationBlock stationBlock : stationBlocks){
                        if(!stationBlock.getBlockNo().equals(toStationBlock.getBlockNo())){
                            //同一groupNo，的站台有载荷
                            if(stationBlock.isOutLoad()){
                                hasOutLoad=true;
                                break;
                            }
                        }
                    }

                }
                if(!hasOutLoad){
                    operator.tryMoveUnloadGoodsToStation((StationBlock) nextBlock);
                }
            }
        }

    }
}
