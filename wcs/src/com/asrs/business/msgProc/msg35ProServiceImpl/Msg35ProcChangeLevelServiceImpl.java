package com.asrs.business.msgProc.msg35ProServiceImpl;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.msgProc.msg35ProcService.Msg35ProcService;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message35;
import com.thread.blocks.*;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: ed_chen
 * @Date: Create in 23:34 2018/6/21
 * @Description:
 * @Modified By:
 */
public class Msg35ProcChangeLevelServiceImpl implements Msg35ProcService {

    private Message35 message35;
    private AsrsJob aj;
    private Block block;

    public Msg35ProcChangeLevelServiceImpl(Message35 message35, AsrsJob aj, Block block) {
        this.message35 = message35;
        this.aj = aj;
        this.block = block;
    }

    @Override
    public void sCar35Proc() throws Exception {
        SCar sCar = (SCar) block;
        if(message35.isOnCar()){
            if(message35.Station.equals(aj.getToStation())){
                sCar.clearMckeyAndReservMckey();
                sCar.setOnMCar(message35.Station);
                //
                MCar mCar = (MCar) MCar.getByBlockNo(aj.getToStation());
                mCar.setGroupNo(sCar.getGroupNo());
            }
        }else if (message35.isOffCar()) {
            if (message35.Station.equals(aj.getToStation())) {
                MCar mCar = (MCar) MCar.getByBlockNo(aj.getToStation());
                sCar.setOnMCar(mCar.getBlockNo());
                //sCar.setGroupNo(mCar.getGroupNo());
                mCar.setGroupNo(sCar.getGroupNo());
            } else {
                sCar.setOnMCar(null);
            }
        }
    }

    @Override
    public void srm35Proc() throws Exception {

    }

    @Override
    public void mCar35Proc() throws Exception {
        MCar mCar = (MCar) block;
        if (message35.isMove()) {
//           mCar.setLevel(Integer.parseInt(message35.Level));
            mCar.setBay(Integer.parseInt(message35.Bay));
            mCar.setDock(message35.Station);
            mCar.setCheckLocation(true);
            if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                sCar.setLevel(mCar.getLevel());
                sCar.setBay(mCar.getBay());
            }
        } else if (message35.isUnLoadCar()) {
            //换成卸子车，清除任务，子车,清除绑定子车
            mCar.clearMckeyAndReservMckey();
            mCar.setsCarBlockNo(null);
            mCar.setGroupNo(null);
        } else if (message35.isLoadCar()) {
            mCar.setsCarBlockNo(message35.Station);
            aj.setStatus(AsrsJobStatus.DONE);
            mCar.clearMckeyAndReservMckey();
        }

    }

    @Override
    public void lift35Proc() throws Exception {
        Lift lift = (Lift) block;
        if (message35.isLoadCar()) {
            lift.generateMckey(aj.getMcKey());
        } else if (message35.isUnLoadCar()) {
            lift.clearMckeyAndReservMckey();
        }else if(message35.isMove()){
            lift.setDock(message35.Station);
        }
    }

    @Override
    public void converyor35Proc() throws Exception {
        Conveyor conveyor = (Conveyor) block;
        if (message35.isLoadCar()) {
            conveyor.generateMckey(aj.getMcKey());
        } else if (message35.isUnLoadCar()) {
            conveyor.clearMckeyAndReservMckey();
        }
    }

    @Override
    public void station35Proc() throws Exception {

    }
}
