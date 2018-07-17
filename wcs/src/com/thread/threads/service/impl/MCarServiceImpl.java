package com.thread.threads.service.impl;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.*;
import com.thread.threads.operator.MCarOperator;
import com.thread.threads.service.MCarService;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by van on 2017/10/31.
 */
public class MCarServiceImpl implements MCarService {
    private MCar mCar;

    public MCarServiceImpl(MCar mCar) {
        this.mCar = mCar;
    }

    @Override
    public void withOutJob() throws Exception {

        /**
         *  注：小车所在的母车未必是小车绑定的母车，有可能是小车去一层母车上为了充电！！！（小车充电时应该没有绑定母车的）
         *      小车不在母车上：小车在此层货位里，小车在提升机上，小车在充电位
         *      母车绑定有子车，母车去接小车的任务,若没有接到再去查本层的入库和出库任务。
         *      母车没有绑定子车，判断是否有充电任务，此母车是不是同一position的一层母车，是否有换层任务（换层到此母车）
         */
        if (mCar.getGroupNo() != null) {
            //判断母车绑定有子车
            SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());
            if (sCar != null) {
                boolean hasJob = false;

                if (StringUtils.isNotBlank(sCar.getReservedMcKey()) || StringUtils.isNotBlank(sCar.getMcKey())) {
                    /**
                     *  子车有预约任务，母车预约任务（子车在35中有查询是否在此列继续出库的代码）
                     */
                    String mckey = StringUtils.isNotBlank(sCar.getReservedMcKey()) ? sCar.getReservedMcKey():sCar.getMcKey();
                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
                    if(asrsJob!=null && !asrsJob.getType().equals(AsrsJobType.PUTAWAY)  && !asrsJob.getType().equals(AsrsJobType.RECHARGED) &&!asrsJob.getStatus().equals(AsrsJobStatus.DONE)){
                        mCar.setReservedMcKey(mckey);
                        hasJob=true;
                    }

                }
                if (sCar.getPower() > Const.LOWER_POWER){
                    Query charQuery = HibernateUtil.getCurrentSession().createQuery("from AsrsJob a where " +
                            "a.type=:tttp and a.toStation=:toStation and a.status!=:status");
                    charQuery.setParameter("tttp", AsrsJobType.CHANGELEVEL);
                    charQuery.setParameter("status", AsrsJobStatus.DONE);
                    charQuery.setParameter("toStation", mCar.getBlockNo());
                    List<AsrsJob> charQuerys = charQuery.list();
                    if(charQuerys.size()==0){
                        //若无换到此层的
                        //检查子车上是否有任务,将其赋予母车的reservedMckey
                        if (!hasJob) {

                            hasJob = findPutawayByLevelOfMcar(hasJob);
                        }
                        if (!hasJob) {
                            //查找本层的出库任务
                            if (StringUtils.isNotBlank(sCar.getMcKey()) || StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                                String mckey = StringUtils.isNotBlank(sCar.getReservedMcKey()) ? sCar.getReservedMcKey() : sCar.getMcKey();
                                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
                                if(!asrsJob.getStatus().equals(AsrsJobStatus.DONE) && asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                                    mCar.setReservedMcKey(mckey);
                                }
                            } else {
                                hasJob = findStockRemovalByLevelOfMcar(hasJob);
                            }
                        }

                        if (!hasJob) {
                            //若没查到任务，且母车绑定有子车，并且子车不在母车上，给母车发上车
                            if (sCar != null && StringUtils.isBlank(mCar.getsCarBlockNo())) {
                                if (StringUtils.isBlank(sCar.getMcKey()) && StringUtils.isBlank(sCar.getReservedMcKey())) {
                                    MCarOperator srmOperator = new MCarOperator(mCar, "9999");
                                    srmOperator.tryLoadCar();
                                }
                            }
                        }
                    }
                }else{
                    if (!hasJob) {
                        //存在小车电量低，将入库任务做完再上车充电
                        hasJob = findPutawayByLevelOfMcar(hasJob);
                    }
                    if (!hasJob && sCar != null && StringUtils.isBlank(mCar.getsCarBlockNo())) {
                        if (StringUtils.isBlank(sCar.getMcKey()) && StringUtils.isBlank(sCar.getReservedMcKey())) {
                            MCarOperator srmOperator = new MCarOperator(mCar, "9999");
                            srmOperator.tryLoadCar();
                        }
                    }
                }

            }
        } else {
            //查找是否有充电或者充电完成任务
            Query charQuery = HibernateUtil.getCurrentSession().createQuery("from AsrsJob  where (type=:tp or type=:ttp) and (fromStation=:fs or toStation=:ts) and status!=:status");
            charQuery.setParameter("tp", AsrsJobType.RECHARGED);
            charQuery.setParameter("ttp", AsrsJobType.RECHARGEDOVER);
            charQuery.setParameter("status", AsrsJobStatus.DONE);
            charQuery.setParameter("fs", mCar.getBlockNo());
            charQuery.setParameter("ts",  mCar.getBlockNo());
            charQuery.setMaxResults(1);
            AsrsJob chargedJob = (AsrsJob) charQuery.uniqueResult();

            if (chargedJob == null) {
                //母车没有绑定小车
                //查找是否有换层任务(充电任务直接赋值的)
                Query query = HibernateUtil.getCurrentSession().createQuery(
                        "from AsrsJob a,MCar m where  a.type =:tp3 and " +
                                "a.toStation = m.blockNo and m.position=:position and m.blockNo=:blockNo ) ");
                query.setString("tp3", AsrsJobType.CHANGELEVEL);
                query.setString("blockNo", mCar.getBlockNo());
                query.setString("position", mCar.getPosition());
                query.setMaxResults(1);
                AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                if (asrsJob != null && !asrsJob.getStatus().equals(AsrsJobStatus.DONE)) {
                    mCar.setReservedMcKey(asrsJob.getMcKey());
                }
            }
        }
    }

    /*
     * @author：ed_chen
     * @date：2018/4/17 15:57
     * @description：查找母车所在层的 入库任务
     * @param hasJob
     * @param mCar
     * @return：boolean
     */
    public boolean findPutawayByLevelOfMcar(boolean hasJob) {
        Block block = mCar.getPreBlockHasMckey(AsrsJobType.PUTAWAY);
        if (block != null) {
            //如果上一段block有mckey，
            if (block instanceof Conveyor) {
                Conveyor conveyor = (Conveyor) block;
                if (StringUtils.isNotBlank(conveyor.getMcKey())) {
                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                    //如果小车绑定母车的上一节是入库作业，设置小车的reservedmckey
                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY) && !asrsJob.getStatus().equals(AsrsJobStatus.DONE)) {
                        mCar.setReservedMcKey(block.getMcKey());
                        hasJob = true;
                    }
                }
            } else if (block instanceof StationBlock) {
                if (StringUtils.isNotBlank(block.getMcKey())) {
                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY) && !asrsJob.getStatus().equals(AsrsJobStatus.DONE) ) {
                        mCar.setReservedMcKey(block.getMcKey());
                        hasJob = true;
                    }
                }
            }else if (block instanceof Lift) {
                if (StringUtils.isNotBlank(block.getMcKey())) {
                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY) && !asrsJob.getStatus().equals(AsrsJobStatus.DONE) ) {
                        mCar.setReservedMcKey(block.getMcKey());
                        hasJob = true;
                    }
                }
            }
        }
        return hasJob;
    }

    /*
     * @author：ed_chen
     * @date：2018/4/17 16:07
     * @description：查找母车所在层的 出库任务
     * @param hasJob
     * @param mCar
     * @return：boolean
     */
    public boolean findStockRemovalByLevelOfMcar(boolean hasJob) {
        //获取本层的出库任务
        Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and status=:status and fromStation=:fStation order by id asc ").setMaxResults(1);
        query.setParameter("tp", AsrsJobType.RETRIEVAL);
        query.setParameter("fStation", mCar.getBlockNo());
        query.setParameter("status", AsrsJobStatus.RUNNING);
        AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
        if (asrsJob != null) {
            mCar.setReservedMcKey(asrsJob.getMcKey());
            hasJob = true;
        }
        return hasJob;
    }

    //仓库里没有充电作业，执行正常操作
    //移动提升机上没有子车
            /*if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                //获取子车
                SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());
                //检查子车上是否有任务
                if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                    //子车有出库预约任务，提升机预约任务
                    mCar.setReservedMcKey(sCar.getReservedMcKey());

                } else {
                    //子车不在提升机上
                    //获取入库任务
                    boolean hasJob = false;

                    //堆垛机上的子车电量不足，生成充电任务
                    if (sCar.getPower() <= 30) {
                        if(StringUtils.isBlank(sCar.getOnMCar())){
                            return;
                        }
                        Location location = Location.getByLocationNo(sCar.getChargeLocation());
                        if(location!=null){
                            AsrsJob asrsJob = new AsrsJob();
                            asrsJob.setMcKey(Mckey.getNext());
                            asrsJob.setToLocation(sCar.getChargeLocation());
                            asrsJob.setFromStation(mCar.getBlockNo());
                            MCar chargeSrm = mCar.getMCarByPosition(location.getPosition(),location.getLevel());
                            asrsJob.setToStation(chargeSrm.getBlockNo());
                            asrsJob.setStatus(AsrsJobStatus.RUNNING);
                            asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                            asrsJob.setType(AsrsJobType.RECHARGED);
                            asrsJob.setWareHouse(mCar.getWareHouse());
//                            AsrsJob asrsJob3=(AsrsJob) HibernateUtil.getCurrentSession().createQuery("from AsrsJob order by generateTime").setMaxResults(1).uniqueResult();
//                            if(asrsJob3!=null){
//                                Calendar calendar=Calendar.getInstance();
//                                calendar.setTime(asrsJob3.getGenerateTime());
//                                calendar.add(Calendar.MINUTE,-1);
//                                asrsJob.setGenerateTime(asrsJob3.getGenerateTime());
//                            }else {
//                                asrsJob.setGenerateTime(new Date());
//                            }
                            HibernateUtil.getCurrentSession().save(asrsJob);
                            mCar.setMcKey(asrsJob.getMcKey());
                            sCar.setMcKey(asrsJob.getMcKey());
                            hasJob = true;
                        }
                    }


                    Block block = mCar.getPreBlockHasMckey(AsrsJobType.PUTAWAY);
                    if (!hasJob) {
                        if (block != null) {
                            //如果上一段block有mckey，
                            if (block instanceof Conveyor) {
                                Conveyor conveyor = (Conveyor) block;
                                if (StringUtils.isNotBlank(conveyor.getMcKey())) {
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                                        if(block.getBlockNo().equals("0046")){
                                            if(asrsJob.getFromStation().equals("0040")
                                                    || asrsJob.getFromStation().equals("0039")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }
                                        }else if(block.getBlockNo().equals("0059")||block.getBlockNo().equals("0065")){
                                            Block mc05 = block.getNextBlock(asrsJob.getType(), mCar.getBlockNo());
                                            if(mc05.getBlockNo().equals("MC05")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }
                                        }else if(block.getBlockNo().equals("0066")
                                                ||block.getBlockNo().equals("0067")
                                                ||block.getBlockNo().equals("0068")){
                                            if(mCar.getBlockNo().equals("MC06")||mCar.getBlockNo().equals("MC07")||mCar.getBlockNo().equals("MC08")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }

                                        } else if(block.getBlockNo().equals("0047")
                                                ||block.getBlockNo().equals("0048")
                                                ||block.getBlockNo().equals("0049")){
                                            if(mCar.getBlockNo().equals("MC06")||mCar.getBlockNo().equals("MC07")||mCar.getBlockNo().equals("MC08")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }
                                        } else if(asrsJob.getFromStation().equals("0037")||asrsJob.getFromStation().equals("0040")||asrsJob.getFromStation().equals("0039")){
                                            if(mCar.getBlockNo().equals("MC01")||mCar.getBlockNo().equals("MC02")||mCar.getBlockNo().equals("MC03")||mCar.getBlockNo().equals("MC04")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }
                                        }
                                    }
                                }
                            } else if (block instanceof StationBlock) {
                                if (StringUtils.isNotBlank(block.getMcKey())) {
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                                        mCar.setReservedMcKey(block.getMcKey());
                                        hasJob = true;
                                    }
                                }
                            }
                        }
                    }

                    //获取出库任务
                    if (!hasJob) {
                        Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:fStation order by generateTime asc ").setMaxResults(1);
                        query.setParameter("tp", AsrsJobType.RETRIEVAL);
                        query.setParameter("fStation", mCar.getBlockNo());
                        AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                        if (asrsJob != null) {
                            mCar.setReservedMcKey(asrsJob.getMcKey());
                            hasJob = true;
                        }
                    }

                    //获取出库任务
                    if (!hasJob) {
                        Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:fStation order by generateTime asc ").setMaxResults(1);
                        query.setParameter("tp", AsrsJobType.LOCATIONTOLOCATION);
                        query.setParameter("fStation", mCar.getBlockNo());
                        AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                        if (asrsJob != null) {
                            mCar.setReservedMcKey(asrsJob.getMcKey());
                            hasJob = true;
                        }
                    }
                    if (!hasJob) {
                        Block bb = mCar.getPreBlockHasMckey(AsrsJobType.ST2ST);
                        if (bb != null) {
                            //如果上一段block有mckey，
                            if (bb instanceof Conveyor) {
                                Conveyor conveyor = (Conveyor) bb;
                                if (StringUtils.isNotBlank(conveyor.getMcKey()) && (!conveyor.isWaitingResponse() || (!conveyor.isMantWaiting() && conveyor.isManty()))) {
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(bb.getMcKey());
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    if (asrsJob.getType().equals(AsrsJobType.ST2ST)) {
                                        mCar.setReservedMcKey(bb.getMcKey());
                                        hasJob = true;
                                    }
                                }
                            } else if (bb instanceof StationBlock) {
                                if (StringUtils.isNotBlank(bb.getMcKey()) && !bb.isWaitingResponse()) {
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(bb.getMcKey());
                                    if (asrsJob.getType().equals(AsrsJobType.ST2ST)) {
                                        mCar.setReservedMcKey(bb.getMcKey());
                                        hasJob = true;
                                    }
                                }
                            }
                        }
                    }


                    if (hasJob) {
                        AsrsJob asrsJob = null;
                        if (StringUtils.isNotBlank(mCar.getReservedMcKey()))
                            asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
                        else if (StringUtils.isNotBlank(mCar.getMcKey()))
                            asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
                        asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                    } else {
                        Query qqq = HibernateUtil.getCurrentSession().createQuery("from AsrsJob  where  (fromStation=:fs or toStation=:ts) and (( type='03' and status<>'3') or type <> '03')");
                        qqq.setParameter("fs", mCar.getBlockNo());
                        qqq.setParameter("ts", mCar.getBlockNo());
                        qqq.setMaxResults(1);
                        AsrsJob ajb = (AsrsJob) qqq.uniqueResult();
                        if (sCar.getPower() <= 90&& ajb==null) {
                            if(StringUtils.isBlank(sCar.getOnMCar())){
                                return;
                            }
                            Location location = Location.getByLocationNo(sCar.getChargeLocation());
                            if(location!=null){
                                AsrsJob asrsJob = new AsrsJob();
                                asrsJob.setMcKey(Mckey.getNext());
                                asrsJob.setToLocation(sCar.getChargeLocation());
                                asrsJob.setFromStation(mCar.getBlockNo());
                                MCar chargeSrm = mCar.getMCarByPosition(location.getPosition(),location.getLevel());
                                asrsJob.setToStation(chargeSrm.getBlockNo());
                                asrsJob.setStatus(AsrsJobStatus.RUNNING);
                                asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                                asrsJob.setType(AsrsJobType.RECHARGED);
                                asrsJob.setWareHouse(mCar.getWareHouse());
                                HibernateUtil.getCurrentSession().save(asrsJob);
                                mCar.setMcKey(asrsJob.getMcKey());
                                sCar.setMcKey(asrsJob.getMcKey());
                                hasJob = true;
                            }
                        }

                    }
                }

            } else {
                SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());
                boolean hasJob = false;

                if (sCar != null) {

                    if (StringUtils.isNotBlank(sCar.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                        if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            mCar.setReservedMcKey(sCar.getMcKey());
                            hasJob = true;
                        }
                    }

                    if (!hasJob) {
                        if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                            if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                                mCar.setReservedMcKey(sCar.getMcKey());
                                hasJob = true;
                            }
                        }
                    }

                    if (!hasJob) {
                        //查找入库任务
                        Block block = mCar.getPreBlockHasMckey(AsrsJobType.PUTAWAY);
                        if (block != null) {
                            //如果上一段block有mckey，设置提升机reservedmckey
                            if (block instanceof Conveyor) {
                                Conveyor conveyor = (Conveyor) block;
                                if (StringUtils.isNotBlank(conveyor.getMcKey()) && (!conveyor.isWaitingResponse() || (!conveyor.isMantWaiting() && conveyor.isManty()))) {
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                                        if(block.getBlockNo().equals("0046")){
                                            if(asrsJob.getFromStation().equals("0040")
                                                    || asrsJob.getFromStation().equals("0039")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }
                                        }else if(block.getBlockNo().equals("0059")||block.getBlockNo().equals("0065")){
                                            Block mc05 = block.getNextBlock(asrsJob.getType(), mCar.getBlockNo());
                                            if(mc05.getBlockNo().equals("MC05")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }
                                        }else if(block.getBlockNo().equals("0066")
                                                ||block.getBlockNo().equals("0067")
                                                ||block.getBlockNo().equals("0068")){
                                            if(mCar.getBlockNo().equals("MC06")||mCar.getBlockNo().equals("MC07")||mCar.getBlockNo().equals("MC08")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }

                                        } else if(block.getBlockNo().equals("0047")
                                                ||block.getBlockNo().equals("0048")
                                                ||block.getBlockNo().equals("0049")){
                                            if(mCar.getBlockNo().equals("MC06")||mCar.getBlockNo().equals("MC07")||mCar.getBlockNo().equals("MC08")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }
                                        } else if(asrsJob.getFromStation().equals("0037")||asrsJob.getFromStation().equals("0040")||asrsJob.getFromStation().equals("0039")){
                                            if(mCar.getBlockNo().equals("MC01")||mCar.getBlockNo().equals("MC02")||mCar.getBlockNo().equals("MC03")||mCar.getBlockNo().equals("MC04")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }
                                        }
                                    }
                                }
                            } else if (block instanceof StationBlock) {
                                if (StringUtils.isNotBlank(block.getMcKey()) && !block.isWaitingResponse()) {
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                                        mCar.setReservedMcKey(block.getMcKey());
                                        hasJob = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (!hasJob) {
                    Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:st order by generateTime asc ").setMaxResults(1);
                    query.setParameter("tp", AsrsJobType.RETRIEVAL);
                    query.setParameter("st", mCar.getBlockNo());
                    AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                    if (asrsJob != null) {
                        mCar.setReservedMcKey(asrsJob.getMcKey());
                        hasJob = true;
                    }
                }

                if (!hasJob) {
                    Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:st order by generateTime asc ").setMaxResults(1);
                    query.setParameter("tp", AsrsJobType.LOCATIONTOLOCATION);
                    query.setParameter("st", mCar.getBlockNo());
                    AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                    if (asrsJob != null) {
                        mCar.setReservedMcKey(asrsJob.getMcKey());
                        hasJob = true;
                    }
                }

                if (!hasJob) {
                    //查找入库任务
                    Block block = mCar.getPreBlockHasMckey(AsrsJobType.CHANGELEVEL);
                    if (block != null) {
                        //如果上一段block有mckey，设置提升机reservedmckey
                        if (block instanceof Conveyor) {
                            Conveyor conveyor = (Conveyor) block;
                            if (StringUtils.isNotBlank(conveyor.getMcKey()) && (!conveyor.isWaitingResponse() || (!conveyor.isMantWaiting() && conveyor.isManty()))) {
                                //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                                if (asrsJob.getType().equals(AsrsJobType.CHANGELEVEL) && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING) || asrsJob.getStatus().equals(AsrsJobStatus.ACCEPT)) {
                                    mCar.setReservedMcKey(block.getMcKey());
                                    asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                                    hasJob = true;
                                }
                            }
                        }
                    }
                }

                if (hasJob) {
                    AsrsJob asrsJob = null;
                    if (StringUtils.isNotBlank(mCar.getReservedMcKey()))
                        asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
                    asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                }

                if (!hasJob) {
                    if(sCar!=null){
                        if (StringUtils.isBlank(sCar.getMcKey()) && StringUtils.isBlank(sCar.getReservedMcKey())) {
                            MCarOperator srmOperator = new MCarOperator(mCar, "9999");
                            srmOperator.tryLoadCar();

                        }

                    }
                }
            }*/


    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {

    }
}
