package com.thread.threads.service.impl.charageover;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.ScarChargeLocation;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
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
        Location location = Location.getByLocationNo(sCar.getTempLocation());
        ScarOperator scarOperator = new ScarOperator(sCar, sCar.getReservedMcKey());
        MCar mCar = MCar.getMCarByGroupNo(sCar.getGroupNo());
        scarOperator.tryOffSrm(mCar.getBlockNo(), location);
    }

    @Override
    public void withMckey() throws Exception {
        ScarOperator scarOperator = new ScarOperator(sCar, sCar.getMcKey());
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
        Block block =  Block.getByBlockNo(asrsJob.getToStation());
        if(block instanceof Srm){
            Srm endSrm = (Srm) block;
            Srm srm = Srm.getSrmByPosition(sCar.getPosition());
            if (!sCar.getPosition().equals(endSrm.getPosition())) {
                if (sCar.getBay() == 1) {
                    if (StringUtils.isBlank(sCar.getOnMCar())) {
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
        }else if(block instanceof MCar){
            MCar mCar = (MCar) block;

            ScarChargeLocation scarChargeLocation = ScarChargeLocation.getReservedChargeLocationBySCarBlockNo(sCar.getBlockNo());
            Location chargeLocation = scarChargeLocation.getChargeLocation();

            if(chargeLocation!=null){
                if(SCar.STATUS_CHARGE_OVER.equals(sCar.getStatus()) ){
                    //若小车处于充电完成状态
                    scarOperator.tryChargeFinish(sCar, chargeLocation);
                }else if(SCar.STATUS_RUN.equals(sCar.getStatus())){
                    //若小车处于运行状态（已到巷道口）
                    if(StringUtils.isBlank(mCar.getsCarBlockNo()) && chargeLocation.getLevel()==mCar.getLevel()
                            && chargeLocation.getBay()==mCar.getBay() ){
                        scarOperator.tryOnMCar(mCar);
                    }
                }

            }
        }
    }
}
