package com.thread.threads.service.impl.sts;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.MCar;
import com.thread.threads.operator.MCarOperator;
import com.thread.threads.service.impl.MCarServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2018/1/4.
 */
public class MCarAndScarStsService extends MCarServiceImpl {
    private MCar mCar;

    public MCarAndScarStsService(MCar block) {

        super(block);
        this.mCar = (MCar) block;

    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
        MCarOperator operator = new MCarOperator(mCar, asrsJob.getMcKey());

        Location toLocation = Location.getByLocationNo(asrsJob.getFromLocation());

        if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
            //提升机是否到达指定位置
            if (mCar.arrive(toLocation)) {
                //移动提升机移动到指定层列，开始卸子车
                operator.unLoadCar(mCar.getsCarBlockNo(), asrsJob.getMcKey(), toLocation);
            } else {
                operator.move(toLocation);
            }
        } else {
            operator.tryLoadCar();
        }
    }

    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
        MCarOperator operator = new MCarOperator(mCar, asrsJob.getMcKey());

        if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
            //堆垛机上有子车
            Location location = Location.getByLocationNo(asrsJob.getToLocation());
            operator.tryUnLoadCarToLocation(location);
        } else {
            //提升机去找子车，接子车
            operator.tryLoadCar();
        }
    }
}
