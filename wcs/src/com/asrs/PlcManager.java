package com.asrs;

import java.util.HashMap;

/**
 * Author: Zhouyue
 * Date: 2008-9-25
 * Time: 11:53:09
 * Copyright Daifuku Shanghai Ltd.
 */
public class PlcManager
{
      private static PlcManager instance = new PlcManager();

      private PlcManager()
      {

          plcs.put("plc01", true);

      }

      public static PlcManager Instance()
      {
            return instance;
      }

      HashMap<String, Boolean> plcs = new HashMap<String, Boolean>();

      public boolean IsOnline(String plcName)
      {
            return plcs.containsKey(plcName) && plcs.get(plcName);
      }

      public void SetOnline(String plcName, boolean isOnline)
      {            
            plcs.put(plcName, isOnline);
      }
}
