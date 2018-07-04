package com.asrs.business.msgProc.msg35ProServiceImpl;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.msgProc.msg35ProcService.Msg35ProcService;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message35;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: ed_chen
 * @Date: Create in 23:21 2018/6/21
 * @Description:
 * @Modified By:
 */
public class Msg35ProcRechargedServiceImpl implements Msg35ProcService {
    private Message35 message35;
    private AsrsJob aj;
    private Block block;

    public Msg35ProcRechargedServiceImpl(Message35 message35, AsrsJob aj, Block block) {
        this.message35 = message35;
        this.aj = aj;
        this.block = block;
    }

    @Override
    public void sCar35Proc() throws Exception {
        SCar sCar = (SCar) block;

        if (message35.isMove()) {
            Srm srm = (Srm) Srm.getByBlockNo(aj.getToStation());
            sCar.setPosition(srm.getPosition());

        } else if (message35.isOffCar()) {

            if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                sCar.setStatus(SCar.STATUS_CHARGE);
            }
            sCar.setOnMCar(null);
            sCar.setReservedMcKey(null);
            sCar.setBank(Integer.parseInt(message35.Bank));

        } else if (message35.isCharge()) {

            sCar.setMcKey(null);
            aj.setStatus(AsrsJobStatus.DONE);
            sCar.setStatus(SCar.STATUS_CHARGE);

        }
    }

    @Override
    public void srm35Proc() throws Exception {
        Srm mCar = (Srm) block;

        if (message35.isMove()) {
            mCar.setBay(Integer.parseInt(message35.Bay));
            mCar.setDock(message35.Station);
            mCar.setCheckLocation(true);
            mCar.setLevel(Integer.parseInt(message35.Level));

            Location loc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), mCar.getPosition());

            if (loc != null) {
                mCar.setActualArea(loc.getActualArea());
            }

            if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                sCar.setBay(mCar.getBay());
                sCar.setLevel(mCar.getLevel());
            }

            mCar.setDock(null);

        } else if (message35.isLoadCar()) {
            mCar.setsCarBlockNo(message35.Station);
            mCar.generateMckey(message35.McKey);
        } else if (message35.isUnLoadCar()) {
            mCar.setsCarBlockNo(null);
            Location location = Location.getByLocationNo(aj.getToLocation());
            //充电任务中，如果堆垛机的列是充电的这一列，那么堆垛机正常完成任务，
            if (location.getBay() == Integer.parseInt(message35.Bay)) {
                mCar.clearMckeyAndReservMckey();
                if (mCar.getPosition().equals(location.getPosition())) {
                    aj.setStatus(AsrsJobStatus.DONE);
                }
            }
            if (mCar.getBlockNo().equals(aj.getFromStation())) {
                mCar.clearMckeyAndReservMckey();
            }
        }
    }

    @Override
    public void mCar35Proc() throws Exception {
        MCar mCar = (MCar) block;

        if (message35.isMove()) {
            mCar.setBay(Integer.parseInt(message35.Bay));
            mCar.setDock(message35.Station);
            mCar.setCheckLocation(true);
//                                mCar.setLevel(Integer.parseInt(message35.Level));

            Location loc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), mCar.getPosition());

            if (loc != null) {
                mCar.setActualArea(loc.getActualArea());
            }

            if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                sCar.setBay(mCar.getBay());
                sCar.setLevel(mCar.getLevel());
            }

            mCar.setDock(null);

        } else if (message35.isLoadCar()) {
            mCar.setsCarBlockNo(message35.Station);
            mCar.generateMckey(message35.McKey);
        } else if (message35.isUnLoadCar()) {
            mCar.setsCarBlockNo(null);
            mCar.setMcKey(null);
            Location location = Location.getByLocationNo(aj.getToLocation());
            //充电任务中，如果堆垛机的列是充电的这一列，那么堆垛机正常完成任务，
            if (location.getBay() == Integer.parseInt(message35.Bay)) {
                mCar.clearMckeyAndReservMckey();
                /*if (mCar.getPosition().equals(location.getPosition())) {
                    aj.setStatus(AsrsJobStatus.DONE);
                }*/
            }
            if (mCar.getBlockNo().equals(aj.getFromStation())) {
                mCar.clearMckeyAndReservMckey();
            }
        }
    }

    @Override
    public void lift35Proc() throws Exception {

    }

    @Override
    public void converyor35Proc() throws Exception {

    }

    @Override
    public void station35Proc() throws Exception {

    }
}
