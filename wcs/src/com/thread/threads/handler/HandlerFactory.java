package com.thread.threads.handler;

import com.asrs.message.Message35;
import com.thread.blocks.*;

/**
 * Created by van on 2017/12/27.
 */
public class HandlerFactory {

    public static MsgHandler getInstance(Message35 msg35) {
        String blockNo = msg35.MachineNo;
        Block block = Block.getByBlockNo(blockNo);
        if (block instanceof StationBlock) {

        } else if (block instanceof SCar) {
            return new SCarHandler(msg35, (SCar) block);
        } else if (block instanceof MCar) {

        } else if (block instanceof Srm) {
            return new SrmHandler(msg35, (Srm) block);
        } else if (block instanceof Conveyor) {

        } else if (block instanceof Lift) {

        }
        return null;
    }

}
