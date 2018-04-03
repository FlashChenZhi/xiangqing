package com.thread.threads.service.impl.retrieval;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.threads.operator.MCarOperator;
import com.thread.threads.operator.SrmOperator;
import com.thread.threads.service.impl.MCarServiceImpl;
import com.util.common.StringUtils;

/**
 * Created by van on 2018/2/22.
 */
public class MCarAndScarRetrievalService extends MCarServiceImpl {
    private MCar mCar;

    public MCarAndScarRetrievalService(MCar mCar) {
        super(mCar);
        this.mCar = mCar;
    }

    @Override
    public void withReserveMckey() throws Exception {

        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
        MCarOperator operator = new MCarOperator(mCar, asrsJob.getMcKey());

        Location toLocation = Location.getByLocationNo(asrsJob.getFromLocation());

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
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

        if (org.apache.commons.lang3.StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
            SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());

            if (org.apache.commons.lang3.StringUtils.isNotBlank(sCar.getReservedMcKey())
                    && !sCar.getReservedMcKey().equals(mCar.getMcKey())) {

                AsrsJob newJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                Location newLocation = Location.getByLocationNo(newJob.getFromLocation());

                if (newLocation.getLevel() == mCar.getLevel()
                        && newLocation.getBay() == mCar.getBay()) {
                    //优先卸子车
                    operator.unLoadCarFirst(sCar, sCar.getReservedMcKey());
                } else {
                    //是否先卸子车
                    boolean flag = unLoadCarFirst();
                    if (flag) {
                        operator.unLoadCarFirst(sCar, sCar.getReservedMcKey());
                    } else {
                        //有限卸货
                        operator.tryUnloadGoods();
                    }
                }
            } else {

                if (sCar.isWaitingResponse()) {
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(sCar.getMcKey())) {

                    } else {
                        AsrsJob newJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                        Location newLocation = Location.getByLocationNo(newJob.getFromLocation());

                        if (newLocation.getLevel() == mCar.getLevel()
                                && newLocation.getBay() == mCar.getBay()) {
                            //优先卸子车
                            operator.unLoadCarFirst(sCar, sCar.getReservedMcKey());
                        } else {
                            //是否先卸子车
                            boolean flag = unLoadCarFirst();
                            if (flag) {
                                operator.unLoadCarFirst(sCar, sCar.getReservedMcKey());
                            } else {
                                //有限卸货
                                operator.tryUnloadGoods();
                            }
                        }

                    }
                } else {
                    //移动升降机上没有子车
                    operator.tryUnloadGoods();
                }
            }


        } else {
            //移动升降机上没有子车
            operator.tryUnloadGoods();
        }
    }

    private boolean unLoadCarFirst() {
        return true;
    }


}
