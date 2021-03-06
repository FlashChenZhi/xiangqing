package com.asrs.business.consts;

/**
 * Author: Zhouyue
 * Date: 2008-10-16
 * Time: 14:24:46
 * Copyright Daifuku Shanghai Ltd.
 */
public class AsrsJobStatusDetail
{

      public static final String WAITING = "0";
      public static final String ARRIVAL = "1";//输送机送达
      public static final String INDICATED = "2";//提升机或堆垛机载货
      public static final String ACCEPTED = "3";//母车载货
      public static final String PICKUP = "4";//小车载货
      public static final String MESSAGE = "5";
      public static final String PICKING_DONE = "P";
      public static final String PICKING_ARRIVAL = "6";
      public static final String REFUSED = "7";
      public static final String DONE = "8";
      public static final String ABNORMAL = "9";
}
