package com.thread.threads.service.impl.retrieval;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.impl.ScarAndSrmServiceImpl;

/**
 * Created by van on 2017/11/21.
 */
public class ScarAndSrmRetrievalService extends ScarAndSrmServiceImpl {

    private SCar sCar;

    public ScarAndSrmRetrievalService(SCar sCar) {
        super(sCar);
        this.sCar = sCar;
    }

    @Override
    public void withOutJob() throws Exception {


    }

    @Override
    public void withReserveMckey() throws Exception {

        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
        ScarOperator operator = new ScarOperator(sCar, asrsJob.getMcKey());
        MCar srm = (MCar) Block.getByBlockNo(sCar.getOnMCar());
        Location location = Location.getByLocationNo(asrsJob.getFromLocation());
        if (srm != null)
            operator.tryOffSrm(srm.getBlockNo(), location);

    }


    @Override
    public void withMckey() throws Exception {

        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
        ScarOperator operator = new ScarOperator(sCar, asrsJob.getMcKey());
        if("0".equals(sCar.getLoad())) {
            operator.tryPickingGoods(asrsJob.getFromLocation());
        }else {
            MCar mCar = (MCar) MCar.getByBlockNo(asrsJob.getFromStation());
            operator.tryCarryGoodsToMCar(mCar);
        }


    }
}
