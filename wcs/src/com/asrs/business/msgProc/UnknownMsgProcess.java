package com.asrs.business.msgProc;

import com.asrs.communication.*;
import com.asrs.message.*;
import com.util.common.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-16
 * Time: 11:00:06
 * Copyright Dsl.Worgsoft.
 */
public class UnknownMsgProcess implements MsgProcess
{
      public void Do(MessageBuilder msg)
      {
            LogWriter.writeError(this.getClass(), "Unknown message" + msg.ID);
      }

      public void setProxy(XmlProxy wmsProxy,MessageProxy wcsProxy)
      {
      }
}
