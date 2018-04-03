package com.domain.consts.xmlmessage;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-25
 * Time: 下午10:05
 * To change this template use File | Settings | File Templates.
 */
public class XMLMessageStatus {
    //刚创建未送信
    public static int CREATED = 0;
    //已经送信给WASS
    public static int SENT = 1;
    //收到WASS的发信成功
    public static int ACCEPT = 2;
    //已经收到WMS的回答,只有LoadUnitAtID是有回答的
    public static int ANSWERED = 3;
    public static final int TIME_OUT = 4;
}
