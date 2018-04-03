package com.thread.threads.service.impl.charage;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.SCar;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.impl.ScarAndSrmServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2017/12/13.
 */
public class ScarCharageService extends ScarAndSrmServiceImpl {

    private SCar sCar;

    public ScarCharageService(SCar sCar) {
        super(sCar);
        this.sCar = sCar;
    }

    @Override
    public void withReserveMckey() throws Exception {

        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
        Location location = Location.getByLocationNo(sCar.getTempLocation());
        ScarOperator scarOperator = new ScarOperator(sCar, asrsJob.getMcKey());
        scarOperator.tryOffSrm(sCar.getOnMCar(), location);

    }

    @Override
    public void withMckey() throws Exception {

        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
        Location location = Location.getByLocationNo(asrsJob.getToLocation());
        ScarOperator scarOperator = new ScarOperator(sCar, asrsJob.getMcKey());
        Location temLocation = Location.getByLocationNo(sCar.getChargeChanel());

        if (sCar.getPosition().equals(location.getPosition())) {
            //充电子车和充电货位在一边
            if (StringUtils.isNotBlank(sCar.getOnMCar())) {
                //子车在母车上
                scarOperator.tryOffSrm(sCar.getOnMCar(), location);
            } else {
                //子车不在堆垛机上，子车已经下车在充电位置上了。发送充电
                scarOperator.tryCharge(sCar,location);
            }

        } else {
            //充电子车和充电货位不在一边
            Location chanel = Location.getByLocationNo(sCar.getChargeChanel());
            if (StringUtils.isNotBlank(sCar.getOnMCar())) {
                scarOperator.tryOffSrm(sCar.getOnMCar(), chanel);
            } else {
                scarOperator.move(chanel.getLocationNo());
            }
        }

    }
}
