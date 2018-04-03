package com.thread.threads.service.impl;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.*;
import com.thread.threads.operator.SrmOperator;
import com.thread.threads.service.SrmService;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

/**
 * Created by van on 2017/10/30.
 */
public abstract class SrmAndScarServiceImpl implements SrmService {

    private Srm srm;

    public SrmAndScarServiceImpl(Block block) {
        this.srm = (Srm) block;
    }

    @Override
    public void withOutJob() throws Exception {

        Query charQuery = HibernateUtil.getCurrentSession().createQuery("from AsrsJob  where (type=:tp or type=:ttp) and (fromStation=:fs or toStation=:ts) and status!=:status");
        charQuery.setParameter("tp", AsrsJobType.RECHARGED);
        charQuery.setParameter("ttp", AsrsJobType.RECHARGEDOVER);
        charQuery.setParameter("status", AsrsJobStatus.DONE);
        charQuery.setParameter("fs", srm.getBlockNo());
        charQuery.setParameter("ts", srm.getBlockNo());
        charQuery.setMaxResults(1);
        AsrsJob chargedJob = (AsrsJob) charQuery.uniqueResult();

        if (chargedJob != null) {

            if (chargedJob.getType().equals(AsrsJobType.RECHARGED)) {
                //如果仓库里存在充电作业，不查找其他作业，
                Srm fromSrm = (Srm) Srm.getByBlockNo(chargedJob.getFromStation());
                Location location = Location.getByLocationNo(chargedJob.getToLocation());
                if (fromSrm.getBlockNo().equals(srm.getBlockNo())) {
                    //如果充电子车绑定的堆垛机就是当前堆垛机
                    //不用处理，作业生成的时候自动绑定任务

                } else {
                    //如果充电子车绑定的堆垛机不是当前堆垛机
                    if (srm.getPosition().equals(location.getPosition())) {
                        //如果当前堆垛机和充电货位的位置是一边
                        if (!chargedJob.getStatus().equals(AsrsJobStatus.DONE))
                            srm.setReservedMcKey(chargedJob.getMcKey());
                    } else {
                        //如果当前堆垛机和冲淡位置不是一边
                        //不用理睬
                    }
                }
            } else if (chargedJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                //系统存在充电完成作业
                Srm toSrm = (Srm) Srm.getByBlockNo(chargedJob.getToStation());
                Location location = Location.getByLocationNo(chargedJob.getFromLocation());
                SCar sCar = SCar.getScarByGroup(toSrm.getGroupNo());
                if (toSrm.getBlockNo().equals(srm.getBlockNo())) {
                    //目标堆垛机是当前堆垛机
                    if (srm.getPosition().equals(location.getPosition())) {
                        //当前堆垛机位置和充电位置一直
                        //不用理睬
                    } else {
                        //当前堆垛机在充电位置的另一边
                        if (!chargedJob.getStatus().equals(AsrsJobStatus.DONE))
                            toSrm.setReservedMcKey(chargedJob.getMcKey());
                    }
                } else {
                    //目标堆垛机不是当前堆垛机
                    if (srm.getPosition().equals(location.getPosition())) {
                        //当前堆垛机和充电位置堆垛机一直
                        if (sCar.getBank() == location.getBank()
                                && sCar.getBay() == location.getBay()
                                && sCar.getLevel() == location.getLevel()
                                && sCar.getPosition().equals(srm.getPosition())) {
                            //如果子车和堆垛机在同一边，并且子车在充电位子上
                            srm.setReservedMcKey(chargedJob.getMcKey());
                        }
                    }
                }
            }

        } else {
            //仓库里没有充电作业，执行正常操作
            //移动提升机上没有子车
            if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                //获取子车
                SCar sCar = SCar.getScarByGroup(srm.getGroupNo());
                //检查子车上是否有任务
                if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                    //子车有出库预约任务，提升机预约任务
                    srm.setReservedMcKey(sCar.getReservedMcKey());

                } else {
                    //子车不在提升机上
                    //获取入库任务
                    boolean hasJob = false;

                    //堆垛机上的子车电量不足，生成充电任务
                    if (sCar.getPower() < 40) {
                        AsrsJob asrsJob = new AsrsJob();
                        asrsJob.setMcKey(Mckey.getNext());
                        asrsJob.setToLocation(sCar.getChargeLocation());
                        asrsJob.setFromStation(srm.getBlockNo());
                        Location location = Location.getByLocationNo(sCar.getChargeLocation());
                        Srm chargeSrm = srm.getSrmByPosition(location.getPosition());
                        asrsJob.setToStation(chargeSrm.getBlockNo());
                        asrsJob.setStatus(AsrsJobStatus.RUNNING);
                        asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                        asrsJob.setType(AsrsJobType.RECHARGED);
                        asrsJob.setWareHouse(srm.getWareHouse());
                        HibernateUtil.getCurrentSession().save(asrsJob);
                        srm.setMcKey(asrsJob.getMcKey());
                        sCar.setMcKey(asrsJob.getMcKey());
                        hasJob = true;
                    }


                    Block block = srm.getPreBlockHasMckey(AsrsJobType.PUTAWAY);
                    if (!hasJob) {
                        if (block != null) {
                            //如果上一段block有mckey，
                            if (block instanceof Conveyor) {
                                Conveyor conveyor = (Conveyor) block;
                                if (StringUtils.isNotBlank(conveyor.getMcKey()) && (!conveyor.isWaitingResponse() || (!conveyor.isMantWaiting() && conveyor.isManty()))) {
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                                        srm.setReservedMcKey(block.getMcKey());
                                        hasJob = true;
                                    }
                                }
                            } else if (block instanceof StationBlock) {
                                if (StringUtils.isNotBlank(block.getMcKey()) && !block.isWaitingResponse()) {
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                                        srm.setReservedMcKey(block.getMcKey());
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
                        query.setParameter("fStation", srm.getBlockNo());
                        AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                        if (asrsJob != null) {
                            srm.setReservedMcKey(asrsJob.getMcKey());
                            hasJob = true;
                        }
                    }

                    //获取出库任务
                    if (!hasJob) {
                        Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:fStation order by id asc ").setMaxResults(1);
                        query.setParameter("tp", AsrsJobType.LOCATIONTOLOCATION);
                        query.setParameter("fStation", srm.getBlockNo());
                        AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                        if (asrsJob != null) {
                            srm.setReservedMcKey(asrsJob.getMcKey());
                            hasJob = true;
                        }
                    }
                    if (!hasJob) {
                        Block bb = srm.getPreBlockHasMckey(AsrsJobType.ST2ST);
                        if (bb != null) {
                            //如果上一段block有mckey，
                            if (bb instanceof Conveyor) {
                                Conveyor conveyor = (Conveyor) bb;
                                if (StringUtils.isNotBlank(conveyor.getMcKey()) && (!conveyor.isWaitingResponse() || (!conveyor.isMantWaiting() && conveyor.isManty()))) {
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(bb.getMcKey());
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    if (asrsJob.getType().equals(AsrsJobType.ST2ST)) {
                                        srm.setReservedMcKey(bb.getMcKey());
                                        hasJob = true;
                                    }
                                }
                            } else if (bb instanceof StationBlock) {
                                if (StringUtils.isNotBlank(bb.getMcKey()) && !bb.isWaitingResponse()) {
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(bb.getMcKey());
                                    if (asrsJob.getType().equals(AsrsJobType.ST2ST)) {
                                        srm.setReservedMcKey(bb.getMcKey());
                                        hasJob = true;
                                    }
                                }
                            }
                        }
                    }


                    if (hasJob) {
                        AsrsJob asrsJob = null;
                        if (StringUtils.isNotBlank(srm.getReservedMcKey()))
                            asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getReservedMcKey());
                        else if (StringUtils.isNotBlank(srm.getMcKey()))
                            asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getMcKey());
                        asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                    } else {
                        if (!srm.getCycle().equals(srm.getDock())) {
//                            SrmOperator srmOperator = new SrmOperator(srm, "9999");
//                            srmOperator.cycle(srm);
                        }
                    }
                }

            } else {
                SCar sCar = SCar.getScarByGroup(srm.getGroupNo());
                boolean hasJob = false;

                if (sCar != null) {

                    if (StringUtils.isNotBlank(sCar.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                        if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            srm.setReservedMcKey(sCar.getMcKey());
                            hasJob = true;
                        }
                    }

                    if (!hasJob) {
                        if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                            if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                                srm.setReservedMcKey(sCar.getMcKey());
                                hasJob = true;
                            }
                        }
                    }

                    if (!hasJob) {
                        //查找入库任务
                        Block block = srm.getPreBlockHasMckey(AsrsJobType.PUTAWAY);
                        if (block != null) {
                            //如果上一段block有mckey，设置提升机reservedmckey
                            if (block instanceof Conveyor) {
                                Conveyor conveyor = (Conveyor) block;
                                if (StringUtils.isNotBlank(conveyor.getMcKey()) && (!conveyor.isWaitingResponse() || (!conveyor.isMantWaiting() && conveyor.isManty()))) {
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                                        srm.setReservedMcKey(block.getMcKey());
                                        hasJob = true;
                                    }
                                }
                            } else if (block instanceof StationBlock) {
                                if (StringUtils.isNotBlank(block.getMcKey()) && !block.isWaitingResponse()) {
                                    //如果提升机的上一节是入库作业，设置提升机reservedmckey
                                    AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                                    if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                                        srm.setReservedMcKey(block.getMcKey());
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
                    query.setParameter("st", srm.getBlockNo());
                    AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                    if (asrsJob != null) {
                        srm.setReservedMcKey(asrsJob.getMcKey());
                        hasJob = true;
                    }
                }

                if (!hasJob) {
                    Query query = HibernateUtil.getCurrentSession().createQuery(" from AsrsJob where type=:tp and statusDetail = '0' and fromStation=:st order by id asc ").setMaxResults(1);
                    query.setParameter("tp", AsrsJobType.LOCATIONTOLOCATION);
                    query.setParameter("st", srm.getBlockNo());
                    AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                    if (asrsJob != null) {
                        srm.setReservedMcKey(asrsJob.getMcKey());
                        hasJob = true;
                    }
                }


                if (!hasJob) {
                    if (StringUtils.isBlank(sCar.getMcKey()) && StringUtils.isBlank(sCar.getReservedMcKey())) {
                        SrmOperator srmOperator = new SrmOperator(srm, "9999");
                        srmOperator.tryLoadCar();
                    }
                }
            }
        }
    }

}
