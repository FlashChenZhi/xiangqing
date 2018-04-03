package com.thread.threads.service.impl.retrieval;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.SrmOperator;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2017/11/2.
 */
public class SrmAndScarRetrievalService extends SrmAndScarServiceImpl {

    private Srm srm;

    public SrmAndScarRetrievalService(Block block) {
        super(block);
        srm = (Srm) block;
    }

    @Override
    public void withReserveMckey() throws Exception {

        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getReservedMcKey());
        SrmOperator operator = new SrmOperator(srm, asrsJob.getMcKey());

        Location toLocation = Location.getByLocationNo(asrsJob.getFromLocation());

        if (StringUtils.isNotEmpty(srm.getsCarBlockNo())) {
            //提升机是否到达指定位置
            if (srm.arrive(toLocation)) {
                //移动提升机移动到指定层列，开始卸子车
                operator.unLoadCar(srm.getsCarBlockNo(), asrsJob.getMcKey(), toLocation);
            } else {
                operator.move(toLocation);
            }
        } else {
            operator.tryLoadCar();
        }
    }


    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getMcKey());
        SrmOperator operator = new SrmOperator(srm, asrsJob.getMcKey());

        if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
            SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());

            if (StringUtils.isNotBlank(sCar.getReservedMcKey())
                    && !sCar.getReservedMcKey().equals(srm.getMcKey())) {

                AsrsJob newJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                Location newLocation = Location.getByLocationNo(newJob.getFromLocation());

                if (newLocation.getLevel() == srm.getLevel()
                        && newLocation.getBay() == srm.getBay()) {
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
                    if (StringUtils.isNotBlank(sCar.getMcKey())) {

                    } else {
                        AsrsJob newJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                        Location newLocation = Location.getByLocationNo(newJob.getFromLocation());

                        if (newLocation.getLevel() == srm.getLevel()
                                && newLocation.getBay() == srm.getBay()) {
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
