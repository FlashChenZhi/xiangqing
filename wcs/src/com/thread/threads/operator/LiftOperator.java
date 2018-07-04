package com.thread.threads.operator;

import com.asrs.message.Message03;
import com.thread.blocks.*;
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

    /*
     * @author：ed_chen
     * @date：2018/6/24 23:44
     * @description：移动到dock
     * @param block
     * @return：void
     */
    public void moveToDock(Block block) throws Exception {
        String dock=block.getDock(block.getBlockNo(), lift.getBlockNo());
        MsgSender.send03(Message03._CycleOrder.move, mckey, lift, "", dock, "", "");
    }

    /*
     * @author：ed_chen
     * @date：2018/6/24 23:44
     * @description：接子车到提升机
     * @param block
     * @return：void
     */
    public void loadSCar(String sCarBlockNo) throws Exception {
        MsgSender.send03(Message03._CycleOrder.loadCar, mckey, lift, "", sCarBlockNo, "", "");

    }

    /*
     * @author：ed_chen
     * @date：2018/6/24 23:44
     * @description：卸子车从提升机
     * @param block
     * @return：void
     */
    public void unloadSCar(SCar sCar,Block block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.unloadCar, mckey, lift, "", sCar.getBlockNo(), "", "");
        MsgSender.send03(Message03._CycleOrder.onCar, mckey, sCar, "", block.getBlockNo(), "", "");
    }

    /**
     * 从输送机取货
     *
     * @param block
     * @throws Exception
     */
    public void tryCarryFromConvery(Block block) throws Exception {
        if (isMoveSuccess(block) && !lift.isWaitingResponse()) {
            moveCarryGoods(block.getBlockNo());
        } else {
            //move(block.getDock(block.getBlockNo(), lift.getBlockNo()));
            moveToDock(block);
        }
    }
    /*
     * @author：ed_chen
     * @date：2018/6/26 17:40
     * @description：尝试从上一个block取货
     * @param block
     * @return：void
     */
    public void tryCarryFromPreBlock(Block block) throws Exception {
        if (isMoveSuccess(block) && !lift.isWaitingResponse() ) {
            if(block instanceof MCar){
                if(((MCar)block).getDock()!=null && ((MCar)block).getDock().equals(lift.getBlockNo()) ){
                    moveCarryGoods(block.getBlockNo());
                }
            }else{
                moveCarryGoods(block.getBlockNo());
            }
        } else {
            //move(block.getDock(block.getBlockNo(), lift.getBlockNo()));
            moveToDock(block);
        }
    }

    /**
     * 往输送机卸货
     *
     * @param block
     * @throws Exception
     */
    public void tryUnloadGoodsToConvery(Block block) throws Exception {
        if (isMoveSuccess(block) && !lift.isWaitingResponse()) {
            /*if (StringUtils.isEmpty(block.getMcKey()) && StringUtils.isEmpty(block.getReservedMcKey())) {
                moveUnLoadGoods(block.getBlockNo());
                MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, block, "", lift.getBlockNo(), "", "");
            }*/

            Conveyor coy = (Conveyor) block;
            if (coy.isManty()) {
                MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, lift, "", block.getBlockNo(), "", "");
                MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, block, "", lift.getBlockNo(), "", "");
            } else {
                if (!block.isWaitingResponse() && org.apache.commons.lang3.StringUtils.isBlank(block.getMcKey()) && org.apache.commons.lang3.StringUtils.isBlank(block.getReservedMcKey())) {
                    MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, lift, "", block.getBlockNo(), "", "");
                    MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, block, "", lift.getBlockNo(), "", "");
                }
            }

        } else {
            //move(block.getBlockNo());
            moveToDock(block);
        }
    }

    /**
     * 往母车卸货
     *
     * @param block
     * @throws Exception
     */
    public void tryUnloadGoodsToMCar(Block block) throws Exception {
        if (isMoveSuccess(block) && !lift.isWaitingResponse() ) {
            /*if (StringUtils.isEmpty(block.getMcKey()) && StringUtils.isEmpty(block.getReservedMcKey())) {
                moveUnLoadGoods(block.getBlockNo());
                MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, block, "", lift.getBlockNo(), "", "");
            }*/
            if(block instanceof MCar){
                if(((MCar)block).getDock().equals(lift.getBlockNo())){
                    moveUnLoadGoods(block.getBlockNo());
                }
            }else{
                moveUnLoadGoods(block.getBlockNo());
            }
        } else {
            moveToDock(block);
        }
    }

    /**
     * 提升机卸子车到输送机
     *
     * @param conveyor
     */
    public void tryUnLoadCarToConveyor(Conveyor conveyor) throws Exception {
        //Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where mcKey =:mckey").setParameter("mckey", mckey).setMaxResults(1);
        //修改
        Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where reservedMcKey =:mckey").setParameter("mckey", mckey).setMaxResults(1);
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

    /*
     * @author：ed_chen
     * @date：2018/6/25 0:36
     * @description：提升机从母车上接子车
     * @param block
     * @return：void
     */
    public void tryLoadCarFromMCar(Block block) throws Exception {
        //Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where mcKey =:mckey").setParameter("mckey", mckey).setMaxResults(1);
        //修改
        if(block instanceof MCar && StringUtils.isNotEmpty(((MCar) block).getsCarBlockNo()) ){
            if (isMoveSuccess(block) && !lift.isWaitingResponse()) {
                loadSCar(((MCar) block).getsCarBlockNo());
            } else {
                //move(block.getDock(block.getBlockNo(), lift.getBlockNo()));

                moveToDock(block);
            }
        }
    }

    /*
     * @author：ed_chen
     * @date：2018/6/25 0:36
     * @description：提升机卸子车到母车
     * @param block
     * @return：void
     */
    public void tryUnLoadCarToMCar(Block block) throws Exception {
        //Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where mcKey =:mckey").setParameter("mckey", mckey).setMaxResults(1);
        //修改
        Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where mcKey =:mckey").setParameter("mckey", mckey).setMaxResults(1);
        SCar sCar = (SCar) query.uniqueResult();
        if(block instanceof MCar && StringUtils.isEmpty(((MCar) block).getsCarBlockNo()) ){
            if (isMoveSuccess(block) && !lift.isWaitingResponse()) {
                if(lift.getBlockNo().equals(((MCar) block).getDock())){
                    unloadSCar(sCar,block);
                }
            } else {
                //move(block.getDock(block.getBlockNo(), lift.getBlockNo()));
                if(!sCar.isWaitingResponse())
                moveToDock(block);
            }
        }
    }
    /*
     * @author：ed_chen
     * @date：2018/6/24 23:43
     * @description：判断是否移动到位
     * @param block
     * @return：boolean
     */
    public boolean isMoveSuccess(Block block) throws Exception {
        if (block.getBlockNo().equals(lift.getDock())||block.getDock(block.getBlockNo(), lift.getBlockNo()).equals(lift.getDock())) {
            return true;
        } else {
           return false;
        }
    }

}
