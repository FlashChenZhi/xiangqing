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

        Block block= null;
        /**
         * 注：先查找充电，充电完成，换层任务，再查入库，出库任务
         *     加上任务类型判断，是因为出库任务和换层任务接驳台到提升机的这段路经是一样的，
         *     为了防止拿错任务。
         */

        if(StringUtils.isEmpty(lift.getReservedMcKey())){
            //查找充电
            block = lift.getPreBlockHasMckey(AsrsJobType.RECHARGED);
            if (block != null) {
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                //判断所拿任务是否是充电任务
                if(asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                    lift.setReservedMcKey(block.getMcKey());
                }
            }
        }

        if(StringUtils.isEmpty(lift.getReservedMcKey())){
            //查找充电完成
            block = lift.getPreBlockHasMckey(AsrsJobType.RECHARGEDOVER);
            if (block != null) {
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                //判断所拿任务是否是充电完成任务
                if(asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                    lift.setReservedMcKey(block.getMcKey());
                }
            }
        }
        if(StringUtils.isEmpty(lift.getReservedMcKey())){
            //查找换层
            block = lift.getPreBlockHasMckey(AsrsJobType.CHANGELEVEL);
            if (block != null) {
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                if(asrsJob.getType().equals(AsrsJobType.CHANGELEVEL)){
                    lift.setReservedMcKey(block.getMcKey());
                }
            }
        }

        if (StringUtils.isEmpty(lift.getReservedMcKey())) {
            //查找入库
            block = lift.getPreBlockHasMckey(AsrsJobType.PUTAWAY);
            if (block != null) {
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                //判断所拿任务是否是入库任务
                if(asrsJob.getType().equals(AsrsJobType.PUTAWAY)){
                    lift.setReservedMcKey(block.getMcKey());
                }

            }
        }
        if (StringUtils.isEmpty(lift.getReservedMcKey())) {
            //查找出库
            block = lift.getPreBlockHasMckey(AsrsJobType.RETRIEVAL);
            if (block != null) {
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                //判断所拿任务是否是出库任务
                if(asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
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
