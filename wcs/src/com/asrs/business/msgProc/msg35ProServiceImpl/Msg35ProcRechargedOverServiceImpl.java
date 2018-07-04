package com.asrs.business.msgProc.msg35ProServiceImpl;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.CreateAsrsJob;
import com.asrs.business.msgProc.msg35ProcService.Msg35ProcService;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.ScarChargeLocation;
import com.asrs.message.Message35;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 23:24 2018/6/21
 * @Description:
 * @Modified By:
 */
public class Msg35ProcRechargedOverServiceImpl implements Msg35ProcService {
    private Message35 message35;
    private AsrsJob aj;
    private Block block;

    public Msg35ProcRechargedOverServiceImpl(Message35 message35, AsrsJob aj, Block block) {
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
            sCar.setOnMCar(null);
            sCar.setBank(Integer.parseInt(message35.Bank));
            if(sCar.getGroupNo()!=Integer.parseInt(aj.getBarcode())){
                sCar.clearMckeyAndReservMckey();
            }

        } else if (message35.isOnCar()) {
            sCar.setOnMCar(message35.Station);
            sCar.setBank(0);
            if (message35.Station.equals(aj.getToStation())) {
                aj.setStatus(AsrsJobStatus.DONE);
                sCar.clearMckeyAndReservMckey();
            }
            ScarChargeLocation scarChargeLocation = ScarChargeLocation.getReservedChargeLocationBySCarBlockNo(sCar.getBlockNo());
            if(scarChargeLocation!=null) {
                scarChargeLocation.setReceved(false);
            }
            Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where position=:position and level=:level");
            query.setParameter("position", sCar.getPosition());
            query.setParameter("level",sCar.getLevel() );
            List<SCar> sCarList =query.list();
            if(sCarList.size()==1){
                //本层充电完成，不需要生成换层任务
            }else{
                //非本层充电完成，需要生成换层任务
                AsrsJob asrsJob = AsrsJob.getAsrsJobByTypeAndBarcode(AsrsJobType.CHANGELEVEL, aj.getBarcode());
                if(asrsJob!=null){
                    sCar.setMcKey(asrsJob.getMcKey());
                }
            }
        }else if(message35.isChargeFinish()){
            //充电完成，解除子车状态，后续按照正常空车上车处理
            sCar.setStatus(SCar.STATUS_RUN);
        }
    }

    @Override
    public void srm35Proc() throws Exception {
        Srm mCar = (Srm) block;
        if (message35.isMove()) {
            mCar.setCheckLocation(true);
            mCar.setBay(Integer.parseInt(message35.Bay));
            mCar.setLevel(Integer.parseInt(message35.Level));

            if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                SCar sCar = (SCar) SCar.getByBlockNo(mCar.getsCarBlockNo());
                sCar.setLevel(mCar.getLevel());
                sCar.setBay(mCar.getBay());
            }

        } else if (message35.isLoadCar()) {
            mCar.setsCarBlockNo(message35.Station);
            if (mCar.getBlockNo().equals(aj.getToStation())) {
                mCar.clearMckeyAndReservMckey();
                aj.setStatus(AsrsJobStatus.DONE);

                Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where wareHouse=:wh and status=:st ");
                query.setParameter("wh", mCar.getWareHouse());
                query.setParameter("st", SCar.STATUS_CHARGE);
                List<SCar> sCars = query.list();
                for (SCar sCar : sCars) {
                    sCar.setStatus(SCar.STATUS_RUN);
                }

            } else {
                mCar.generateMckey(message35.McKey);
            }
        } else if (message35.isUnLoadCar()) {
            mCar.setsCarBlockNo(null);
            mCar.clearMckeyAndReservMckey();
        }
    }

    @Override
    public void mCar35Proc() throws Exception {
        MCar mCar = (MCar) block;
        if (message35.isMove()) {
            mCar.setCheckLocation(true);
            mCar.setBay(Integer.parseInt(message35.Bay));
            //mCar.setLevel(Integer.parseInt(message35.Level));

            if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                SCar sCar = (SCar) SCar.getByBlockNo(mCar.getsCarBlockNo());
                sCar.setLevel(mCar.getLevel());
                sCar.setBay(mCar.getBay());
            }

        } else if (message35.isLoadCar()) {
            mCar.setsCarBlockNo(message35.Station);
            if (mCar.getBlockNo().equals(aj.getToStation())) {
                mCar.clearMckeyAndReservMckey();
                aj.setStatus(AsrsJobStatus.DONE);

                //sCar.setStatus(SCar.STATUS_RUN);
                /*Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where wareHouse=:wh and status=:st ");
                query.setParameter("wh", mCar.getWareHouse());
                query.setParameter("st", SCar.STATUS_CHARGE);
                List<SCar> sCars = query.list();
                for (SCar sCar : sCars) {
                    sCar.setStatus(SCar.STATUS_RUN);
                }*/
                if(mCar.getGroupNo()!=null){
                    AsrsJob asrsJob = AsrsJob.getAsrsJobByTypeAndBarcode(AsrsJobType.CHANGELEVEL, aj.getBarcode());
                    if(asrsJob!=null){
                        mCar.setMcKey(asrsJob.getMcKey());
                    }

                }else{
                    mCar.setGroupNo(Integer.parseInt(aj.getBarcode()));
                }

            } else {
                mCar.generateMckey(message35.McKey);
            }
        } else if (message35.isUnLoadCar()) {
            mCar.setsCarBlockNo(null);
            mCar.clearMckeyAndReservMckey();
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
