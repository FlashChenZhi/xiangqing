package com.thread.threads.service.impl;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.thread.blocks.Block;
import com.thread.blocks.Lift;
import com.thread.threads.service.LiftService;
import com.util.common.StringUtils;

/**
 * Created by van on 2017/10/31.
 */
public class LiftServiceImpl implements LiftService {

    private Lift lift;

    public LiftServiceImpl(Lift lift) {
        this.lift = lift;
    }

    @Override
    public void withOutJob() throws Exception {

        //查找入库
        Block block = lift.getPreBlockHasMckey(AsrsJobType.PUTAWAY);
        if (block != null) {
            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
             if((block.getBlockNo().equals("0045")&&(asrsJob.getFromStation().equals("0040")||asrsJob.getFromStation().equals("0039")))||asrsJob.getFromStation().equals("0063")||asrsJob.getFromStation().equals("0057")||block.getBlockNo().equals("0019")){
                    lift.setReservedMcKey(block.getMcKey());
            }
        }

        if (StringUtils.isEmpty(lift.getReservedMcKey())) {
            block = lift.getPreBlockHasMckey(AsrsJobType.RETRIEVAL);
            if (block != null) {
                lift.setReservedMcKey(block.getMcKey());
            }
        }

        if(StringUtils.isEmpty(lift.getReservedMcKey())){
            block = lift.getPreBlockHasMckey(AsrsJobType.RECHARGED);
            if (block != null) {
                lift.setReservedMcKey(block.getMcKey());
            }
        }

        if(StringUtils.isEmpty(lift.getReservedMcKey())){
            block = lift.getPreBlockHasMckey(AsrsJobType.RECHARGEDOVER);
            if (block != null) {
                lift.setReservedMcKey(block.getMcKey());
            }
        }
        if(StringUtils.isEmpty(lift.getReservedMcKey())){
            block = lift.getPreBlockHasMckey(AsrsJobType.CHANGELEVEL);
            if (block != null) {
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                if(asrsJob.getType().equals(AsrsJobType.CHANGELEVEL)){
                        lift.setReservedMcKey(block.getMcKey());
                }
            }
        }

    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {

    }
}
