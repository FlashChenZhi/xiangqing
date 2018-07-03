package com.asrs.business.consts;

import com.asrs.Mckey;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.ScarChargeLocation;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 21:27 2018/7/2
 * @Description: 创建AsrsJob类
 * @Modified By:
 */
public class CreateAsrsJob {

    private Block block;

    public CreateAsrsJob(Block block) {
        this.block = block;
    }

    /*
     * @author：ed_chen
     * @date：2018/4/17 15:57
     * @description：生成小车的充电任务
     * @param
     * @return：boolean
     */
    public boolean createCharge(boolean hasJob, MCar mCar) {
        SCar sCar=(SCar)block;
        Session session = HibernateUtil.getCurrentSession();
        Query charQuery = session.createQuery("from AsrsJob a where (a.type=:tp or a.type=:ttp or " +
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

        List<ScarChargeLocation> scarChargeLocationList = ScarChargeLocation.getAbleChargeLocationBySCarBlockNo(sCar.getBlockNo());
        if(scarChargeLocationList.size()>0){
            //存在可用充电位
            hasJob=changeLevel(1,hasJob,2);
            if(!hasJob){
                //生成的换层任务
                ScarChargeLocation scarChargeLocation =scarChargeLocationList.get(0);
                scarChargeLocation.setReceved(true);

                Query q = HibernateUtil.getCurrentSession().createQuery("from MCar where level=:level and position=:po");
                q.setParameter("level", 1);
                q.setParameter("po", sCar.getPosition());
                q.setMaxResults(1);
                MCar toMcar = (MCar) q.uniqueResult();

                AsrsJob asrsJob = new AsrsJob();
                asrsJob.setMcKey(Mckey.getNext());
                asrsJob.setToLocation(scarChargeLocation.getChargeLocation().getLocationNo());
                asrsJob.setFromStation(toMcar.getBlockNo());
                Location location = Location.getByLocationNo(sCar.getChargeLocation());
                MCar chargeSrm = mCar.getMCarByPosition(location.getPosition(), location.getLevel());
                asrsJob.setToStation(chargeSrm.getBlockNo());
                asrsJob.setBarcode(sCar.getBlockNo());
                asrsJob.setStatus(AsrsJobStatus.RUNNING);
                asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                asrsJob.setType(AsrsJobType.RECHARGED);
                asrsJob.setWareHouse(mCar.getWareHouse());
                session.save(asrsJob);

            }
        }
        hasJob = true;
        return hasJob;
    }


    /*
     * @author：ed_chen
     * @date：2018/4/17 15:57
     * @description：生成充电完成任务
     * @param
     * @return：boolean
     */
    public boolean createChargeOver(boolean hasJob) {
        Session session = HibernateUtil.getCurrentSession();
        SCar sCar=(SCar) block;
        Query charQuery = session.createQuery("from AsrsJob a where (a.type=:tp or a.type=:ttp or " +
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
        //获取一层母车
        MCar levMCar = MCar.getMCarByPosition(sCar.getPosition(), sCar.getLevel());
        ScarChargeLocation scarChargeLocation = ScarChargeLocation.getReservedChargeLocationBySCarBlockNo(sCar.getBlockNo());
        if(scarChargeLocation!=null){
            //小车在一层，并且一层母车没有绑定小车，
            // 并且此母车没有mckey和reservedmckey（即没有换层任务到这个母车）
            SCar levSCar=null;

            if(StringUtils.isNotBlank(levMCar.getMcKey()) || StringUtils.isNotBlank(levMCar.getReservedMcKey()) || levMCar.isWaitingResponse()){
                hasJob = true;
                return hasJob;
            }
            if(levMCar.getGroupNo() != null){
                levSCar = SCar.getScarByGroup(levMCar.getGroupNo());
                if(StringUtils.isNotBlank(levSCar.getMcKey()) || StringUtils.isNotBlank(levSCar.getReservedMcKey()) || levSCar.isWaitingResponse()){
                    hasJob = true;
                    return hasJob;
                }
            }
            AsrsJob asrsJob = new AsrsJob();
            asrsJob.setMcKey(Mckey.getNext());
            asrsJob.setFromLocation(scarChargeLocation.getChargeLocation().getLocationNo());
            asrsJob.setFromStation(levMCar.getBlockNo());
            asrsJob.setToStation(levMCar.getBlockNo());
            asrsJob.setBarcode(sCar.getGroupNo().toString());
            asrsJob.setStatus(AsrsJobStatus.RUNNING);
            asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
            asrsJob.setType(AsrsJobType.RECHARGEDOVER);
            asrsJob.setWareHouse(levMCar.getWareHouse());

            sCar.setMcKey(asrsJob.getMcKey());
            sCar.setStatus(SCar.STATUS_CHARGE_OVER);
            levMCar.setReservedMcKey(asrsJob.getMcKey());

            if(levSCar!=null){
                levSCar.setReservedMcKey(asrsJob.getMcKey());
            }
            session.save(asrsJob);
        }

        hasJob = true;
        return hasJob;
    }


    /*
     * @author：ed_chen
     * @date：2018/7/2 21:35
     * @description：生成换层任务
     * @param level
     * @param hasJob
     * @param type
     * @return：boolean
     */
    public boolean changeLevel(int level, boolean hasJob,int type) {
        SCar sCar=(SCar)block;
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

        Query q = HibernateUtil.getCurrentSession().createQuery("from MCar where level=:level and position=:po");
        q.setParameter("level", level);
        q.setParameter("po", sCar.getPosition());
        q.setMaxResults(1);
        MCar toMcar = (MCar) q.uniqueResult();

        if(type==1){
            //普通换层，type=1，判断此层是否有小车。
            if (levlScar != null) {
                hasJob = true;
                return hasJob;
            }
        }else{
            //充电换层，type=2，不用判断此层是否有小车,判断小车母车是否有任务
            if(StringUtils.isNotBlank(levlScar.getMcKey()) || StringUtils.isNotBlank(levlScar.getReservedMcKey()) || levlScar.isWaitingResponse() ||
                    StringUtils.isNotBlank(toMcar.getMcKey()) || StringUtils.isNotBlank(toMcar.getReservedMcKey()) || toMcar.isWaitingResponse() ){
                hasJob = true;
                return hasJob;
            }
        }


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
        sCar.setMcKey(asrsJob.getMcKey());
        if(levlScar!=null && type==2){
            levlScar.setReservedMcKey(asrsJob.getMcKey());
        }

        hasJob = type==2?false:true;

        return hasJob;
    }

}
