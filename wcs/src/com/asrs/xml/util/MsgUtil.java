package com.asrs.xml.util;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-8-25
 * Time: 上午11:05
 * To change this template use File | Settings | File Templates.
 */
public class MsgUtil {
    public static String getLocFromXML(String bank, String bay, String level){
        String warehouseNo = "0";

        String agcBank = bank;
        if (bank.length() > 2){
            agcBank = bank.substring(bank.length()-2);
        }

        return warehouseNo + agcBank + bay + level;
    }

    public static String getBankFromLocNo(String locNo) {
        return "9" + locNo.substring(1, 3);
    }

    public static String getBayFromLocNo(String locNo) {
        return locNo.substring(3, 6);
    }

    public static String getLevelFromLocNo(String locNo) {
        return locNo.substring(6, 9);
    }

    public static void printMsg(String connectName, String msg){
        System.out.println(connectName + "XMLRecv发送Message:" + XMLUtil.convertString(msg));
    }
}
