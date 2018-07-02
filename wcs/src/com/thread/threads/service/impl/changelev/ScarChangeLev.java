package com.thread.threads.service.impl.changelev;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.SCar;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.impl.ScarAndMCarServiceImpl;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by van on 2018/3/15.
 */
public class ScarChangeLev extends ScarAndMCarServiceImpl {

    private SCar sCar;
    public ScarChangeLev(SCar sCar) {
        super(sCar);
        this.sCar = sCar;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
        Location location = Location.getByLocationNo(sCar.getTempLocation());
        ScarOperator scarOperator = new ScarOperator(sCar, asrsJob.getMcKey());
        if(StringUtils.isNotBlank(sCar.getOnMCar())){
            scarOperator.tryOffSrm(sCar.getOnMCar(), location);
        }

    }

    @Override
    public void withMckey() throws Exception {

    }

}
