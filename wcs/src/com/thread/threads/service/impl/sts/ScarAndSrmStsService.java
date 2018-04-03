package com.thread.threads.service.impl.sts;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.impl.ScarAndSrmServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2018/1/4.
 */
public class ScarAndSrmStsService extends ScarAndSrmServiceImpl {

    private SCar sCar;

    public ScarAndSrmStsService(SCar sCar) {
        super(sCar);
        this.sCar = sCar;
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
        if (StringUtils.isBlank(sCar.getOnMCar())) {
            operator.tryPickingGoods(asrsJob.getFromLocation());
        } else {
            Srm srm = (Srm) Block.getByBlockNo(sCar.getOnMCar());
            operator.tryUnloadGoods(srm);
        }

    }
}
