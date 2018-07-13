package com.util.common;

/**
 * Created by IntelliJ IDEA.
 * User: lenovo
 * Date: 2010-11-10
 * Time: 10:24:51
 * To change this template use File | Settings | File Templates.
 */
public class Const {
    public final static String POPUP_NAME = "POPUP_NAME";

    public static final int COUNT_PER_PAGE = 30;

    public static final int bankCount = 50;
    public static final int bayCount = 27;
    public static final int containerQty = 1;//每个托盘所载货物数量
    public static final String containerCode = "XQSpringWater";//每个托盘的托盘号
    public static final String skuCode = "SpringWater";//商品代码
    public static final String skuName = "乡情矿泉水";//商品名称
    public static final String warehouseCode = "ck";//仓库代码

    public final static String OP_LOG_NAME = "opLog";
    public static final Integer LOWER_POWER = 40;
    public static final Integer HIGH_POWER = 95;
    //收货暂存区
    public static final String RECV_TEMP_LOCATION = "TEMP001";

    public static final String WHID = "WH113";

    public static final String loginUser = "userName";

//    public static final String WMSPROXY = "rmi://127.0.0.1:1089/XmlProxy";
    public static final String WMSPROXY = "rmi://localhost/XmlProxy";

    public static final String OPPLE_IN_WMS_URL = "http://10.10.0.63/inbound/wap/servlet ";
    public static final String OPPLE_OUT_WMS_URL = "http://10.10.0.63/outbound/wap/servlet";
    public static final String OPPLE_OUT_CLOSE_WMS_URL = "http://10.10.0.63/outbound/close/wap/servlet";
    public static final String OPPLE_TRANSFER_WMS_URL = "http://10.10.0.63/transfer/pallet/wap/servlet";


    public static final String EMPTY_PALLET = "EMPT";
}
