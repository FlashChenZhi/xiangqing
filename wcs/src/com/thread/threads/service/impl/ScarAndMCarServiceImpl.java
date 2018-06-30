package com.thread.threads.service.impl;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.*;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.ScarService;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import onbon.bx05.area.unit.StringBxUnit;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

/**
 * Created by van on 2017/10/30.
 */
public class ScarAndMCarServiceImpl implements ScarService {

    private SCar sCar;

    public ScarAndMCarServiceImpl(SCar sCar) {
        this.sCar = sCar;
    }

    @Override
    public void withOutJob() throws Exception {


        //若小车找任务
        Session session = HibernateUtil.getCurrentSession();
        //获取小车绑定的母车
        /**
         *  注：
         *      子车绑定有母车，子车去找任务
         *      子车没有绑定母车：子车在提升机上有换层或充电任务，子车在充电status!=1
         */
        MCar mCar = MCar.getMCarByGroupNo(sCar.getGroupNo());
        if (mCar != null) {
            //查找是否有充电或者充电完成任务
            Query charQuery = session.createQuery("from AsrsJob  where (type=:tp or type=:ttp) and (fromStation=:fs or toStation=:ts) and status!=:status");
            charQuery.setParameter("tp", AsrsJobType.RECHARGED);
            charQuery.setParameter("ttp", AsrsJobType.RECHARGEDOVER);
            charQuery.setParameter("status", AsrsJobStatus.DONE);
            charQuery.setParameter("fs", mCar.getBlockNo());
            charQuery.setParameter("ts",  mCar.getBlockNo());
            charQuery.setMaxResults(1);
            AsrsJob chargedJob = (AsrsJob) charQuery.uniqueResult();

            if (chargedJob != null) {
                //若存在充电任务或充电完成任务
                if (chargedJob.getType().equals(AsrsJobType.RECHARGED)) {
                    //如果仓库里存在充电作业，不查找其他作业，
                    MCar fromMCar = (MCar) MCar.getByBlockNo(chargedJob.getFromStation());
                    Location location = Location.getByLocationNo(chargedJob.getToLocation());
                    if (fromMCar.getBlockNo().equals(mCar.getBlockNo())) {
                        //如果充电子车绑定的堆垛机就是当前堆垛机
                        //不用处理，作业生成的时候自动绑定任务

                    } else {
                        //如果充电子车绑定的堆垛机不是当前堆垛机
                        if (mCar.getPosition().equals(location.getPosition()) && mCar.getLevel() == 1) {
                            //如果当前堆垛机和充电货位的位置是一边并且是在第一层
                            if (!chargedJob.getStatus().equals(AsrsJobStatus.DONE))
                                sCar.setReservedMcKey(chargedJob.getMcKey());
                        } else {
                            //如果当前堆垛机和冲淡位置不是一边
                            //不用理睬
                        }
                    }
                } else if (chargedJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                    //系统存在充电完成作业
                    MCar toMCar = (MCar) MCar.getByBlockNo(chargedJob.getToStation());
                    Location location = Location.getByLocationNo(chargedJob.getFromLocation());
                    //SCar sCar = SCar.getScarByGroup(toMCar.getGroupNo());
                    if (toMCar.getBlockNo().equals(mCar.getBlockNo())) {
                        //目标堆垛机是当前堆垛机
                        if (mCar.getPosition().equals(location.getPosition())) {
                            //当前堆垛机位置和充电位置一直
                            //不用理睬，对乡情来说充电位置和母车位置的position一直相同！！！
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
                //子车是否在母车上
                if (StringUtils.isNotBlank(sCar.getOnMCar())) {

                    //子车在母车上，检查母车上是否有任务
                    if (StringUtils.isNotBlank(mCar.getReservedMcKey()) || StringUtils.isNotBlank(mCar.getMcKey())) {
                        //母车有预约任务，子车预约任务(本层入库任务是母车找的，出库其他列是母车找的，本列在35中已经找了)
                        String mckey = StringUtils.isNotBlank(mCar.getReservedMcKey()) ? mCar.getReservedMcKey() : mCar.getMcKey();
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
                        if(!asrsJob.getStatus().equals(AsrsJobStatus.DONE)  ){
                            if(AsrsJobType.RETRIEVAL.equals(asrsJob.getType())){
                                if(AsrsJobStatus.RUNNING.equals(asrsJob.getStatus())){
                                    sCar.setReservedMcKey(mCar.getReservedMcKey());
                                    asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                                    asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                                }
                            }else{
                                sCar.setReservedMcKey(mCar.getReservedMcKey());
                                asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                                asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                            }
                        }
                    } else {
                        //母车上无任务
                        boolean hasJob = false;
                        //堆垛机上的子车电量不足，生成充电任务
                        if (sCar.getPower() <= Const.LOWER_POWER) {
                            //生成充电任务
                            hasJob = createCharge(hasJob, mCar);
                        }
                        if (!hasJob) {
                            //本层的入库任务,从母车那边取，本层的任务由母车寻找
                            if (StringUtils.isNotBlank(mCar.getReservedMcKey()) || StringUtils.isNotBlank(mCar.getMcKey())) {
                                String mckey = StringUtils.isNotBlank(mCar.getReservedMcKey()) ? mCar.getReservedMcKey() : mCar.getMcKey();
                                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
                                if(!asrsJob.getStatus().equals(AsrsJobStatus.DONE) && asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                                    sCar.setReservedMcKey(mCar.getMcKey());
                                    hasJob=true;
                                }
                            } else {
                                hasJob = findPutawayByLevelOfMcar(hasJob, mCar);
                            }
                        }
                        if (!hasJob) {
                            //查找小车本层的出库任务,从母车那边取，本层的任务由母车寻找
                            if (StringUtils.isNotBlank(mCar.getReservedMcKey()) || StringUtils.isNotBlank(mCar.getMcKey())) {
                                String mckey = StringUtils.isNotBlank(mCar.getReservedMcKey()) ? mCar.getReservedMcKey() : mCar.getMcKey();
                                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
                                if(!asrsJob.getStatus().equals(AsrsJobStatus.DONE) && asrsJob.getType().equals(AsrsJobType.RETRIEVAL) && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING)) {
                                    sCar.setReservedMcKey(mCar.getMcKey());
                                    hasJob=true;
                                }
                            } else {
                                hasJob = findStockRemovalByLevelOfMcar(hasJob, mCar);
                            }
                        }
                        if (!hasJob) {
                            //查找没有小车并且没有小车正在赶往此母车并且此母车有入库任务 的母车
                            Query query = session.createQuery("from MCar m where m.sCarBlockNo is null and " +
                                    "m.position=:position and " +
                                    "not exists (select 1 from AsrsJob a where a.type=:tp and a.toStation = m.blockNo ) " +
                                    "and " +
                                    "exists (select d from RouteDetail d,Block b,AsrsJob a where a.mcKey=b.mcKey and d.currentBlockNo = b.blockNo and " +
                                    "d.nextBlockNo =m.blockNo and b.mcKey is not null and a.type=:tp2 and a.toStation=m.blockNo and d.route.type=:tp2 and d.route.status='1' )");
                            query.setString("tp", AsrsJobType.CHANGELEVEL);
                            query.setString("position", sCar.getPosition());
                            query.setString("tp2", AsrsJobType.PUTAWAY);
                            query.setMaxResults(1);
                            MCar toMCar = (MCar) query.uniqueResult();
                            if (toMCar != null) {
                                //存在有入库任务的母车，小车换层
                                hasJob = changeLevel(toMCar.getLevel(), hasJob);
                            }
                        }
                        if (!hasJob) {
                            //查找没有小车并且没有小车正在赶往此母车并且此母车有出库任务 的母车
                            Query query = session.createQuery("from MCar m where m.sCarBlockNo is null and " +
                                    "m.position=:position and " +
                                    "not exists (select 1 from AsrsJob a where a.type=:tp and a.toStation = m.blockNo ) " +
                                    "and " +
                                    "exists (select 1 from AsrsJob a where a.type =:tp1 and statusDetail = '0' and fromStation=m.blockNo )");
                            query.setString("tp", AsrsJobType.CHANGELEVEL);
                            query.setString("position", sCar.getPosition());
                            query.setString("tp1", AsrsJobType.RETRIEVAL);
                            query.setMaxResults(1);
                            MCar toMCar = (MCar) query.uniqueResult();
                            if (toMCar != null) {
                                //存在有出库任务的母车，小车换层
                                hasJob = changeLevel(toMCar.getLevel(), hasJob);
                            }
                        }
                        /*//获取库内移动任务
+                        if (!hasJob) {
+                            Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:fStation order by id asc ").setMaxResults(1);
+                            query.setParameter("tp", AsrsJobType.LOCATIONTOLOCATION);
+                            query.setParameter("fStation", mCar.getBlockNo());
+                            AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
+                            if (asrsJob != null) {
+                                mCar.setReservedMcKey(asrsJob.getMcKey());
+                                hasJob = true;
+                            }
+                        }*/
                        //获取直行任务
                         /*if (!hasJob) {
+                            Block bb = mCar.getPreBlockHasMckey(AsrsJobType.ST2ST);
+                            if (bb != null) {
+                                //如果上一段block有mckey，
+                                if (bb instanceof Conveyor) {
+                                    Conveyor conveyor = (Conveyor) bb;
+                                    if (StringUtils.isNotBlank(conveyor.getMcKey()) && (!conveyor.isWaitingResponse() || (!conveyor.isMantWaiting() && conveyor.isManty()))) {
+                                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(bb.getMcKey());
+                                        //如果提升机的上一节是入库作业，设置提升机reservedmckey
+                                        if (asrsJob.getType().equals(AsrsJobType.ST2ST)) {
+                                            mCar.setReservedMcKey(bb.getMcKey());
+                                            hasJob = true;
+                                        }
+                                    }
+                                } else if (bb instanceof StationBlock) {
+                                    if (StringUtils.isNotBlank(bb.getMcKey()) && !bb.isWaitingResponse()) {
+                                        //如果提升机的上一节是入库作业，设置提升机reservedmckey
+                                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(bb.getMcKey());
+                                        if (asrsJob.getType().equals(AsrsJobType.ST2ST)) {
+                                            mCar.setReservedMcKey(bb.getMcKey());
+                                            hasJob = true;
+                                        }
+                                    }
+                                }
+                            }
+                        }*/

                        if (hasJob) {
                            AsrsJob asrsJob = null;
                            if (StringUtils.isNotBlank(sCar.getReservedMcKey()))
                                asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                            else if (StringUtils.isNotBlank(sCar.getMcKey()))
                                asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                            asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                            asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                        } else {
/*                        if (!mCar.getCycle().equals(mCar.getDock())) {
+                            SrmOperator srmOperator = new SrmOperator(srm, "9999");
+                            srmOperator.cycle(srm);
+                        }*/
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(mCar.getReservedMcKey())) {
                        //母车有出库预约任务，子车预约任务
                        String mckey = mCar.getReservedMcKey();
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
                        if(!asrsJob.getStatus().equals(AsrsJobStatus.DONE)){
                            if(AsrsJobType.RETRIEVAL.equals(asrsJob.getType())){
                                if(AsrsJobStatus.RUNNING.equals(asrsJob.getStatus())){
                                    sCar.setReservedMcKey(mCar.getReservedMcKey());
                                }
                            }else{
                                sCar.setReservedMcKey(mCar.getReservedMcKey());
                            }
                        }

                    } else {
                        boolean hasJob = false;

                        //堆垛机上的子车电量不足，生成充电任务
                        if (sCar.getPower() <= Const.LOWER_POWER) {
                            //生成充电任务
                            hasJob = createCharge(hasJob, mCar);
                        }
                     /*if (StringUtils.isNotBlank(mCar.getMcKey())) {
+                            //若母车有出库任务mckey，则给子车
+                            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
+                            if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
+                                sCar.setReservedMcKey(mCar.getMcKey());
+                                hasJob = true;
+                            }
+                        }
+
+                        if (!hasJob) {
+                            //若母车有出库任务reservedMckey，则给子车
+                            if (StringUtils.isNotBlank(mCar.getReservedMcKey())) {
+                                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
+                                if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
+                                    sCar.setReservedMcKey(mCar.getMcKey());
+                                    hasJob = true;
+                                }
+                            }
+                        }*/
                        if (!hasJob) {
                            //本层的入库任务,从母车那边取，本层的任务由母车寻找
                            if (StringUtils.isNotBlank(mCar.getReservedMcKey()) || StringUtils.isNotBlank(mCar.getMcKey())) {
                                String mckey = StringUtils.isNotBlank(mCar.getReservedMcKey()) ? mCar.getReservedMcKey() : mCar.getMcKey();
                                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
                                if(!asrsJob.getStatus().equals(AsrsJobStatus.DONE) && asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                                    sCar.setReservedMcKey(mCar.getMcKey());
                                    hasJob=true;
                                }
                            } else {
                                hasJob = findPutawayByLevelOfMcar(hasJob, mCar);
                            }
                        }
                        if (!hasJob) {
                            //查找小车本层的出库任务,从母车那边取，本层的任务由母车寻找
                            if (StringUtils.isNotBlank(mCar.getReservedMcKey()) || StringUtils.isNotBlank(mCar.getMcKey())) {
                                String mckey = StringUtils.isNotBlank(mCar.getReservedMcKey()) ? mCar.getReservedMcKey() : mCar.getMcKey();
                                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
                                if(!asrsJob.getStatus().equals(AsrsJobStatus.DONE) && asrsJob.getType().equals(AsrsJobType.RETRIEVAL) && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING)) {
                                    sCar.setReservedMcKey(mCar.getMcKey());
                                    hasJob=true;
                                }
                            } else {
                                hasJob = findStockRemovalByLevelOfMcar(hasJob, mCar);
                            }
                        }

                        //不查找其他层的任务，因为小车不在母车上生成不了换层任务

                        /*if (!hasJob) {
+
+                            Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:st order by id asc ").setMaxResults(1);
+                            query.setParameter("tp", AsrsJobType.LOCATIONTOLOCATION);
+                            query.setParameter("st", mCar.getBlockNo());
+                            AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
+                            if (asrsJob != null) {
+                                mCar.setReservedMcKey(asrsJob.getMcKey());
+                                hasJob = true;
+                            }
+                        }*/

                     /*if (!hasJob) {
+                            //查找换层任务
+                            //应该在Mcar线程中保留，
+                            Block block = mCar.getPreBlockHasMckey(AsrsJobType.CHANGELEVEL);
+                            if (block != null) {
+                                //如果上一段block有mckey，设置提升机reservedmckey
+                                if (block instanceof Conveyor) {
+                                    Conveyor conveyor = (Conveyor) block;
+                                    if (StringUtils.isNotBlank(conveyor.getMcKey()) && (!conveyor.isWaitingResponse() || (!conveyor.isMantWaiting() && conveyor.isManty()))) {
+                                        //如果提升机的上一节是入库作业，设置提升机reservedmckey
+                                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
+                                        if (asrsJob.getType().equals(AsrsJobType.CHANGELEVEL) && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING) || asrsJob.getStatus().equals(AsrsJobStatus.ACCEPT)) {
+                                            mCar.setReservedMcKey(block.getMcKey());
+                                            asrsJob.setStatus(AsrsJobStatus.ACCEPT);
+                                            hasJob = true;
+                                        }
+                                    }
+                                }
+                            }
+                        }*/

                        if (!hasJob && mCar != null) {
                            //上车
                            if (mCar != null) {
                                if (StringUtils.isBlank(mCar.getMcKey()) && StringUtils.isBlank(mCar.getReservedMcKey())) {
                                    ScarOperator operator = new ScarOperator(sCar, "9999");
                                    operator.tryOnMCar(mCar);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /*
     * @author：ed_chen
     * @date：2018/4/17 15:57
     * @description：生成充电任务
     * @param
     * @return：boolean
     */
    public boolean createCharge(boolean hasJob, MCar mCar) {
        Session session = HibernateUtil.getCurrentSession();
        Query charQuery = session.createQuery("from AsrsJob a where (a.type=:tp or a.type=:ttp or " +
                "a.type=:tttp) and exists(select 1 from MCar m where m.blockNo=a.fromStation and " +
                "m.position = :position )");
        charQuery.setParameter("tp", AsrsJobType.RECHARGED);
        charQuery.setParameter("ttp", AsrsJobType.RECHARGEDOVER);
        charQuery.setParameter("ttp", AsrsJobType.CHANGELEVEL);
        /*charQuery.setParameter("status", AsrsJobStatus.DONE);*/
        charQuery.setParameter("position", sCar.getPosition());
        List<AsrsJob> charQuerys = charQuery.list();
        if (!charQuerys.isEmpty()) {
            //已有同一个position充电或者充电完成或者换层作业
            hasJob = true;
            return hasJob;
        }
        Query query = session.createQuery("select count(*) from SCar s where s.status =:status and s.position=:position ");
        query.setString("status", SCar.STATUS_CHARGE);
        query.setString("position", sCar.getPosition());
        long count = (long) query.uniqueResult();
        if(count!=0){
            //已有同一个position的小车在充电
            hasJob = true;
            return hasJob;
        }

        AsrsJob asrsJob = new AsrsJob();
        asrsJob.setMcKey(Mckey.getNext());
        asrsJob.setToLocation(sCar.getChargeLocation());
        asrsJob.setFromStation(mCar.getBlockNo());
        Location location = Location.getByLocationNo(sCar.getChargeLocation());
        MCar chargeSrm = mCar.getMCarByPosition(location.getPosition(), location.getLevel());
        asrsJob.setToStation(chargeSrm.getBlockNo());
        asrsJob.setStatus(AsrsJobStatus.RUNNING);
        asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
        asrsJob.setType(AsrsJobType.RECHARGED);
        asrsJob.setWareHouse(mCar.getWareHouse());
        session.save(asrsJob);
        if (StringUtils.isNotBlank(sCar.getOnMCar())) {
            //小车在母车上
            mCar.setMcKey(asrsJob.getMcKey());
            sCar.setMcKey(asrsJob.getMcKey());
        } else {
            //小车不在母车上
            mCar.setReservedMcKey(asrsJob.getMcKey());
            sCar.setMcKey(asrsJob.getMcKey());
        }

        hasJob = true;
        return hasJob;
    }

    /*
     * @author：ed_chen
     * @date：2018/4/17 15:57
     * @description：查找母车所在层的 入库任务
     * @param hasJob
     * @param mCar
     */
    public boolean findPutawayByLevelOfMcar(boolean hasJob, MCar mCar) {
        Block block = mCar.getPreBlockHasMckey(AsrsJobType.PUTAWAY);
        if (block != null) {
            //如果上一段block有mckey，
            if (block instanceof Conveyor) {
                Conveyor conveyor = (Conveyor) block;
                if (StringUtils.isNotBlank(conveyor.getMcKey())) {
                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                    //如果小车绑定母车的上一节是入库作业，设置小车的reservedmckey
                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                        //sCar.setReservedMcKey(block.getMcKey());
                        hasJob = true;
                    }
                }
            } else if (block instanceof StationBlock) {
                if (StringUtils.isNotBlank(block.getMcKey())) {
                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                        //sCar.setReservedMcKey(block.getMcKey());
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
    public boolean findStockRemovalByLevelOfMcar(boolean hasJob, MCar mCar) {
        //获取本层的出库任务
        Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:fStation order by id asc ").setMaxResults(1);
        query.setParameter("tp", AsrsJobType.RETRIEVAL);
        query.setParameter("fStation", mCar.getBlockNo());
        AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
        if (asrsJob != null) {
            //sCar.setReservedMcKey(asrsJob.getMcKey());
            hasJob = true;
        }
        return hasJob;
    }

    //生成换层任务
    public boolean changeLevel(int level, boolean hasJob) {

        Query charQuery = HibernateUtil.getCurrentSession().createQuery("from AsrsJob a where (a.type=:tp or a.type=:ttp or " +
                "a.type=:tttp) and exists(select 1 from MCar m where m.blockNo=a.fromStation and " +
                "m.position = :position )");
        charQuery.setParameter("tp", AsrsJobType.RECHARGED);
        charQuery.setParameter("ttp", AsrsJobType.RECHARGEDOVER);
        charQuery.setParameter("tttp", AsrsJobType.CHANGELEVEL);
        /*charQuery.setParameter("status", AsrsJobStatus.DONE);*/
        charQuery.setParameter("position", sCar.getPosition());
        List<AsrsJob> charQuerys = charQuery.list();
        if (!charQuerys.isEmpty()) {
            //已有同一个position充电或者充电完成或者换层作业
            hasJob = true;
            return hasJob;
        }

        if (com.util.common.StringUtils.isEmpty(sCar.getOnMCar())) {
            //子车不在母车上
            hasJob = true;
            return hasJob;
        }
        if (com.util.common.StringUtils.isNotEmpty(sCar.getMcKey()) || com.util.common.StringUtils.isNotEmpty(sCar.getReservedMcKey())) {
            //子车有任务，不能执行换层操作
            hasJob = true;
            return hasJob;
        }
        Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where level=:level  and position =:position");
        query.setParameter("level", level);
        query.setParameter("position", sCar.getPosition());
        query.setMaxResults(1);
        SCar levlScar = (SCar) query.uniqueResult();
        if (levlScar != null) {

        } else {
            Query q = HibernateUtil.getCurrentSession().createQuery("from MCar where level=:level and position=:po");
            q.setParameter("level", level);
            q.setParameter("po", sCar.getPosition());
            q.setMaxResults(1);
            MCar toMcar = (MCar) q.uniqueResult();

            AsrsJob asrsJob = new AsrsJob();
            asrsJob.setType(AsrsJobType.CHANGELEVEL);
            asrsJob.setStatus(AsrsJobStatus.RUNNING);
            asrsJob.setStatusDetail(AsrsJobStatusDetail.WAITING);
            asrsJob.setFromStation(sCar.getOnMCar());
            asrsJob.setToStation(toMcar.getBlockNo());
            asrsJob.setBarcode(sCar.getGroupNo()+"");
            asrsJob.setGenerateTime(new Date());
            asrsJob.setMcKey(Mckey.getNext());
            HibernateUtil.getCurrentSession().save(asrsJob);

            toMcar.setReservedMcKey(asrsJob.getMcKey());

            MCar fromMcar = (MCar) Block.getByBlockNo(sCar.getOnMCar());
            fromMcar.setMcKey(asrsJob.getMcKey());
            sCar.setReservedMcKey(asrsJob.getMcKey());

            hasJob = true;
        }
        return hasJob;
    }


        /*//子车在母车上
        if (StringUtils.isNotBlank(sCar.getOnMCar())) {
            MCar mCar = (MCar) MCar.getByBlockNo(sCar.getOnMCar());
            if (StringUtils.isNotBlank(mCar.getMcKey()) ) {
                //如果提升机上有任务，
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
                if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)
                        && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING)
                        && asrsJob.getToStation().equals(mCar.getBlockNo())) {
                    //如果提升机存在入库任务，子车设置reservedmckey，出库任务不管，默认子车已经取货完成上提升机了
                    sCar.setReservedMcKey(mCar.getMcKey());
                    asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                }else if(asrsJob.getType().equals(AsrsJobType.RECHARGED)
                        && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING)
                        && asrsJob.getToStation().equals(mCar.getBlockNo())
                        && StringUtils.isNotBlank(sCar.getOnMCar())){
                    sCar.setMcKey(mCar.getMcKey());
                    asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                }

            } else if (StringUtils.isNotBlank(mCar.getReservedMcKey())) {

                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
                //如果提升机上又预约任务
                if (StringUtils.isNotBlank(mCar.getsCarBlockNo()) && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING) ) {
                    if(!asrsJob.getType().equals(AsrsJobType.ST2ST)) {
                        if(mCar.getBlockNo().equals(asrsJob.getToStation())
                                || mCar.getBlockNo().equals(asrsJob.getFromStation())) {
                            sCar.setReservedMcKey(mCar.getReservedMcKey());
                            asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                        }
                    }
                }
            }

        } else {

            MCar mCar = MCar.getMCarByGroupNo(sCar.getGroupNo());
            boolean hasJob = false;
            //如果提升机上有任务，
            if (mCar != null && StringUtils.isNotBlank(mCar.getMcKey())) {
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
                if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)
                        && asrsJob.getToStation().equals(mCar.getBlockNo())
                        && asrsJob.getStatus().equals(AsrsJobStatus.RUNNING)) {
                    //如果提升机存在入库任务，子车设置reservedmckey，出库任务不管，默认子车已经取货完成上提升机了
                    //充电状态不是完成状态
                    sCar.setReservedMcKey(mCar.getMcKey());
                    asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                    hasJob = true;
                }
            }

            //,上车
            if (!hasJob && mCar != null) {
                ScarOperator operator = new ScarOperator(sCar, "9999");
                operator.tryOnMCar(mCar);
            }

        }*/


    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {

    }
}
