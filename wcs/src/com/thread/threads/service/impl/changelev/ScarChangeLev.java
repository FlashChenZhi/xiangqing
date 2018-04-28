package com.thread.threads.service.impl.changelev;

import com.thread.blocks.SCar;
import com.thread.threads.service.impl.ScarAndMCarServiceImpl;

/**
 * Created by van on 2018/3/15.
 */
public class ScarChangeLev extends ScarAndMCarServiceImpl {

    private SCar sCar;

    public ScarChangeLev(SCar sCar) {
        super(sCar);
        this.sCar = sCar;
    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {

    }

}
