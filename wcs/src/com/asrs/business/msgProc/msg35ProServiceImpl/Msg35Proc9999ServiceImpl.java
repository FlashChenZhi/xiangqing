package com.asrs.business.msgProc.msg35ProServiceImpl;

import com.asrs.business.msgProc.msg35ProcService.Msg35ProcService;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message35;
import com.thread.blocks.*;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: ed_chen
 * @Date: Create in 23:37 2018/6/21
 * @Description:
 * @Modified By:
 */
public class Msg35Proc9999ServiceImpl implements Msg35ProcService {

    private Message35 message35;
    private AsrsJob aj;
    private Block block;

    public Msg35Proc9999ServiceImpl(Message35 message35, AsrsJob aj, Block block) {
        this.message35 = message35;
        this.aj = aj;
        this.block = block;
    }

    @Override
    public void sCar35Proc() throws Exception {
        SCar sCar = (SCar) block;
        if (message35.isOnCar()) {
            MCar mCar = (MCar) Block.getByBlockNo(message35.Station);
            sCar.setPosition(mCar.getPosition());
            sCar.setOnMCar(message35.Station);
            sCar.setBank(0);
        } else if (message35.isChargeFinish()) {
            //欧普照明，充电完成，不做处理，解除子车状态，后续按照正常空车上车处理
        }
    }

    @Override
    public void srm35Proc() throws Exception {
        Srm srm = (Srm) block;
        if (message35.isMove()) {
            if ("0000".equals(message35.Station) || StringUtils.isBlank(message35.Station)) {
                srm.setDock(null);
                Location loc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), srm.getPosition());
                srm.setBay(loc.getBay());
                srm.setLevel(loc.getLevel());
                srm.setActualArea(loc.getActualArea());
            } else {
                srm.setLevel(1);
                srm.setDock(message35.Station);
                srm.setBay(0);
                if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                    sCar.setLevel(1);
                    sCar.setBay(0);
                }
            }
            srm.setCheckLocation(true);
        } else if (message35.isLoadCar()) {
            srm.setsCarBlockNo(message35.Station);
        }
    }

    @Override
    public void mCar35Proc() throws Exception {
        MCar mCar = (MCar) block;
        if (message35.isMove()) {
            if ("0000".equals(message35.Station) || StringUtils.isBlank(message35.Station)) {
                mCar.setDock(null);
                Location loc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), mCar.getPosition());
                mCar.setBay(loc.getBay());
//                                    mCar.setLevel(loc.getLevel());
                mCar.setActualArea(loc.getActualArea());
            } else {
//                                    mCar.setLevel(1);
                mCar.setDock(message35.Station);
                mCar.setBay(0);
                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                    sCar.setLevel(1);
                    sCar.setBay(0);
                }
            }
            mCar.setCheckLocation(true);
        } else if (message35.isLoadCar()) {
            mCar.setsCarBlockNo(message35.Station);
        }
    }

    @Override
    public void lift35Proc() throws Exception {
        Lift lift = (Lift) block;
        if (message35.isMove()) {
            if ("9999".equals(message35.McKey)&&StringUtils.isBlank(lift.getMcKey())&&StringUtils.isBlank(lift.getReservedMcKey())) {
                lift.setDock(message35.Station);
                lift.setLevel(1);
                lift.setWaitingResponse(false);
            }
        }
    }

    @Override
    public void converyor35Proc() throws Exception {

    }

    @Override
    public void station35Proc() throws Exception {

    }
}
