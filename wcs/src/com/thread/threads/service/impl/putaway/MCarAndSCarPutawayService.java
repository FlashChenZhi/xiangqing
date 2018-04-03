package com.thread.threads.service.impl.putaway;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.threads.operator.MCarOperator;
import com.thread.threads.service.impl.MCarServiceImpl;
import com.thread.utils.MsgSender;
import com.util.common.StringUtils;

/**
 * Created by van on 2018/2/1.
 */
public class MCarAndSCarPutawayService extends MCarServiceImpl {

    private MCar mCar;

    public MCarAndSCarPutawayService(MCar mCar) {
        super(mCar);
        this.mCar = mCar;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
        MCarOperator operator = new MCarOperator(mCar, asrsJob.getMcKey());

        if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
            //提升机上有子车，提升机准备去接货
            operator.tryCarryGoods();
        } else {

           // Location location = Location.getByLocationNo(asrsJob.getToLocation());
//            if (mCar.getBay() == location.getBay()
//                    && mCar.getLevel() == location.getLevel()) {
                //优先接货
            SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());
            if(StringUtils.isEmpty(sCar.getOnMCar()))
                operator.tryCarryGoods();
//            } else {
//                //计算是否先取货，还是先取车，如果子车不在提升机上
//                boolean flag = loadCarFirst();
//
//                if (flag) {
//                    //先去取车
//                    //移动提升机上没有子车，先去接子车，去子车所在的层列
//                    operator.tryLoadCar();
//                } else {
//                    //提升机先去接货
//                    operator.tryCarryGoods();
//                }
//            }
        }


    }

    private boolean loadCarFirst() {
        return true;
    }


    @Override
    public void withMckey() throws Exception {
        //移动提升机正在执行任务
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
        MCarOperator operator = new MCarOperator(mCar, asrsJob.getMcKey());

        Location toLocation = Location.getByLocationNo(asrsJob.getToLocation());

        if(mCar.getBlockNo().equals(asrsJob.getToStation())){
            if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                //移动提升机上有子车
                //判断移动提升机的位置排列层，实绩位置，和目标货位是否一致
                if (mCar.arrive(toLocation)) {
                    //移动提升机移动到指定层列，开始卸子车
                    operator.unLoadCar(mCar.getsCarBlockNo(), asrsJob.getMcKey(), toLocation);
                } else {
                    //移动提升机移动到指定的层，列
                    operator.move(toLocation);
                }
            } else {
                //提升机去找子车，接子车
                operator.tryLoadCar();
            }
        }else {
            Block nextBlock = mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                operator.tryUnloadGoodsToConvery((Conveyor) nextBlock);
        }

    }
}
