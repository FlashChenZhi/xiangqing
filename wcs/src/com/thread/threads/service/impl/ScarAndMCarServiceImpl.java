package com.thread.threads.service.impl;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.ScarService;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2017/10/30.
 */
public class ScarAndMCarServiceImpl implements ScarService {

    private SCar sCar;

    public ScarAndMCarServiceImpl(SCar sCar) {
        this.sCar = sCar;
    }

    @Override
    public void withOutJob() throws Exception {
        //子车在母车上
        if (StringUtils.isNotBlank(sCar.getOnMCar())) {
            MCar mCar = (MCar) MCar.getByBlockNo(sCar.getOnMCar());
            if (StringUtils.isNotBlank(mCar.getMcKey()) ) {
                //如果提升机上有任务，
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
                if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)
                        && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING)
                        && asrsJob.getToStation().equals(mCar.getBlockNo())) {
                    //如果提升机存在入库任务，子车设置reservedmckey，出库任务不管，默认子车已经取货完成上提升机了
                    sCar.setReservedMcKey(mCar.getMcKey());
                    asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                }

            } else if (StringUtils.isNotBlank(mCar.getReservedMcKey())) {

                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
                //如果提升机上又预约任务
                if (StringUtils.isNotBlank(mCar.getsCarBlockNo()) && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING) ) {
                    if(!asrsJob.getType().equals(AsrsJobType.ST2ST)) {
                        if(mCar.getBlockNo().equals(asrsJob.getToStation())
                                || mCar.getBlockNo().equals(asrsJob.getFromStation())) {
                            sCar.setReservedMcKey(mCar.getReservedMcKey());
                            asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                        }
                    }
                }
            }

        } else {

            MCar mCar = MCar.getMCarByGroupNo(sCar.getGroupNo());
            boolean hasJob = false;
            //如果提升机上有任务，
            if (mCar != null && StringUtils.isNotBlank(mCar.getMcKey())) {
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
                if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)
                        && asrsJob.getToStation().equals(mCar.getBlockNo())
                        && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING)) {
                    //如果提升机存在入库任务，子车设置reservedmckey，出库任务不管，默认子车已经取货完成上提升机了
                    //充电状态不是完成状态
                    sCar.setReservedMcKey(mCar.getMcKey());
                    asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                    hasJob = true;
                }
            }

            //,上车
            if (!hasJob && mCar != null) {
                ScarOperator operator = new ScarOperator(sCar, "9999");
                operator.tryOnMCar(mCar);
            }

        }

    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {

    }
}
