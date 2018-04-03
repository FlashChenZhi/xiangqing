package com.domain.consts.xmlbean;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-27
 * Time: 上午10:30
 * To change this template use File | Settings | File Templates.
 */
public class XMLConstant {
    public static String COM_DIVISION = "WCS";
    public static String WMS_DIVISION = "WMS";
    public static String COM_CONFIRMATION = "0";
    public static String COM_VERSION = "0100";
    public static String COM_STANDALONE_FLAG = "W";

    /**
     * LoadUnitAtID
     */
    public static String LUAI_SCAN_FLAG = "1";
    public static String LUAI_AI = "00";
    public static String LUAI_ERROR_CODE = "00";
    public static String LUAI_INFORMATION = "00";
    public static String LUAI_STUNITTYPE = "1";

    /**
     * MovementReport
     * */
    //empty
    /**
     * HandlingUnitStatus
     * */
    //empty

    //element for transmission protocol
    public static String PROTOCOL_SOH = new String(new char[]{1});
    public static String PROTOCOL_STX = new String(new char[]{2});
    public static String PROTOCOL_ETX = new String(new char[]{3});
    public static String PROTOCOL_EOT = new String(new char[]{4});
    public static String PROTOCOL_SYN = new String(new char[]{22});
    public static String PROTOCOL_CAN = new String(new char[]{24});
    public static String PROTOCOL_PING = "PING";
}
