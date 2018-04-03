package com.asrs.business.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Zhouyue
 * Date: 2008-10-16
 * Time: 14:19:11
 * Copyright Daifuku Shanghai Ltd.
 */
public class AsrsJobStatus {
    public static final String WAITING = "0";
    public static final String ARRIVAL = "1";
    public static final String RUNNING = "2";
    public static final String CANCEL = "3";
    public static final String DONE = "4";
    //异常
    public static final String ABNORMAL = "5";
    public static final Map<String, String> map = new HashMap<String, String>();

    static {
        map.put(WAITING, "等待");
        map.put(ARRIVAL, "到达");
        map.put(RUNNING, "运行");
        map.put(CANCEL, "取消");
        map.put(DONE, "完成");
        map.put(ABNORMAL, "异常");
    }
}
