package com.asrs.thread;

import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.wms.domain.*;
import com.wms.domain.blocks.*;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangfan
 * Created on 2017/3/11.
 */
public class JobDoHelp {

    /**
     * 入库完成
     *
     * @param mckey
     */
    public static void finishPutaway(String mckey) {
        LogWriter.info(LoggerType.WMS, "强制完成入库任务mckey" + mckey);
//        Job job = Job.getByMcKey(mckey);
//        AsrsJob asrsJob = AsrsJob.getByMckey(mckey);
//        Crane crane = (Crane) Block.getByBlockNo("ML01");
//        SCar sCar = (SCar) Block.getByBlockNo("SC01");
//        StationBlock station = (StationBlock) Block.getByBlockNo("0001");
//
//
//        if (job != null) {
//
//            Location toLocation = job.getToLocation();
//            List<JobDetail> details = new ArrayList<JobDetail>(job.getJobDetails());
//            for (JobDetail detail : details) {
//
//                Container container = detail.getInventory().getContainer();
//                container.setLocation(toLocation);
//                toLocation.setEmpty(false);
//                toLocation.setReserved(false);
//                Inventory inventory = detail.getInventory();
//
//                StoreResult storeResult = new StoreResult();
//                storeResult.setQty(inventory.getQty());
//                storeResult.setSyncFlag("1");
//                storeResult.setLocationNo(job.getToLocation().getLocationNo());
//                storeResult.setPalletBarcode(container.getBarcode());
//                storeResult.setStoreDate(DateFormat.format(new Date(), DateFormat.YYYYMMDD));
//                storeResult.setStoreTime(DateFormat.format(new Date(), DateFormat.HHMMSS));
//                HibernateUtil.getCurrentSession().saveOrUpdate(storeResult);
//
//            }
//        }
//
//        if (mckey.equals(station.getMcKey())) {
//            station.setMcKey(null);
//        }
//
//        if (mckey.equals(sCar.getMcKey())
//                || mckey.equals(sCar.getReservedMcKey())) {
//
//            sCar.setMcKey(null);
//            sCar.setOnCarNo("ML01");
//            sCar.setWaitingResponse(false);
//
//
//        }
//
//        if (mckey.equals(crane.getMcKey())
//                || mckey.equals(crane.getReservedMcKey())) {
//
//            crane.setsCarNo("SC01");
//            crane.setWaitingResponse(false);
//
//        }
//
//        HibernateUtil.getCurrentSession().delete(job);
//        HibernateUtil.getCurrentSession().delete(asrsJob);

    }

    /**
     * 入库取消
     *
     * @param mckey
     */
    public static void cancelPutaway(String mckey) {
        LogWriter.info(LoggerType.WMS, "强制取消入库任务mckey" + mckey);
        System.out.println("强制取消入库任务mckey" + mckey);
        Session session = HibernateUtil.getCurrentSession();
        cancelBlockData(mckey);


        Job job = Job.getByMcKey(mckey);

        if (job != null) {

            InventoryView inventoryView = InventoryView.getByPalletNo(job.getContainer());
            session.delete(inventoryView);


            Location location = job.getToLocation();
            if (location != null) {
                location.setReserved(false);
                location.setEmpty(true);
            }
            session.delete(job);

        }

        Query asrsJobQ = session.createQuery("from AsrsJob where mcKey=:mckey");
        asrsJobQ.setParameter("mckey", mckey);
        asrsJobQ.setMaxResults(1);
        AsrsJob asrsJob = (AsrsJob) asrsJobQ.uniqueResult();
        if (asrsJob != null){
            asrsJob.delete();
        }


    }

    /**
     * 出库取消
     *
     * @param mckey
     */
    public static void retirevalCancel(String mckey) {
        LogWriter.info(LoggerType.WMS, "强制取消出库任务mckey" + mckey);
        Session session = HibernateUtil.getCurrentSession();
        cancelBlockData(mckey);

        Job job = Job.getByMcKey(mckey);
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);


        if (job != null){
            Container container = Container.getByBarcode(job.getContainer());
            container.setReserved(false);
            Location location = container.getLocation();
            location.setReserved(false);
            location.setRetrievalRestricted(false);
            session.delete(job);
        }

        if (asrsJob != null){
            asrsJob.delete();
        }

    }


    private static void cancelBlockData(String mckey) {
        Session session = HibernateUtil.getCurrentSession();
        List<Block> blocks = new ArrayList<>();
        List<Block> reservedBlocks = new ArrayList<>();
        addBlock("Conveyor",mckey,blocks,reservedBlocks);
        addBlock("Lift",mckey,blocks,reservedBlocks);
        addBlock("MCar",mckey,blocks,reservedBlocks);
        addBlock("SCar",mckey,blocks,reservedBlocks);
        addBlock("Srm",mckey,blocks,reservedBlocks);
        addBlock("StationBlock",mckey,blocks,reservedBlocks);

        for(Block block : blocks) {
            block.setWaitingResponse(false);
            if (block instanceof SCar) {
                SCar sCar = (SCar) block;

                sCar.setMcKey(null);
                sCar.setBank(0);

                Query q = session.createQuery("from Srm b where b.groupNo = :groupNo")
                        .setInteger("groupNo", sCar.getGroupNo());
                Srm srm = (Srm) q.uniqueResult();
                if (srm != null) {
                    sCar.setOnMCar(srm.getBlockNo());
                    srm.setsCarBlockNo(sCar.getBlockNo());
                } else {
                    q = session.createQuery("from MCar b where b.groupNo = :groupNo")
                            .setInteger("groupNo", sCar.getGroupNo());
                    MCar mCar = (MCar) q.uniqueResult();
                    if (mCar != null) {
                        sCar.setOnMCar(mCar.getBlockNo());
                        mCar.setsCarBlockNo(sCar.getBlockNo());
                    }
                }

            } else if (block instanceof Conveyor) {
                Conveyor conveyor = (Conveyor) block;
                conveyor.setMcKey(null);
            } else if (block instanceof MCar) {
                MCar mCar = (MCar) block;
                mCar.setMcKey(null);
                mCar.setBay(0);
                mCar.setCheckLocation(false);
            } else if (block instanceof Srm) {
                Srm srm = (Srm) block;
                srm.setMcKey(null);
                srm.setBay(0);
                srm.setLevel(1);
                srm.setCheckLocation(false);
            }else if (block instanceof Lift) {
                Lift lift = (Lift) block;
                lift.setMcKey(null);
                lift.setDock(null);
                lift.setLevel(0);
            } else if (block instanceof StationBlock) {
                StationBlock stationBlock = (StationBlock) block;
                stationBlock.setMcKey(null);
            }
        }

        for(Block reservedBlock : reservedBlocks) {
            reservedBlock.setWaitingResponse(false);
            if (reservedBlock instanceof SCar) {
                SCar sCar = (SCar) reservedBlock;

                sCar.setReservedMcKey(null);
                sCar.setBank(0);

                Query q = session.createQuery("from Srm b where b.groupNo = :groupNo")
                        .setInteger("groupNo", sCar.getGroupNo());
                Srm srm = (Srm) q.uniqueResult();
                if (srm != null) {
                    sCar.setOnMCar(srm.getBlockNo());
                    srm.setsCarBlockNo(sCar.getBlockNo());
                } else {
                    q = session.createQuery("from MCar b where b.groupNo = :groupNo")
                            .setInteger("groupNo", sCar.getGroupNo());
                    MCar mCar = (MCar) q.uniqueResult();
                    if (mCar != null) {
                        sCar.setOnMCar(mCar.getBlockNo());
                        mCar.setsCarBlockNo(sCar.getBlockNo());
                    }
                }

            } else if (reservedBlock instanceof MCar) {
                MCar mCar = (MCar) reservedBlock;
                mCar.setReservedMcKey(null);
                mCar.setCheckLocation(false);
            } else if (reservedBlock instanceof Srm) {
                Srm srm = (Srm) reservedBlock;
                srm.setReservedMcKey(null);
                srm.setBay(0);
                srm.setLevel(1);
                srm.setCheckLocation(false);
            } else if(reservedBlock instanceof Lift){
                Lift lift = (Lift) reservedBlock;
                lift.setReservedMcKey(null);
                lift.setDock(null);
                lift.setLevel(0);
            }
        }


    }

    private static void addBlock(String table,String mckey,List<Block> blocks,List<Block> reservedBlocks) {
        Session session = HibernateUtil.getCurrentSession();
        Query q = session.createQuery("from " + table +" b where b.mcKey = :mckey")
                .setString("mckey", mckey);
        blocks.addAll(q.list());
        q = session.createQuery("from " + table +" b where b.reservedMcKey = :mckey")
                .setString("mckey", mckey);
        reservedBlocks.addAll(q.list());
    }

    /**
     * 出库完成
     *
     * @param mckey
     */
    public static void retrievalFinish(String mckey) {
//        LogWriter.info(LoggerType.WMS, "强制完成出库任务mckey" + mckey);
//
//        Job job = Job.getByMcKey(mckey);
//        AsrsJob asrsJob = AsrsJob.getByMckey(mckey);
//
//        Crane crane = (Crane) Block.getByBlockNo("ML01");
//        SCar sCar = (SCar) Block.getByBlockNo("SC01");
//        StationBlock station = (StationBlock) Block.getByBlockNo("0003");
//        Dock dock = (Dock) Block.getByBlockNo("0002");
//        if (mckey.equals(dock.getMcKey()) || mckey.equals(dock.getReservedMcKey())) {
//            dock.setWaitingResponse(false);
//            dock.setMcKey(null);
//            dock.setReservedMcKey(null);
//        }
//        if (mckey.equals(station.getMcKey()) || mckey.equals(station.getReservedMcKey())) {
//            station.setReservedMcKey(null);
//            station.setMcKey(null);
//            station.setWaitingResponse(false);
//        }
//
//        Container c = new Container();
//
//        List<JobDetail> details = new ArrayList<JobDetail>(job.getJobDetails());
//        for (JobDetail detail : details) {
//            Inventory inventory = detail.getInventory();
//
//            org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from ReceivingPlan where batchNo =:batchNo").setMaxResults(1);
////            query.setParameter("batchNo", inventory.getBatchNo());
//            ReceivingPlan receivingPlan = (ReceivingPlan) query.uniqueResult();
//            receivingPlan.setRecvedQty(receivingPlan.getRecvedQty().subtract(inventory.getQty()));
//            HibernateUtil.getCurrentSession().update(receivingPlan);
//
//            Location location = job.getFromLocation();
//            location.setEmpty(true);
//            location.setRetrievalRestricted(false);
//            HibernateUtil.getCurrentSession().update(location);
//
//            HibernateUtil.getCurrentSession().delete(inventory);
//            HibernateUtil.getCurrentSession().delete(c);
//
//
//        }
//
//        if (mckey.equals(sCar.getMcKey()) || mckey.equals(sCar.getReservedMcKey())) {
//            sCar.setOnCarNo("ML01");
//            crane.setsCarNo("SC01");
//            sCar.setReservedMcKey(null);
//            sCar.setMcKey(null);
//            crane.setReservedMcKey(null);
//            crane.setMcKey(null);
//            sCar.setWaitingResponse(false);
//
//        }
//
//        if (mckey.equals(crane.getMcKey()) || mckey.equals(crane.getReservedMcKey())) {
//            crane.setMcKey(null);
//            crane.setReservedMcKey(null);
//            crane.setWaitingResponse(false);
//
//            crane.setBay(2);
//            crane.setLevel(0);
//
//        }
//
//        if (job != null) {
//            HibernateUtil.getCurrentSession().delete(job);
//        }
//
//        if (asrsJob != null) {
//            HibernateUtil.getCurrentSession().delete(asrsJob);
//        }


    }
}
