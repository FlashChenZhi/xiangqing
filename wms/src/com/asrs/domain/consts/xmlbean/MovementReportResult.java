package com.asrs.domain.consts.xmlbean;

/**
 * Created with IntelliJ IDEA.
 * User: 张俊
 * Date: 13-7-29
 * Time: 上午11:21
 * To change this template use File | Settings | File Templates.
 */
public class MovementReportResult {
    public static String OK = "00";
    //空出库
    public static String LOAD_NOT_EXIST = "01";
    //路径变更
    public static String DEST_BLOCKED = "02";
    //AGC端删除数据
    public static String DEL_BY_AGC = "03";
    //RM高度不一致
    public static String CARRIED_OUT_WITH_CHANGES = "04";
    //重复入库
    public static String DEST_OCCUPIED = "05";
    //WASS指示删除
    public static String TRANSPORT_DEL = "06";
    //WASS强制完成
    public static String TRANSPORT_DEL_NG = "07";

    public static String NO_ROUTE = "14";
}
