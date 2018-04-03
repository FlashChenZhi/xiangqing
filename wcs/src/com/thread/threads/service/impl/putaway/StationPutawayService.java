package com.thread.threads.service.impl.putaway;

import com.thread.blocks.StationBlock;
import com.thread.threads.service.impl.StationServiceImpl;

/**
 * Created by van on 2017/11/9.
 */
public class StationPutawayService extends StationServiceImpl {

    private StationBlock stationBlock;

    public StationPutawayService(StationBlock stationBlock) {
        super(stationBlock);
        this.stationBlock = stationBlock;
    }



}
