package com.asrs.communication;

import com.asrs.message.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-20
 * Time: 13:29:52
 * Copyright Dsl.Worgsoft.
 */
public interface MessageProxy extends Remote
{
      public void addSndMsg(Message msg) throws RemoteException;

      public void addRcvdMsg(MessageBuilder mb) throws RemoteException;

      public MessageBuilder getRcvdMsg() throws RemoteException, InterruptedException;

      public Message getSndMsg(String plcName) throws RemoteException, InterruptedException;
}
