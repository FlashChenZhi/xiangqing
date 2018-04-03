package com.thread.threads.operator;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.utils.MsgSender;
import org.apache.commons.lang.StringUtils;

/**
 * Created by van on 2017/11/2.
 */
public class SrmOperator {

    private Srm srm;
    private String mckey;

    public SrmOperator(Srm srm, String mckey) {
        this.srm = srm;
        this.mckey = mckey;
    }

    /**
     * 移栽取货
     *
     * @param block
     * @throws Exception
     */
    public void moveCarryGoods(String block) throws Exception {

        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, srm, "", block, "", "");

    }

    /**
     * 移栽卸货
     *
     * @param block
     * @throws Exception
     */
    public void moveUnloadGoods(String block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, srm, "", block, "", "");

    }

    /**
     * 移动到指定block边
     *
     * @param block
     * @throws Exception
     */
    public void move(String block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.move, mckey, srm, "", block, "", "");
    }

    /**
     * 移动
     *
     * @param toLocation 移动位置
     * @throws Exception
     */
    public void move(Location toLocation) throws Exception {
        MsgSender.send03(Message03._CycleOrder.move, mckey, srm, toLocation.getLocationNo(), "", toLocation.getBay() + "", toLocation.getLevel() + "");
    }

    /**
     * 装载子车
     *
     * @param sCar       子车号
     * @param mckey      mckey
     * @param toLocation
     * @throws Exception
     */
    public void loadCar(String sCar, String mckey, Location toLocation) throws Exception {
        MsgSender.send03(Message03._CycleOrder.loadCar, mckey, srm, toLocation.getLocationNo(), sCar, "", "");
    }


    /**
     * 卸子车
     *
     * @param sCarBlcok  子车号
     * @param mckey      mckey
     * @param toLocation 目标货位
     * @throws Exception
     */
    public void unLoadCar(String sCarBlcok, String mckey, Location toLocation) throws Exception {
        SCar sCar = (SCar) Block.getByBlockNo(sCarBlcok);
        if (StringUtils.isNotBlank(sCar.getOnMCar())) {
            MsgSender.send03(Message03._CycleOrder.unloadCar, mckey, srm, toLocation.getLocationNo(), sCarBlcok, "", "");
        }
    }

    /**
     * 移动提升机接子车
     *
     * @throws Exception
     */
    public void tryLoadCar() throws Exception {
        //移动提升机上没有子车，先去接子车，去子车所在的层列
        SCar sCar = SCar.getScarByGroup(srm.getGroupNo());

        if (sCar != null && sCar.getStatus().equals("1")) {
            Location sCarLocation = Location.getByBankBayLevel(sCar.getBank(), sCar.getBay(), sCar.getLevel(), sCar.getPosition());
            if (sCarLocation != null) {
                //判断移动提升机的位置排列层，实绩位置，和目标货位是否一致
                if (srm.arrive(sCarLocation)) {
                    //移动提升机移动到指定层列，开始装载子车
                    this.loadCar(sCar.getBlockNo(), mckey, sCarLocation);
                } else {
                    //移动堆垛机移动到指定的层，列
                    this.move(sCarLocation);
                }

            } else {
                if (StringUtils.isNotBlank(srm.getMcKey())) {
                    if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                        Location location = Location.getByLocationNo(asrsJob.getToLocation());
                        if (!srm.arrive(location))
                            this.move(location);
                    }
                }
            }
        }
    }

    /**
     * 提升机去准备接货
     *
     * @throws Exception
     */
    public void tryCarryGoods() throws Exception {
        AsrsJob job = AsrsJob.getAsrsJobByMcKey(mckey);
        //移动提升机上有子车，去上一个block接货
        Block block = srm.getPreBlock(mckey, job.getType());

        if (!block.getBlockNo().equals(srm.getDock()) || !srm.getCheckLocation() == true) {
            //移动到dock
            this.move(block.getBlockNo());
        } else {
            //从block移栽取货
            this.moveCarryGoods(block.getBlockNo());
        }

    }

    /**
     * 移动提升机准备卸货
     */
    public void tryUnloadGoods() throws Exception {
        AsrsJob job = AsrsJob.getAsrsJobByMcKey(mckey);
        Block block = srm.getNextBlock(job.getType(), job.getToStation());
        if (!block.getBlockNo().equals(srm.getDock()) || !srm.getCheckLocation() == true) {
            //移动到dock
            this.move(block.getBlockNo());
        } else {
            //从block移栽卸货
            if (StringUtils.isBlank(block.getMcKey()) && StringUtils.isBlank(block.getReservedMcKey())) {
                this.moveUnloadGoods(block.getBlockNo());
                ConveyorOperator conveyorOperator = new ConveyorOperator((Conveyor) block, job.getMcKey());
                conveyorOperator.tryMoveCarryGoodsFromSrm(srm);
            }
        }
    }

    /**
     * 提升机卸子车到货位
     *
     * @param location
     */
    public void tryUnLoadCarToLocation(Location location) throws Exception {
        if (srm.getActualArea().equals(location.getActualArea())
                && srm.getLevel() == location.getLevel()
                && srm.getBay() == location.getBay()
                && srm.getCheckLocation() == true) {
            unLoadCar(srm.getsCarBlockNo(), mckey, location);
        } else {
            move(location);
        }
    }

    /**
     * 提升机卸子车到输送机
     *
     * @param nextBlock
     */
    public void tryUnLoadCarToConveyor(Conveyor nextBlock) throws Exception {
        if (nextBlock.getDock().equals(srm.getDock())) {
            MsgSender.send03(Message03._CycleOrder.unloadCar, mckey, srm, "", nextBlock.getBlockNo(), "", "");
        } else {
            move(nextBlock.getBlockNo());
        }
    }

    /**
     * 从输送机上接子车
     *
     * @param preBlock
     */
    public void tryLoadCarFromConveyor(Conveyor preBlock) throws Exception {
        if (preBlock.getDock().equals(srm.getDock())) {
            MsgSender.send03(Message03._CycleOrder.loadCar, mckey, srm, "", preBlock.getBlockNo(), "", "");
        } else {
            move(preBlock.getDock());
        }
    }

    /**
     * 堆垛机回原点
     *
     * @param srm
     * @throws Exception
     */
    public void cycle(Srm srm) throws Exception {
        MsgSender.send03(Message03._CycleOrder.move, mckey, srm, "", srm.getCycle(), "", "");
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
        if (srm.getCheckLocation() && srm.getLevel() == location.getLevel() && srm.getBay() == location.getBay()) {
            unLoadCar(sCar.getBlockNo(), reservedMcKey, location);
            scarOperator.offCar(srm.getBlockNo(), location.getLocationNo());
        } else {
            move(location);
        }

    }

    /**
     * 从充电通道接子车
     *
     * @param sCar
     * @param tempLocation
     * @throws Exception
     */
    public void tryLoadCarFromLocation(SCar sCar, Location tempLocation) throws Exception {
        if (srm.getLevel() == tempLocation.getLevel()
                && srm.getBay() == tempLocation.getBay()) {
            if (sCar.getPosition().equals(srm.getPosition())) {
                if (srm.getCheckLocation())
                    loadCar(sCar.getBlockNo(), mckey, tempLocation);
                else
                    move(tempLocation);
            }
        } else {
            move(tempLocation);
        }
    }
}
