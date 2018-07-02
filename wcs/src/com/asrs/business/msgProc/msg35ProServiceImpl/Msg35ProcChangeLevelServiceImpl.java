package com.asrs.business.msgProc.msg35ProServiceImpl;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.msgProc.msg35ProcService.Msg35ProcService;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message35;
import com.thread.blocks.*;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;


/**
 *
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

                AsrsJob asrsJob = AsrsJob.getAsrsJobByTypeAndBarcode(AsrsJobType.RECHARGED, sCar.getBlockNo());
                if(asrsJob!=null){
                    sCar.setMcKey(asrsJob.getMcKey());
                }
            }
        }else if (message35.isOffCar()) {
            /*if (message35.Station.equals(aj.getToStation())) {
                MCar mCar = (MCar) MCar.getByBlockNo(aj.getToStation());
                sCar.setOnMCar(mCar.getBlockNo());
                mCar.setGroupNo(sCar.getGroupNo());
            } else {*/
                sCar.setOnMCar(null);
            /*}*/
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
            if (message35.Station.equals("0000")) {
                mCar.setDock(null);
                mCar.setBay(Integer.parseInt(message35.Bay));
                Location toLoc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), mCar.getBay(), mCar.getLevel(), mCar.getPosition());
                mCar.setActualArea(toLoc.getActualArea());
                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                    sCar.setBay(mCar.getBay());
                    sCar.setLevel(mCar.getLevel());
                    sCar.setActualArea(mCar.getActualArea());
                }
            } else {
                mCar.setBay(Integer.parseInt(message35.Bay));
                mCar.setDock(message35.Station);
                mCar.setCheckLocation(true);
                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                    sCar.setLevel(mCar.getLevel());
                    sCar.setBay(mCar.getBay());
                }
            }
        } else if (message35.isUnLoadCar()) {
            //换成卸子车，清除任务，子车,清除绑定子车
            mCar.clearMckeyAndReservMckey();
            mCar.setsCarBlockNo(null);
            if(aj.getToStation().equals(mCar.getBlockNo()) && mCar.getGroupNo()!=null){

            }else{
                mCar.setGroupNo(null);
            }

        } else if (message35.isLoadCar()) {
            mCar.setsCarBlockNo(message35.Station);
            mCar.clearMckeyAndReservMckey();
            if(aj.getToStation().equals(mCar.getBlockNo()) && mCar.getGroupNo()!=null) {
                AsrsJob asrsJob = AsrsJob.getAsrsJobByTypeAndFromStation(AsrsJobType.RECHARGED,mCar.getBlockNo());
                if(asrsJob!=null){
                    mCar.setMcKey(asrsJob.getMcKey());
                }
            }else{
                mCar.setGroupNo(Integer.valueOf(aj.getBarcode()));
            }
            aj.setStatus(AsrsJobStatus.DONE);

        }

    }

    @Override
    public void lift35Proc() throws Exception {
        Lift lift = (Lift) block;
        if (message35.isLoadCar()) {
            lift.generateMckey(aj.getMcKey());
            lift.setOnCar(message35.Station);
        } else if (message35.isUnLoadCar()) {
            lift.clearMckeyAndReservMckey();
            lift.setOnCar(null);
        }else if(message35.isMove()){
            lift.setDock(message35.Station);
            Dock dock =lift.getDockClass(message35.Station);
            lift.setLevel(dock.getLevel());
            if(org.apache.commons.lang3.StringUtils.isNotBlank(lift.getOnCar()) && dock!=null){
                SCar sCar =(SCar) SCar.getByBlockNo(lift.getOnCar());
                sCar.setLevel(dock.getLevel());
            }
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
