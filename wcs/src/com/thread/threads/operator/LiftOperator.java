package com.thread.threads.operator;

import com.asrs.message.Message03;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.thread.blocks.Lift;
import com.thread.blocks.SCar;
import com.thread.utils.MsgSender;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;

/**
 * Created by van on 2017/11/2.
 */
public class LiftOperator {

    private Lift lift;
    private String mckey;

    public LiftOperator(Lift lift, String mckey) {
        this.lift = lift;
        this.mckey = mckey;
    }

    /**
     * 移栽取货
     *
     * @param block
     * @throws Exception
     */
    private void moveCarryGoods(String block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, lift, "", block, "", "");
    }

    /**
     * 移栽卸货
     *
     * @param block
     * @throws Exception
     */
    private void moveUnLoadGoods(String block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, lift, "", block, "", "");
    }

    /**
     * 移动
     *
     * @param block
     * @throws Exception
     */
    public void move(String block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.move, mckey, lift, "", block, "", "");
    }

    /**
     * 从输送机取货
     *
     * @param block
     * @throws Exception
     */
    public void tryCarryFromConvery(Block block) throws Exception {
        if (block.getBlockNo().equals(lift.getDock())) {
            moveCarryGoods(block.getBlockNo());
        } else {
            move(block.getBlockNo());
        }
    }

    /**
     * 往输送机卸货
     *
     * @param block
     * @throws Exception
     */
    public void tryUnloadGoodsToConvery(Block block) throws Exception {
        if (block.getBlockNo().equals(lift.getDock())) {
            if (StringUtils.isEmpty(block.getMcKey()) && StringUtils.isEmpty(block.getReservedMcKey())) {
                moveUnLoadGoods(block.getBlockNo());
                MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, block, "", lift.getBlockNo(), "", "");
            }
        } else {
            move(block.getBlockNo());
        }
    }

    /**
     * 提升机卸子车到输送机
     *
     * @param conveyor
     */
    public void tryUnLoadCarToConveyor(Conveyor conveyor) throws Exception {
        Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where mcKey =:mckey").setParameter("mckey", mckey).setMaxResults(1);
        SCar sCar = (SCar) query.uniqueResult();
        if (conveyor.getBlockNo().equals(lift.getDock())) {
            MsgSender.send03(Message03._CycleOrder.unloadCar, mckey, lift, "", sCar.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.offCar, mckey, sCar, "", conveyor.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.loadCar, mckey, conveyor, "", sCar.getBlockNo(), "", "");
        } else {
            if(!sCar.isWaitingResponse())
            MsgSender.send03(Message03._CycleOrder.move, mckey, lift, "", conveyor.getBlockNo(), "", "");

        }
    }


}
