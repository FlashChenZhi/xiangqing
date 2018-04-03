package com.asrs.business.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Zhouyue
 * Date: 2008-10-16
 * Time: 14:19:11
 * Copyright Daifuku Shanghai Ltd.
 */
public class AsrsJobStatus
{
    public static final String SUSPEND = "0";
    public static final String RUNNING = "1";
    public static final String ACCEPT = "2";
    public static final String PICKING = "3";
    public static final String CANCEL = "6";
    public static final String DONE = "8";
    public static final String ABNORMAL = "9";

    public static final Map<String, String> map = new HashMap<String, String>();

    static {
        map.put(RUNNING, "运行");
        map.put(CANCEL, "取消");
        map.put(ACCEPT,"接受任务");
        map.put(PICKING,"拣选完成");
        map.put(DONE, "完成");
        map.put(ABNORMAL, "异常");
    }

}
