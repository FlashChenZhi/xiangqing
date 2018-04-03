package com.asrs.business.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Zhouyue
 * Date: 2008-10-16
 * Time: 14:52:54
 * Copyright Daifuku Shanghai Ltd.
 */
public class AsrsJobType {
    public static final String PUTAWAY = "01";//入库
    public static final String ST2ST = "02";//直行
    public static final String RETRIEVAL = "03";//出库
    public static final String PICKING_RETRIEVAL = "04";//拣选出库
    public static final String ADD_RETRIEVAL = "05";//补充出库
    public static final String BACK_PUTAWAY = "06";//回库
    public static final String RECHARGED = "07";//充电
    public static final String RECHARGEDOVER = "08";//充电结束
    public static final String CHANGELEVEL = "09";//换层
    public static final String COUNT = "10";//盘库回库
    public static final String LOCATIONTOLOCATION = "11";//库内移动

//    public static final String TRANSFER = "07";//库内移动

    public static final Map<String, String> map = new HashMap<String, String>();

    static {
        map.put(PUTAWAY, "入库");
        map.put(ST2ST, "库内移动");
        map.put(RETRIEVAL, "出库");
        map.put(RECHARGED, "充电");
        map.put(RECHARGEDOVER, "充电完成");
        map.put(CHANGELEVEL, "换层");
        map.put(COUNT, "盘库回库");
//        map.put(TRANSFER,"库内移动");
    }

}
