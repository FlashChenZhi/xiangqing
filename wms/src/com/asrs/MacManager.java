package com.asrs;

import com.asrs.business.consts.*;

import java.util.HashMap;

/**
 * Author: Zhouyue
 * Date: 2008-10-20
 * Time: 15:14:00
 * Copyright Daifuku Shanghai Ltd.
 */
public class MacManager
{
      private static MacManager instance = new MacManager();

      private MacManager()
      {
      }

      public static MacManager Instance()
      {
            return instance;
      }

      HashMap<String, String> macs = new HashMap<String, String>();

      public String getStatus(String mac) throws AsrsException
      {
            if (macs.containsKey(mac))
            {
                  return macs.get(mac);
            }
            else
            {
                  throw new AsrsException(String.format(AsrsError.MACHINE_NOT_FOUND,mac));
            }
      }

      public void setStatus(String mac, String status)
      {
            macs.put(mac, status);
      }
}
