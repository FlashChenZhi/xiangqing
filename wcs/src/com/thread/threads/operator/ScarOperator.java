package com.thread.threads.operator;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.utils.MsgSender;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2017/11/2.
 */
public class ScarOperator {

    private SCar sCar;
    private String mckey;

    public ScarOperator(SCar sCar, String mckey) {
        this.sCar = sCar;
        this.mckey = mckey;
    }

    /**
     * 子车上母车
     *
     * @param block
     * @throws Exception
     */
    public void onCar(String block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.onCar, mckey, sCar, "", block, "", "");
    }


    /**
     * 子车上移动提升机
     *
     * @param srm
     * @throws Exception
     */
    public void tryOnSrm(Srm srm) throws Exception {

        if (sCar.getLevel() == srm.getLevel()
                && sCar.getBay() == srm.getBay()) {
            MsgSender.send03(Message03._CycleOrder.onCar, mckey, sCar, "", srm.getBlockNo(), "", "");
        }
    }

    /**
     * 上提升机
     *
     * @param srm
     * @param locationNo
     * @throws Exception
     */
    public void tryOnSrm(Srm srm, String locationNo) throws Exception {

        if (sCar.getActualArea().equals(srm.getActualArea())
                && sCar.getLevel() == srm.getLevel()
                && sCar.getBay() == srm.getBay()) {
            MsgSender.send03(Message03._CycleOrder.onCar, mckey, sCar, locationNo, srm.getBlockNo(), "", "");
        }
    }


    /**
     * 子车上固定提升机
     *
     * @param block
     * @throws Exception
     */
    public void onLift(String block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.onCar, mckey, sCar, "", block, "", "");
    }

    /**
     * 移动到货位
     *
     * @param locationNo
     * @throws Exception
     */
    public void move(String locationNo) throws Exception {
        MsgSender.send03(Message03._CycleOrder.move, mckey, sCar, locationNo, "", "", "");
    }

    /**
     * 子车下母车
     * 子车下母车需要指定货位位置，用于子车判断从母车的AB面下车
     *
     * @param locationNo
     * @throws Exception
     */
    public void offCar(String srm, String locationNo) throws Exception {
        MsgSender.send03(Message03._CycleOrder.offCar, mckey, sCar, locationNo, srm, "", "");
    }

    /**
     * 子车下移动提升机
     * 子车下移动提升机需要指定位置，用于子车判断从提升机的AB面下车
     *
     * @param locationNo
     */
    public void offSrm(String locationNo) throws Exception {
        MsgSender.send03(Message03._CycleOrder.offCar, mckey, sCar, locationNo, "", "", "");
    }

    /**
     * 子车去取货
     *
     * @param locationNo
     * @throws Exception
     */
    public void pickUpGoods(String locationNo) throws Exception {
        MCar srm = MCar.getMCarByGroupNo(sCar.getGroupNo());
        MsgSender.send03(Message03._CycleOrder.pickUpGoods, mckey, sCar, locationNo, srm.getBlockNo(), "", "");
    }

    /**
     * 子车卸货
     *
     * @param locationNo
     * @throws Exception
     */
    public void unloadGoods(String locationNo) throws Exception {
        MsgSender.send03(Message03._CycleOrder.unloadGoods, mckey, sCar, locationNo, "", "", "");
    }

    /**
     * 子车卸货
     */
    public void tryUnloadGoods(Srm srm) throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
        Location location = Location.getByLocationNo(asrsJob.getToLocation());
        if (StringUtils.isNotBlank(srm.getMcKey())
                && location.getActualArea().equals(sCar.getActualArea())
                && location.getBay() == sCar.getBay()
                && sCar.getLevel() == location.getLevel()) {
            MsgSender.send03(Message03._CycleOrder.unloadGoods, mckey, sCar, location.getLocationNo(), srm.getBlockNo(), "", "");
        }
    }

    /**
     * 子车卸货
     */
    public void tryUnloadGoods(MCar srm) throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
        Location location = Location.getByLocationNo(asrsJob.getToLocation());
        if ( location.getActualArea().equals(sCar.getActualArea())
                && location.getBay() == sCar.getBay()
                && sCar.getLevel() == location.getLevel()) {
            MsgSender.send03(Message03._CycleOrder.unloadGoods, mckey, sCar, location.getLocationNo(), srm.getBlockNo(), "", "");
        }
    }


    /**
     * 子车卸货
     */
    public void tryOffCarCarryGoods(Srm srm) throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
        Location location = Location.getByLocationNo(asrsJob.getToLocation());
        if (StringUtils.isNotBlank(srm.getMcKey())
                && location.getActualArea().equals(sCar.getActualArea())
                && location.getBay() == sCar.getBay()
                && sCar.getLevel() == location.getLevel()) {
            MsgSender.send03(Message03._CycleOrder.offCarCarryGoods, mckey, sCar, location.getLocationNo(), srm.getBlockNo(), "", "");
        }
    }

    /**
     * 子车卸货
     */
    public void tryOffCarCarryGoodsFromMcar(MCar srm) throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
        Location location = Location.getByLocationNo(asrsJob.getToLocation());
        if (StringUtils.isNotBlank(srm.getMcKey())
                && location.getActualArea().equals(sCar.getActualArea())
                && location.getBay() == sCar.getBay()
                && sCar.getLevel() == location.getLevel()) {
            MsgSender.send03(Message03._CycleOrder.offCarCarryGoods, mckey, sCar, location.getLocationNo(), srm.getBlockNo(), "", "");
        }
    }

    public void tryCarryGoodsToMCar(MCar mCar)throws Exception{
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
        Location location = Location.getByLocationNo(asrsJob.getFromLocation());
        if(mCar.arrive(location)){
            MsgSender.send03(Message03._CycleOrder.onCarCarryGoods, mckey, sCar, location.getLocationNo(), mCar.getBlockNo(), "", "");
        }
    }


    /**
     * 子车尝试去取货
     *
     * @param toLocation
     */
    public void tryPickingGoods(String toLocation) throws Exception {
        Location location = Location.getByLocationNo(toLocation);
        if (location.getActualArea().equals(sCar.getActualArea())
                && location.getBay() == sCar.getBay()
                && sCar.getLevel() == sCar.getLevel()) {
            pickUpGoods(toLocation);
        }
    }

    /**
     * 子车尝试下堆垛机
     *
     * @param onMCar
     * @param location
     * @throws Exception
     */
    public void tryOffSrm(String onMCar, Location location) throws Exception {
        if (sCar.getBay() == location.getBay() && sCar.getLevel() == location.getLevel()) {
            offCar(onMCar, location.getLocationNo());
        }
    }

    /**
     * 子车跨区域上提升机
     *
     * @param srm
     * @throws Exception
     */
    public void tryOnSrmOverPosition(Srm srm, String location) throws Exception {
        if (sCar.getLevel() == srm.getLevel()
                && sCar.getBay() == srm.getBay()) {
            MsgSender.send03(Message03._CycleOrder.onCar, mckey, sCar, location, srm.getBlockNo(), "", "");
        }

    }

    /**
     * 子车充电
     *
     * @param sCar
     * @param location
     * @throws Exception
     */
    public void tryCharge(SCar sCar, Location location) throws Exception {
        MsgSender.send03(Message03._CycleOrder.charge, sCar.getMcKey(), sCar, location.getLocationNo(), "", "", "");
    }

    public void tryOnMCar(MCar mCar) throws Exception {
        if (sCar.getLevel() == mCar.getLevel()
                && sCar.getBay() == mCar.getBay()) {
            Location location = Location.getByBankBayLevel(sCar.getBank(),sCar.getBay(),sCar.getLevel(),sCar.getPosition());
            MsgSender.send03(Message03._CycleOrder.onCar, mckey, sCar, location.getLocationNo(), mCar.getBlockNo(), "", "");
        }

    }
}
