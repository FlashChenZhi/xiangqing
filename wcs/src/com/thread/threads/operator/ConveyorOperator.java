package com.thread.threads.operator;

import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.utils.MsgSender;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

/**
 * Created by van on 2017/11/2.
 * 输送机操作类
 */
public class ConveyorOperator {

    private Conveyor conveyor;
    private String mckey;

    public ConveyorOperator(Conveyor conveyor, String mckey) {
        this.conveyor = conveyor;
        this.mckey = mckey;
    }

    /**
     * 移动 输送机
     * <p>
     * 如果下一节输送机不是等待回复状态，并且没有mckey。发送命令，移动到下一节输送机
     *
     * @param nextBlock
     * @throws Exception
     */
    public void tryMoveToAnotherCrane(Block nextBlock) throws Exception {
        Conveyor coy = (Conveyor) nextBlock;
        if (coy.isManty()) {
            MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, conveyor, "", nextBlock.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, nextBlock, "", conveyor.getBlockNo(), "", "");
        } else {
            if (!nextBlock.isWaitingResponse() && StringUtils.isBlank(nextBlock.getMcKey()) && StringUtils.isBlank(nextBlock.getReservedMcKey())) {
                MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, conveyor, "", nextBlock.getBlockNo(), "", "");
                MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, nextBlock, "", conveyor.getBlockNo(), "", "");
            }
        }
    }

    /**
     * 输送机移栽卸货到电梯
     * <p>
     * 如果电梯的dock和输送机的dock一致
     *
     * @param nextBlock
     */
    public void tryMoveUnloadGoodsToLift(Lift nextBlock) throws Exception {
        if (mckey.equals(nextBlock.getReservedMcKey())) {
            if (nextBlock.getDock() != null && nextBlock.getDock().equals(conveyor.getBlockNo())) {
                MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, conveyor, "", nextBlock.getBlockNo(), "", "");
            }
        }
    }

    /**
     * 输送机移栽卸货到移动升降机
     * <p>
     * 如果移动升降机的dock和输送机的dock一致
     *
     * @param nextBlock
     */
    public void tryMoveUnloadGoodsToSrm(Srm nextBlock) throws Exception {
        if (nextBlock.getDock() != null && nextBlock.getDock().equals(conveyor.getBlockNo()) && StringUtils.isBlank(nextBlock.getMcKey())) {
            MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, conveyor, "", nextBlock.getBlockNo(), "", "");
        }
    }

    /**
     * 输送机移栽卸货到母车
     * <p>
     * 如果母车的dock和输送机的dock一致
     *
     * @param nextBlock
     */
    public void tryMoveUnloadGoodsToMCar(MCar nextBlock) throws Exception {
        if (nextBlock.getDock() != null
                && nextBlock.getDock().equals(conveyor.getBlockNo())
                && StringUtils.isBlank(nextBlock.getMcKey())
                && mckey.equals(nextBlock.getReservedMcKey())) {
            MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, conveyor, "", nextBlock.getBlockNo(), "", "");
        }
    }

    /**
     * 输送移栽卸货到站台
     * <p>
     * 如果站台不是等待回复状态，并且没有mckey。发送命令
     *
     * @param nextBlock
     * @throws Exception
     */
    public void tryMoveUnloadGoodsToStation(StationBlock nextBlock) throws Exception {
        if (StringUtils.isBlank(nextBlock.getMcKey())) {
            MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, conveyor, "", nextBlock.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, nextBlock, "", conveyor.getBlockNo(), "", "");
        }
    }

    /**
     * 从移动提升机上移栽取货
     * 如果移动提升机的dock和输送机的dock一致
     *
     * @throws Exception
     */
    public void tryMoveCarryGoodsFromSrm(Srm preBlock) throws Exception {
        if (preBlock.getDock() != null && preBlock.getDock().equals(conveyor.getBlockNo())) {
            MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, conveyor, "", preBlock.getBlockNo(), "", "");
        }
    }


    /**
     * 从母车上移栽取货
     * 如果移动提升机的dock和输送机的dock一致
     *
     * @throws Exception
     */
    public void tryMoveCarryGoodsFromMcar(MCar preBlock) throws Exception {
        if (preBlock.getDock() != null && preBlock.getDock().equals(conveyor.getBlockNo())) {
            MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, conveyor, "", preBlock.getBlockNo(), "", "");
        }
    }


    /**
     * 输送机卸子车到另一节输送机
     *
     * @param block
     * @throws Exception
     */
    public void tryUnloadScarToConveyor(Conveyor block) throws Exception {
        if (StringUtils.isBlank(block.getMcKey())
                && StringUtils.isBlank(block.getReservedMcKey())) {
            MsgSender.send03(Message03._CycleOrder.unloadCar, mckey, conveyor, "", block.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.loadCar, mckey, block, "", conveyor.getBlockNo(), "", "");
        }
    }

    /**
     * 输送机卸子车到移动提升机
     *
     * @param nextBlock
     */
    public void tryUnloadScarToSrm(Srm nextBlock) throws Exception {
        if (StringUtils.isNotBlank(nextBlock.getReservedMcKey())) {
            if (nextBlock.getDock().equals(conveyor.getDock())) {
                MsgSender.send03(Message03._CycleOrder.unloadCar, mckey, conveyor, "", nextBlock.getBlockNo(), "", "");
            }
        }
    }

    /**
     * @param lift
     */
    public void tryUnLoadCarToLift(Lift lift) throws Exception {

        Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where reservedMcKey =:mckey").setParameter("mckey", mckey).setMaxResults(1);
        SCar sCar = (SCar) query.uniqueResult();

        if (conveyor.getBlockNo().equals(lift.getDock())) {
        MsgSender.send03(Message03._CycleOrder.unloadCar, mckey, conveyor, "", sCar.getBlockNo(), "", "");
        MsgSender.send03(Message03._CycleOrder.onCar, mckey, sCar, "", lift.getBlockNo(), "", "");
        MsgSender.send03(Message03._CycleOrder.loadCar, mckey, lift, "", conveyor.getBlockNo(), "", "");

        } else {
            if(!lift.isWaitingResponse()&&!sCar.isWaitingResponse())
            MsgSender.send03(Message03._CycleOrder.move, mckey, lift, "", conveyor.getBlockNo(), "", "");
        }
    }

    public void tryUnLoadCarToMCar(MCar mCar) throws Exception {
        Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where reservedMcKey =:mckey").setParameter("mckey", mckey).setMaxResults(1);
        SCar sCar = (SCar) query.uniqueResult();
        if (conveyor.getBlockNo().equals(mCar.getDock())) {
            MsgSender.send03(Message03._CycleOrder.unloadCar, mckey, conveyor, "", sCar.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.onCar, mckey, sCar, "", mCar.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.loadCar, mckey, mCar, "", sCar.getBlockNo(), "", "");

        } else {
            if(!mCar.isWaitingResponse()&&!sCar.isWaitingResponse())
                MsgSender.send03(Message03._CycleOrder.move, mckey, mCar, "", conveyor.getBlockNo(), "", "");

        }

    }
}
