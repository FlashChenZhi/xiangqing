package com.thread.threads.service.impl.retrieval;

import com.asrs.business.consts.StationMode;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Station;
import com.thread.blocks.*;
import com.thread.threads.operator.ConveyorOperator;
import com.thread.threads.service.impl.ConveyorServiceImpl;
import org.apache.commons.lang.StringUtils;

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

        if (conveyor.getBlockNo().equals("0012")) {
            //如果当前输送机是0007
            //检查1005上是否有托盘
            Station station1303 =Station.getStation("1303");
            Conveyor conveyor0017 = (Conveyor) Conveyor.getByBlockNo("0017");
            if (StringUtils.isNotBlank(conveyor0017.getMcKey()) || StringUtils.isNotBlank(conveyor0017.getReservedMcKey())) {
                if(StationMode.RETRIEVAL.equals(station1303.getDirection())){
                    return;
                }
            }
        }

        if (conveyor.getBlockNo().equals("0019")) {
            //如果当前输送机是0007
            //检查1005上是否有托盘
            Station station1303 =Station.getStation("1303");
            Conveyor conveyor0017 = (Conveyor) Conveyor.getByBlockNo("0017");
            if (StringUtils.isNotBlank(conveyor0017.getMcKey()) || StringUtils.isNotBlank(conveyor0017.getReservedMcKey())) {
                if(StationMode.PUTAWAY.equals(station1303.getDirection())){
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
