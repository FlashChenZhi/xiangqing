package com.asrs.business.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Zhouyue
 * Date: 2008-10-20
 * Time: 11:32:51
 * Copyright Daifuku Shanghai Ltd.
 */
public class StationMode {

    public static final String PUTAWAY = "01";//入库
    public static final String RETRIEVAL = "03";//出库
    public static final String UNKNOWN = "09";//NG
    public static final Map MAP = new HashMap();
    public static final Map<String, String> stationMap = new HashMap<String, String>();
    static {
        MAP.put(StationMode.PUTAWAY, "入库");
        MAP.put(StationMode.RETRIEVAL, "出库");
        MAP.put(StationMode.UNKNOWN, "NG");
        stationMap.put("1101", "1302");
        stationMap.put("1102", "1301");
    }
}
