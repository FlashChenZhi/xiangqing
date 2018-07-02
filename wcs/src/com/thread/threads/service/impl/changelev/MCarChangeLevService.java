package com.thread.threads.service.impl.changelev;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.ScarChargeLocation;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.threads.operator.MCarOperator;
import com.thread.threads.service.impl.MCarServiceImpl;

/**
 * Created by van on 2018/3/15.
 */
public class MCarChangeLevService extends MCarServiceImpl {

    private MCar mCar;

    public MCarChangeLevService(MCar mCar) {
        super(mCar);
        this.mCar = mCar;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
        MCarOperator operator = new MCarOperator(mCar, mCar.getReservedMcKey());
        if(mCar.getGroupNo()!=null){
            SCar chargeSCar = SCar.getScarByGroup(Integer.parseInt(asrsJob.getBarcode()));
            SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());
            if(chargeSCar!=null){
                ScarChargeLocation scarChargeLocation = ScarChargeLocation.getReservedChargeLocationBySCarBlockNo(chargeSCar.getBlockNo());
                if(scarChargeLocation!=null){
                    if(mCar.getsCarBlockNo()!=null && sCar.getOnMCar()!=null &&mCar.getsCarBlockNo().equals(sCar.getBlockNo())){
                        Location location =Location.getByLocationNo(sCar.getTempLocation());
                        operator.tryUnLoadCarToLocation(location);
                    }else if(mCar.getsCarBlockNo()==null && sCar.getOnMCar()==null){
                        Block block = mCar.getPreBlock(asrsJob.getMcKey(), AsrsJobType.CHANGELEVEL);
                        operator.tryLoadFromLift( block);
                    }
                }
            }
        }else{
            Block block = mCar.getPreBlock(asrsJob.getMcKey(), AsrsJobType.CHANGELEVEL);
            operator.tryLoadFromLift( block);
        }

    }

    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
        MCarOperator operator = new MCarOperator(mCar, mCar.getMcKey());
        Block block = mCar.getNextBlock(AsrsJobType.CHANGELEVEL, asrsJob.getToStation());
        operator.tryLoadToConvery( block);
    }
}
