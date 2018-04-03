package com.thread.threads.service.impl.charageover;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.impl.ScarAndSrmServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2017/12/23.
 */
public class ScarCharageOverService extends ScarAndSrmServiceImpl {
    private SCar sCar;

    public ScarCharageOverService(SCar sCar) {
        super(sCar);
        this.sCar = sCar;
    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {
        ScarOperator scarOperator = new ScarOperator(sCar, sCar.getMcKey());
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
        Srm endSrm = (Srm) Block.getByBlockNo(asrsJob.getToStation());

        Srm srm = Srm.getSrmByPosition(sCar.getPosition());
        if (!sCar.getPosition().equals(endSrm.getPosition())) {
            if (sCar.getBay() == 1) {
                if (StringUtils.isBlank(sCar.getOnMCar())) {
//                    Location location = Location.getByLocationNo(Const.CHARGE_LOCATION);
                    scarOperator.tryOnSrm(srm, sCar.getChargeLocation());
                }
            } else {
                Location location = Location.getByLocationNo(sCar.getChargeChanel());
                if (StringUtils.isNotBlank(sCar.getOnMCar())) {
                    scarOperator.tryOffSrm(srm.getBlockNo(), location);
                } else {
                    scarOperator.move(location.getLocationNo());
                }
            }
        } else {
            scarOperator.tryOnSrm(srm, sCar.getChargeChanel());

        }

    }
}
