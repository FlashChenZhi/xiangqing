package com.asrs;


import com.asrs.business.msgProc.WcsMsgProcCenter;
import com.asrs.business.xmlProc.WmsMsgProcCenter;
import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.util.common.Const;
import com.util.common.LogWriter;

import java.rmi.Naming;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-11
 * Time: 12:44:01
 * Copyright Dsl.Worgsoft.
 */
public class AsrsJobCenter {
    public static XmlProxy _wmsproxy;
    public static MessageProxy _wcsproxy;

    public static void main(String[] args) {
//        System.setSecurityManager(new RMISecurityManager());
        try {
            _wmsproxy = (XmlProxy) Naming.lookup(Const.WMSPROXY);
            _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
        } catch (Exception e) {
            LogWriter.writeError(AsrsJobCenter.class, "初始化MessageProxy时发生错误");
            e.printStackTrace();
            System.exit(-1);
        }

        Thread thread1 = new Thread(new WmsMsgProcCenter(_wmsproxy));
        thread1.setName("WmsMsgProcCenter");
        thread1.start();

        Thread thread2 = new Thread(new WcsMsgProcCenter(_wmsproxy, _wcsproxy));
        thread2.setName("WcsMsgProcCenter");
        thread2.start();

    }

}
