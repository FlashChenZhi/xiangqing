package com.asrs.business.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 14:09 2018/7/15
 * @Description:
 * @Modified By:
 */
public class RetrievalOrderStatus {
    public static final String WAITING = "0";
    public static final String ACCPET = "1";
    public static final String OVER = "2";
    //异常
    public static final String ABNORMAL = "5";
    public static final Map<String, String> map = new HashMap<String, String>();

    static {
        map.put(WAITING, "等待");
        map.put(ACCPET, "开始执行");
        map.put(OVER, "完成");
        map.put(ABNORMAL, "异常");
    }
}
