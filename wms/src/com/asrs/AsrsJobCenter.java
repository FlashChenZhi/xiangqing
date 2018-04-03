package com.asrs;


import com.asrs.business.xmlProc.WmsMsgProcCenter;
import com.asrs.communication.XmlProxy;
import com.util.common.Const;
import com.util.common.LogWriter;
import com.util.common.LoggerType;

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

    public static void main(String[] args) {
        try {
            _wmsproxy = (XmlProxy) Naming.lookup(Const.WMSPROXY);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Thread thread1 = new Thread(new WmsMsgProcCenter(_wmsproxy));
        thread1.setName("WmsMsgProcCenter");
        thread1.start();
    }
}
