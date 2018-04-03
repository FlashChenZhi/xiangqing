package com.thread.utils;

import com.thread.blocks.*;
import com.thread.threads.*;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ThreadFactory {
    public static BlockThread getThread(Block block) {
        if (block instanceof SCar) {
            return new SCarThread(block.getBlockNo());
        } else if (block instanceof Lift) {
            return new LiftThread(block.getBlockNo());
        } else if (block instanceof StationBlock) {
            return new StationThread(block.getBlockNo());
        } else if (block instanceof MCar) {
            return new MCarThread(block.getBlockNo());
        } else if (block instanceof Conveyor) {
            return new ConveyorThread(block.getBlockNo());
        } else if (block instanceof Srm){
            return new SrmThread(block.getBlockNo());
        }
        return null;
    }
}

