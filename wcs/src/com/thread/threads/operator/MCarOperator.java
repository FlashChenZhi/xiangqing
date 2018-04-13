package com.thread.threads.operator;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.utils.MsgSender;
import org.apache.commons.lang.StringUtils;

/**
 * Created by van on 2017/11/2.
 */
public class MCarOperator {

    private MCar mCar;
    private String mckey;

    public MCarOperator(MCar mCar, String mckey) {
        this.mCar = mCar;
        this.mckey = mckey;
    }

    public void tryCarryGoods() throws Exception {
        AsrsJob job = AsrsJob.getAsrsJobByMcKey(mckey);
        //移动提升机上有子车，去上一个block接货
        Block block = mCar.getPreBlock(mckey, job.getType());

        if (!block.getBlockNo().equals(mCar.getDock()) || !mCar.getCheckLocation() == true) {
            //移动到dock
            this.move(block.getBlockNo());
        } else {
            //从block移栽取货
            this.moveCarryGoods(block.getBlockNo());
        }
    }

    private void moveCarryGoods(String block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, mCar, "", block, "", "");
    }

    private void move(String blockNo) throws Exception {
        MsgSender.send03(Message03._CycleOrder.move, mckey, mCar, "", blockNo, "", "");

    }

    public void tryLoadCar() throws Exception {
        //移动提升机上没有子车，先去接子车，去子车所在的层列
        SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());

        if (sCar != null && sCar.getStatus().equals("1")) {
            Location sCarLocation = Location.getByBankBayLevel(sCar.getBank(), sCar.getBay(), sCar.getLevel(), sCar.getPosition());
            if (sCarLocation != null) {
                //判断移动提升机的位置排列层，实绩位置，和目标货位是否一致
                if (mCar.arrive(sCarLocation)) {
                    //移动提升机移动到指定层列，开始装载子车
                    this.loadCar(sCar.getBlockNo(), mckey, sCarLocation);
                } else {
                    //移动堆垛机移动到指定的层，列
                    this.move(sCarLocation);
                }

            } else {
                if (StringUtils.isNotBlank(mCar.getMcKey())) {
                    if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                        Location location = Location.getByLocationNo(asrsJob.getToLocation());
                        if (!mCar.arrive(location))
                            this.move(location);
                    }
                }
            }
        }

    }

    private void loadCar(String sCar, String mckey, Location toLocation) throws Exception {
        MsgSender.send03(Message03._CycleOrder.loadCar, mckey, mCar, toLocation.getLocationNo(), sCar, "", "");

    }

    public void move(Location toLocation) throws Exception {
        MsgSender.send03(Message03._CycleOrder.move, mckey, mCar, toLocation.getLocationNo(), "", toLocation.getBay() + "", toLocation.getLevel() + "");

    }

    public void unLoadCar(String sCarBlcok, String mckey, Location toLocation) throws Exception {
        SCar sCar = (SCar) Block.getByBlockNo(sCarBlcok);
        if (StringUtils.isNotBlank(sCar.getOnMCar())) {
            MsgSender.send03(Message03._CycleOrder.unloadCar, mckey, mCar, toLocation.getLocationNo(), sCarBlcok, "", "");
        }
    }

    /**
     * 卸子车(特殊用于计算是否先卸子车出库)
     *
     * @param sCar
     * @param reservedMcKey
     */
    public void unLoadCarFirst(SCar sCar, String reservedMcKey) throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(reservedMcKey);
        Location location = Location.getByLocationNo(asrsJob.getFromLocation());
        ScarOperator scarOperator = new ScarOperator(sCar, reservedMcKey);
        if (mCar.getCheckLocation() && mCar.getLevel() == location.getLevel() && mCar.getBay() == location.getBay()) {
            unLoadCar(sCar.getBlockNo(), reservedMcKey, location);
            scarOperator.offCar(mCar.getBlockNo(), location.getLocationNo());
        } else {
            move(location);
        }

    }

    /**
     * 母车准备卸货
     */
    public void tryUnloadGoods() throws Exception {
        AsrsJob job = AsrsJob.getAsrsJobByMcKey(mckey);
        Block block = mCar.getNextBlock(job.getType(), job.getToStation());
        if (!block.getBlockNo().equals(mCar.getDock()) || !mCar.getCheckLocation() == true) {
            //移动到dock
            this.move(block.getBlockNo());
        } else {
            //从block移栽卸货
            if (StringUtils.isBlank(block.getMcKey()) && StringUtils.isBlank(block.getReservedMcKey())) {
                this.moveUnloadGoods(block.getBlockNo());
                ConveyorOperator conveyorOperator = new ConveyorOperator((Conveyor) block, job.getMcKey());
                conveyorOperator.tryMoveCarryGoodsFromMcar(mCar);
            }
        }
    }

    /**
     * 移栽卸货
     *
     * @param block
     * @throws Exception
     */
    public void moveUnloadGoods(String block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, mCar, "", block, "", "");

    }

    /**
     * 母车卸子车到货位
     *
     * @param location
     */
    public void tryUnLoadCarToLocation(Location location) throws Exception {
        if (mCar.getActualArea().equals(location.getActualArea())
                && mCar.getLevel() == location.getLevel()
                && mCar.getBay() == location.getBay()
                && mCar.getCheckLocation() == true) {
            unLoadCar(mCar.getsCarBlockNo(), mckey, location);
        } else {
            move(location);
        }
    }

    public void tryUnloadGoodsToConvery(Conveyor conveyor)throws Exception{
        if (conveyor.getBlockNo().equals(mCar.getDock())){
            MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, mCar, "", conveyor.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, conveyor, "", mCar.getBlockNo(), "", "");

        }else{
            move(conveyor.getBlockNo());
        }
    }


    /**
     * 母车卸子车
     *
     * @param conveyor
     */
    public void tryLoadToConvery(Conveyor conveyor) throws Exception {
        if (conveyor.getBlockNo().equals(mCar.getDock())) {
            SCar sCar = (SCar) SCar.getByBlockNo(mCar.getsCarBlockNo());
            MsgSender.send03(Message03._CycleOrder.unloadCar, mckey, mCar, "", sCar.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.offCar, mckey, sCar, "", conveyor.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.loadCar, mckey, conveyor, "", sCar.getBlockNo(), "", "");

        } else {
            move(conveyor.getBlockNo());
        }
    }


}