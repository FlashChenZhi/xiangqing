package com.thread.threads.service.impl.charage;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.SrmOperator;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

/**
 * Created by van on 2017/11/10.
 */
public class SrmCharageService extends SrmAndScarServiceImpl {

    private Srm srm;

    public SrmCharageService(Block block) {
        super(block);
        this.srm = (Srm) block;

    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getReservedMcKey());
        SrmOperator operator = new SrmOperator(srm, asrsJob.getMcKey());
        if (StringUtils.isBlank(srm.getsCarBlockNo())) {
            Query query = HibernateUtil.getCurrentSession().createQuery("from SCar  where mcKey=:mckey");
            query.setParameter("mckey", asrsJob.getMcKey());
            SCar sCar = (SCar) query.uniqueResult();

            Location tempLocation = Location.getByLocationNo(sCar.getChargeChanel());
            operator.tryLoadCarFromLocation(sCar, tempLocation);
        } else {
            SCar sCar  = SCar.getScarByGroup(srm.getGroupNo());
            Location tempLocation = Location.getByLocationNo(sCar.getTempLocation());
            operator.tryUnLoadCarToLocation(tempLocation);
        }
    }

    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getMcKey());
        SrmOperator operator = new SrmOperator(srm, asrsJob.getMcKey());
        Location toLocation = Location.getByLocationNo(asrsJob.getToLocation());
        if (srm.getBlockNo().equals(asrsJob.getToStation())) {
            operator.tryUnLoadCarToLocation(toLocation);

        } else {
            SCar sCar  = SCar.getScarByGroup(srm.getGroupNo());
            Location tempLocation = Location.getByLocationNo(sCar.getChargeChanel());
            operator.tryUnLoadCarToLocation(tempLocation);

        }
    }
}
