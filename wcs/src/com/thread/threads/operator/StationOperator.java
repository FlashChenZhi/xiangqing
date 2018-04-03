package com.thread.threads.operator;

import com.asrs.message.Message03;
import com.thread.blocks.Conveyor;
import com.thread.blocks.Srm;
import com.thread.blocks.StationBlock;
import com.thread.utils.MsgSender;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2017/11/2.
 */
public class StationOperator {

    private StationBlock stationBlock;
    private String mckey;

    public StationOperator(StationBlock stationBlock, String mckey) {
        this.stationBlock = stationBlock;
        this.mckey = mckey;
    }

    /**
     * 移栽卸货
     *
     * @param nextBlock
     * @throws Exception
     */
    public void moveUnloadGoods(String nextBlock) throws Exception {
        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, stationBlock, "", nextBlock, "", "");
    }

    /**
     * 移栽取货
     *
     * @param preBlock
     * @throws Exception
     */
    public void moveCarryGoods(String preBlock) throws Exception {
        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, stationBlock, "", preBlock, "", "");
    }


    /**
     * 站台尝试移动到输送机
     *
     * @param nextBlock
     */
    public void tryMoveToConveyor(Conveyor nextBlock) throws Exception {
        if (nextBlock.isManty()) {
            //运行block多个输送机
                MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, stationBlock, "", nextBlock.getBlockNo(), "", "");
                MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, nextBlock, "", stationBlock.getBlockNo(), "", "");
        } else {
            if (StringUtils.isBlank(nextBlock.getMcKey())
                    && StringUtils.isBlank(nextBlock.getReservedMcKey())
                    && !nextBlock.isWaitingResponse()) {
                MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, stationBlock, "", nextBlock.getBlockNo(), "", "");
                MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, nextBlock, "", stationBlock.getBlockNo(), "", "");
            }
        }

    }

    /**
     * 已在卸货到输送机
     *
     * @param nextBlock
     * @throws Exception
     */
    public void tryMoveToSrm(Srm nextBlock) throws Exception {
        if (stationBlock.getBlockNo().equals(nextBlock.getDock()) && StringUtils.isBlank(nextBlock.getMcKey())) {
            MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, stationBlock, "", nextBlock.getBlockNo(), "", "");
        }
    }
}
