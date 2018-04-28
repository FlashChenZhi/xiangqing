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
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

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
        if(mCar.getGroupNo()!=null){
            //判断母车绑定有子车
            SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());
            if(sCar!=null){
                //检查子车上是否有任务,将其赋予母车的reservedMckey
                if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                    /**
                     *  子车有预约任务，母车预约任务（子车在35中有查询是否在此列继续出库的代码）
                     */
                    mCar.setReservedMcKey(sCar.getReservedMcKey());

                } else {
                    boolean hasJob =false;

                    if (!hasJob) {
                        //查找本层的入库任务
                        hasJob = findPutawayByLevelOfMcar(hasJob);
                    }
                    if (!hasJob) {
                        //查找本层的出库任务
                        hasJob = findStockRemovalByLevelOfMcar(hasJob);
                    }

                    if (hasJob) {
                        //若查到任务改变一下任务状态
                        AsrsJob asrsJob = null;
                        if (StringUtils.isNotBlank(mCar.getReservedMcKey()))
                            asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
                        else if (StringUtils.isNotBlank(mCar.getMcKey()))
                            asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
                        asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                    } else {
                        //若没查到任务，且母车绑定有子车，并且子车不在母车上，给母车发上车
                        if(sCar!=null && StringUtils.isBlank(mCar.getsCarBlockNo())){
                            if (StringUtils.isBlank(sCar.getMcKey()) && StringUtils.isBlank(sCar.getReservedMcKey())) {
                                MCarOperator srmOperator = new MCarOperator(mCar, "9999");
                                srmOperator.tryLoadCar();
                            }
                        }
                    }
                }
            }
        }else{
            //母车没有绑定小车
            //查找是否有充电、充电完成任务、换层任务
            Query query = HibernateUtil.getCurrentSession().createQuery(
                    "from AsrsJob a,MCar m where (a.type=:tp or a.type=:tp2 or a.type =:tp3) and " +
                            "a.toStation = m.blockNo and m.position=:position and m.blockNo=:blockNo ) ");
            query.setString("tp", AsrsJobType.RECHARGED);
            query.setString("tp2", AsrsJobType.RECHARGEDOVER);
            query.setString("tp2", AsrsJobType.CHANGELEVEL);
            query.setString("blockNo", mCar.getBlockNo());
            query.setString("position", mCar.getPosition());
            query.setMaxResults(1);
            AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
            if(asrsJob!=null){
                mCar.setReservedMcKey(asrsJob.getMcKey());
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
    public boolean findPutawayByLevelOfMcar(boolean hasJob){
        Block block = mCar.getPreBlockHasMckey(AsrsJobType.PUTAWAY);
        if (block != null) {
            //如果上一段block有mckey，
            if (block instanceof Conveyor) {
                Conveyor conveyor = (Conveyor) block;
                if (StringUtils.isNotBlank(conveyor.getMcKey())) {
                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                    //如果小车绑定母车的上一节是入库作业，设置小车的reservedmckey
                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                        mCar.setReservedMcKey(block.getMcKey());
                        hasJob=true;
                    }
                }
            } else if (block instanceof StationBlock) {
                if (StringUtils.isNotBlank(block.getMcKey())) {
                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                        mCar.setReservedMcKey(block.getMcKey());
                        hasJob=true;
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
    public boolean findStockRemovalByLevelOfMcar(boolean hasJob){
        //获取本层的出库任务
        Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:fStation order by id asc ").setMaxResults(1);
        query.setParameter("tp", AsrsJobType.RETRIEVAL);
        query.setParameter("fStation", mCar.getBlockNo());
        AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
        if (asrsJob != null) {
            mCar.setReservedMcKey(asrsJob.getMcKey());
            hasJob = true;
        }
        return hasJob;
    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {

    }
       /* Query charQuery = HibernateUtil.getCurrentSession().createQuery("from AsrsJob  where (type=:tp or type=:ttp) and (fromStation=:fs or toStation=:ts) and status!=:status");
        charQuery.setParameter("tp", AsrsJobType.RECHARGED);
        charQuery.setParameter("ttp", AsrsJobType.RECHARGEDOVER);
        charQuery.setParameter("status", AsrsJobStatus.DONE);
        charQuery.setParameter("fs", mCar.getBlockNo());
        charQuery.setParameter("ts", mCar.getBlockNo());

        charQuery.setMaxResults(1);
        AsrsJob chargedJob = (AsrsJob) charQuery.uniqueResult();

        if (chargedJob != null) {

            if (chargedJob.getType().equals(AsrsJobType.RECHARGED)) {
                //如果仓库里存在充电作业，不查找其他作业，
                MCar fromMCar = (MCar) MCar.getByBlockNo(chargedJob.getFromStation());
                Location location = Location.getByLocationNo(chargedJob.getToLocation());
                if (fromMCar.getBlockNo().equals(mCar.getBlockNo())) {
                    //如果充电子车绑定的堆垛机就是当前堆垛机
                    //不用处理，作业生成的时候自动绑定任务

                } else {
                    //如果充电子车绑定的堆垛机不是当前堆垛机
                    if (mCar.getPosition().equals(location.getPosition())) {
                        //如果当前堆垛机和充电货位的位置是一边
                        if (!chargedJob.getStatus().equals(AsrsJobStatus.DONE))
                            mCar.setReservedMcKey(chargedJob.getMcKey());
                    } else {
                        //如果当前堆垛机和冲淡位置不是一边
                        //不用理睬
                    }
                }
            } else if (chargedJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                //系统存在充电完成作业
                MCar toMCar = (MCar) MCar.getByBlockNo(chargedJob.getToStation());
                Location location = Location.getByLocationNo(chargedJob.getFromLocation());
                SCar sCar = SCar.getScarByGroup(toMCar.getGroupNo());
                if (toMCar.getBlockNo().equals(mCar.getBlockNo())) {
                    //目标堆垛机是当前堆垛机
                    if (mCar.getPosition().equals(location.getPosition())) {
                        //当前堆垛机位置和充电位置一直
                        //不用理睬
                    } else {
                        //当前堆垛机在充电位置的另一边
                        if (!chargedJob.getStatus().equals(AsrsJobStatus.DONE))
                            toMCar.setReservedMcKey(chargedJob.getMcKey());
                    }
                } else {
                    //目标堆垛机不是当前堆垛机
                    if (mCar.getPosition().equals(location.getPosition())) {
                        //当前堆垛机和充电位置堆垛机一直
                        if (sCar.getBank() == location.getBank()
                                && sCar.getBay() == location.getBay()
                                && sCar.getLevel() == location.getLevel()
                                && sCar.getPosition().equals(mCar.getPosition())) {
                            //如果子车和堆垛机在同一边，并且子车在充电位子上
                            mCar.setReservedMcKey(chargedJob.getMcKey());
                        }
                    }
                }
            }

        } else {
            //仓库里没有充电作业，执行正常操作
            //移动提升机上有子车
            if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                //获取子车
                SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());
                //检查子车上是否有任务
                if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                    //子车有出库预约任务，提升机预约任务
                    mCar.setReservedMcKey(sCar.getReservedMcKey());

                } else {
                    //子车在提升机上
                    //获取入库任务
                    boolean hasJob = false;

                    //堆垛机上的子车电量不足，生成充电任务
                    if (sCar.getPower() <= 40) {
                        AsrsJob asrsJob = new AsrsJob();
                        asrsJob.setMcKey(Mckey.getNext());
                        asrsJob.setToLocation(sCar.getChargeLocation());
                        asrsJob.setFromStation(mCar.getBlockNo());
                        Location location = Location.getByLocationNo(sCar.getChargeLocation());
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



                    if (!hasJob) {
                        Block block = mCar.getPreBlockHasMckey(AsrsJobType.PUTAWAY);
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
                                        }else if(block.getBlockNo().equals("0059")
                                                ||block.getBlockNo().equals("0065")){
                                            if(mCar.getBlockNo().equals("MC05")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }
                                        }else if(block.getBlockNo().equals("0066")
                                                ||block.getBlockNo().equals("0067")
                                                ||block.getBlockNo().equals("0068")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;

                                        } else if(block.getBlockNo().equals("0047")
                                                ||block.getBlockNo().equals("0048")
                                                ||block.getBlockNo().equals("0049")){
                                            if(mCar.getBlockNo().equals("MC06")||mCar.getBlockNo().equals("MC07")||mCar.getBlockNo().equals("MC08")){
                                                mCar.setReservedMcKey(block.getMcKey());
                                                hasJob = true;
                                            }
                                        } else if(asrsJob.getFromStation().equals("0037")){
                                            mCar.setReservedMcKey(block.getMcKey());
                                            hasJob = true;
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
                        Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:fStation order by id asc ").setMaxResults(1);
                        query.setParameter("tp", AsrsJobType.RETRIEVAL);
                        query.setParameter("fStation", mCar.getBlockNo());
                        AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                        if (asrsJob != null) {
                            mCar.setReservedMcKey(asrsJob.getMcKey());
                            hasJob = true;
                        }
                    }

                    //获取库内移动任务
                    if (!hasJob) {
                        Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:fStation order by id asc ").setMaxResults(1);
                        query.setParameter("tp", AsrsJobType.LOCATIONTOLOCATION);
                        query.setParameter("fStation", mCar.getBlockNo());
                        AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                        if (asrsJob != null) {
                            mCar.setReservedMcKey(asrsJob.getMcKey());
                            hasJob = true;
                        }
                    }
                    //获取直行任务
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
//                        if (!mCar.getCycle().equals(mCar.getDock())) {
//                            SrmOperator srmOperator = new SrmOperator(srm, "9999");
//                            srmOperator.cycle(srm);
//                        }
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
                                        mCar.setReservedMcKey(block.getMcKey());
                                        hasJob = true;
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
                    Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:st order by id asc ").setMaxResults(1);
                    query.setParameter("tp", AsrsJobType.RETRIEVAL);
                    query.setParameter("st", mCar.getBlockNo());
                    AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                    if (asrsJob != null) {
                        mCar.setReservedMcKey(asrsJob.getMcKey());
                        hasJob = true;
                    }
                }

                if (!hasJob) {
                    Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:st order by id asc ").setMaxResults(1);
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


                if (!hasJob) {
                    if(sCar!=null){
                        if (StringUtils.isBlank(sCar.getMcKey()) && StringUtils.isBlank(sCar.getReservedMcKey())) {
                            MCarOperator srmOperator = new MCarOperator(mCar, "9999");
                            srmOperator.tryLoadCar();
                        }
                    }
                }
            }
        }*/




}
