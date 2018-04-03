package com.asrs;

import com.util.hibernate.*;
import org.apache.commons.lang.*;

/**
 * Author: Zhouyue
 * Date: 2010-7-9
 * Time: 9:47:15
 * Copyright Daifuku Shanghai Ltd.
 */
public class Mckey
{
      public static String getNext()
      {
            return StringUtils.leftPad(String.valueOf(HibernateUtil.nextSeq("seq_mckey")),4,"0");
      }
}
