package com.thread.threads.service.impl;

import com.thread.blocks.*;
import com.thread.threads.service.ConveyorService;

/**
 * Created by van on 2017/10/31.
 */
public abstract class ConveyorServiceImpl implements ConveyorService {

    private Conveyor conveyor;

    public ConveyorServiceImpl(Conveyor conveyor) {
        this.conveyor = conveyor;
    }

    @Override
    public void withOutJob() throws Exception {

    }

    @Override
    public abstract void withReserveMckey() throws Exception;

    @Override
    public abstract void withMckey() throws Exception ;
}
