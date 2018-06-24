package com.asrs.business.msgProc.msg35ProcService;

import com.asrs.domain.AsrsJob;
import com.asrs.message.Message35;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.thread.blocks.Block;

import java.rmi.RemoteException;

/**
 * @Author: ed_chen
 * @Date: Create in 22:21 2018/6/21
 * @Description:
 * @Modified By:
 */
public interface Msg35ProcService {

    void sCar35Proc() throws Exception;

    void srm35Proc()throws Exception;

    void mCar35Proc() throws Exception;

    void lift35Proc() throws Exception;

    void converyor35Proc() throws Exception;

    void station35Proc() throws Exception;
}
