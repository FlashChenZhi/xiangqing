package com.thread.threads.service.impl.putaway;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.Srm;
import com.thread.threads.operator.SrmOperator;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import com.util.common.StringUtils;

/**
 * Created by van on 2017/11/2.
 */
public class SrmAndScarPutawayService extends SrmAndScarServiceImpl {

    private Srm srm;

    public SrmAndScarPutawayService(Block block) {
        super(block);
        srm = (Srm) block;
    }

    @Override
    public void withOutJob() throws Exception {

        if (StringUtils.isEmpty(srm.getsCarBlockNo())) {
            SrmOperator srmOperator = new SrmOperator(srm, "9999");
            srmOperator.tryLoadCar();
        }

    }

    @Override
    public void withReserveMckey() throws Exception {

        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getReservedMcKey());
        SrmOperator operator = new SrmOperator(srm, asrsJob.getMcKey());

        if (StringUtils.isNotEmpty(srm.getsCarBlockNo())) {
            //提升机上有子车，提升机准备去接货
            operator.tryCarryGoods();
        } else {

            Location location = Location.getByLocationNo(asrsJob.getToLocation());
            if (srm.getBay() == location.getBay()
                    && srm.getLevel() == location.getLevel()) {
                //优先接货
                operator.tryCarryGoods();
            } else {
                //计算是否先取货，还是先取车，如果子车不在提升机上
                boolean flag = loadCarFirst();

                if (flag) {
                    //先去取车
                    //移动提升机上没有子车，先去接子车，去子车所在的层列
                    operator.tryLoadCar();
                } else {
                    //提升机先去接货
                    operator.tryCarryGoods();
                }
            }
        }
    }

    private boolean loadCarFirst() {
        int t1 = 0; //t1=往返距离÷60m/min+1秒
        int t2 = 5;
        int t3_1 = 0;//    t3ˊ=行走距离÷160m/min+1秒
        int t3_2 = 0;//    t3〞=升降距离÷40m/min+1秒
        int t3 = t3_1 > t3_2 ? t3_1 : t3_2;
        int t4 = 7;
        int t5_1 = 0;//     t5ˊ=行走距离÷160m/min+1秒
        int t5_2 = 0;//     t5〞=升降距离÷40m/min+1秒
        int t5 = t5_1 > t5_2 ? t5_1 : t5_2;
        int time1 = t1 + t2 + t3 + t4 + t5;

        int T1 = 0;
        int T2 = 7;
        int T3 = 0;
        int T4 = 0;
        int Temp1 = (T1 + T2 + T3) > T4 ? (T1 + T2 + T3) : T4;
        int T5 = 5;
        int T6 = 0;
        int time2 = Temp1 + T5 + T6;

        return time1 > time2;
    }

    @Override
    public void withMckey() throws Exception {

        //移动提升机正在执行任务
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getMcKey());

        SrmOperator operator = new SrmOperator(srm, asrsJob.getMcKey());

        Location toLocation = Location.getByLocationNo(asrsJob.getToLocation());

        if (StringUtils.isNotEmpty(srm.getsCarBlockNo())) {
            //移动提升机上有子车
            //判断移动提升机的位置排列层，实绩位置，和目标货位是否一致
            if (srm.arrive(toLocation)) {
                //移动提升机移动到指定层列，开始卸子车
                operator.unLoadCar(srm.getsCarBlockNo(), asrsJob.getMcKey(), toLocation);
            } else {
                //移动提升机移动到指定的层，列
                operator.move(toLocation);
            }

        } else {
            //提升机去找子车，接子车
            operator.tryLoadCar();
        }

    }
}
