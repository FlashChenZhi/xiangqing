package com.thread.threads.service.impl.putaway;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.impl.ScarAndSrmServiceImpl;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by van on 2017/11/9.
 */
public class ScarAndSrmPutawayServcie extends ScarAndSrmServiceImpl {

    private SCar sCar;

    public ScarAndSrmPutawayServcie(SCar sCar) {
        super(sCar);
        this.sCar = sCar;
    }


    @Override
    public void withOutJob()throws Exception {

     }


    /**
     * 伊利修改：
     * 子车卸货分解： 1 先载货下车，2：卸货
     * @throws Exception
     */
    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
        ScarOperator operator = new ScarOperator(sCar, asrsJob.getMcKey());
        MCar srm = MCar.getMCarByGroupNo(sCar.getGroupNo());
        if (StringUtils.isBlank(sCar.getOnMCar())) {
            operator.tryOnMCar(srm);
        }else{
            operator.tryOffCarCarryGoodsFromMcar(srm);
        }
    }


    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
        ScarOperator operator = new ScarOperator(sCar, asrsJob.getMcKey());
        if (StringUtils.isNotBlank(sCar.getOnMCar())) {
            MCar srm = MCar.getMCarByGroupNo(sCar.getGroupNo());
            operator.tryOffCarCarryGoodsFromMcar(srm);
        }else{
            MCar srm = MCar.getMCarByGroupNo(sCar.getGroupNo());
                operator.tryUnloadGoods(srm);
        }
    }
}
