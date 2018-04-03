package com.asrs.business.msgProc;

import com.asrs.communication.*;
import com.asrs.message.*;

import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-12
 * Time: 14:12:24
 * Copyright Dsl.Worgsoft.
 */
public interface MsgProcess
{
      public void Do(MessageBuilder msg) throws MsgException, RemoteException;

      void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy);
}
