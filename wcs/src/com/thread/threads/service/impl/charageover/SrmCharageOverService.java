package com.thread.threads.service.impl.charageover;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.SrmOperator;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2017/12/23.
 */
public class SrmCharageOverService extends SrmAndScarServiceImpl {
    public Srm srm;

    public SrmCharageOverService(Srm block) {
        super(block);
        this.srm = block;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getReservedMcKey());
        if (srm.getBlockNo().equals(asrsJob.getToStation())) {
            SCar sCar = SCar.getScarByGroup(srm.getGroupNo());
            Location location = Location.getByLocationNo(sCar.getChargeChanel());
            SrmOperator srmOperator = new SrmOperator(srm, srm.getReservedMcKey());
            srmOperator.tryLoadCarFromLocation(sCar, location);
        } else {
            Srm endStation = (Srm) Srm.getByBlockNo(asrsJob.getToStation());
            SCar sCar = SCar.getScarByGroup(endStation.getGroupNo());
            Location location = Location.getByLocationNo(sCar.getChargeLocation());
            SrmOperator srmOperator = new SrmOperator(srm, srm.getReservedMcKey());
            srmOperator.tryLoadCarFromLocation(sCar, location);

        }
    }

    @Override
    public void withMckey() throws Exception {
        //堆垛机存在充电任务，堆垛机在充电位置一边
        SrmOperator srmOperator = new SrmOperator(srm, srm.getMcKey());
        if (StringUtils.isBlank(srm.getsCarBlockNo())) {
            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getMcKey());
            //堆垛机上没有子车
            Srm tempSrm = (Srm) Block.getByBlockNo(asrsJob.getToStation());
            SCar sCar = SCar.getScarByGroup(tempSrm.getGroupNo());
            Location location = Location.getByLocationNo(sCar.getChargeChanel());
            srmOperator.tryLoadCarFromLocation(sCar, location);
        } else {
            SCar sCar = SCar.getScarByGroup(srm.getGroupNo());
            Location tempLocation = Location.getByLocationNo(sCar.getChargeChanel());
            srmOperator.tryUnLoadCarToLocation(tempLocation);
        }

    }
}
