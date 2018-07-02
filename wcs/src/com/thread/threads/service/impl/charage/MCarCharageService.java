package com.thread.threads.service.impl.charage;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.MCarOperator;
import com.thread.threads.operator.SrmOperator;
import com.thread.threads.service.impl.MCarServiceImpl;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

/**
 * Created by van on 2017/11/10.
 */

public class MCarCharageService extends MCarServiceImpl{

    private MCar srm;

    public MCarCharageService(MCar block) {
        super(block);
        this.srm = (MCar) block;

    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getReservedMcKey());
        MCarOperator operator = new MCarOperator(srm, asrsJob.getMcKey());
        if (StringUtils.isBlank(srm.getsCarBlockNo())) {
            Query query = HibernateUtil.getCurrentSession().createQuery("from SCar  where mcKey=:mckey");
            query.setParameter("mckey", asrsJob.getMcKey());
            SCar sCar = (SCar) query.uniqueResult();

            Location tempLocation = Location.getByLocationNo(sCar.getChargeChanel());
            //operator.tryLoadCarFromLocation(sCar, tempLocation);
        } else {
            SCar sCar  = SCar.getScarByGroup(srm.getGroupNo());
            Location tempLocation = Location.getByLocationNo(sCar.getTempLocation());
            operator.tryUnLoadCarToLocation(tempLocation);
        }
    }
    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getMcKey());
        MCarOperator operator = new MCarOperator(srm, asrsJob.getMcKey());
        Location toLocation = Location.getByLocationNo(asrsJob.getToLocation());

        if(StringUtils.isNotBlank(srm.getsCarBlockNo())){
            SCar sCar  = (SCar) SCar.getByBlockNo(srm.getsCarBlockNo());
            if (srm.getBlockNo().equals(asrsJob.getToStation())&&StringUtils.isNotBlank(srm.getsCarBlockNo())&&StringUtils.isNotBlank(sCar.getOnMCar())) {
                operator.tryUnLoadCarToLocation(toLocation);

            } else{
                //SCar sCar  = SCar.getScarByGroup(srm.getGroupNo());
                //Location tempLocation = Location.getByLocationNo(sCar.getChargeChanel());
//            if ( srm.getLevel() == toLocation.getLevel()
//                    && srm.getBay() != toLocation.getBay()&&!srm.isWaitingResponse()) {
//                operator.tryLoadCar();
//            }
            }
        }

    }
}
