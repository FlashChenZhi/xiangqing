package com.thread.threads.service.impl.charageover;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.ScarChargeLocation;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.threads.operator.MCarOperator;
import com.thread.threads.service.impl.MCarServiceImpl;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

/**
 * @Author: ed_chen
 * @Date: Create in 14:42 2018/7/2
 * @Description:
 * @Modified By:
 */
public class MCarCharageOverService extends MCarServiceImpl {
    private MCar mCar;

    public MCarCharageOverService(MCar block) {
        super(block);
        this.mCar = (MCar) block;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
        MCarOperator operator = new MCarOperator(mCar, mCar.getReservedMcKey());
        SCar chargeOverSCar = SCar.getScarByGroup(Integer.parseInt(asrsJob.getBarcode()));
        if(mCar.getGroupNo()!=null){
            SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());
            if(chargeOverSCar!=null){
                ScarChargeLocation scarChargeLocation = ScarChargeLocation.getReservedChargeLocationBySCarBlockNo(chargeOverSCar.getBlockNo());
                if(scarChargeLocation!=null){
                    if(mCar.getsCarBlockNo()!=null && sCar.getOnMCar()!=null &&mCar.getsCarBlockNo().equals(sCar.getBlockNo())){
                        Location location =Location.getByLocationNo(sCar.getTempLocation());
                        operator.tryUnLoadCarToLocation(location);
                    }else if(mCar.getsCarBlockNo()==null && sCar.getOnMCar()==null){
                        operator.tryLoadCar2( chargeOverSCar);
                    }
                }
            }
        }else{
            operator.tryLoadCar2( chargeOverSCar);
        }
    }

    @Override
    public void withMckey() throws Exception {

    }
}
