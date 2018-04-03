package com.asrs.communication;


import com.util.common.LogWriter;
import com.util.common.LoggerType;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-3
 * Time: 16:41:59
 * Copy right Worgsoft.
 */

public class XmlConnectionManager implements Runnable {
    private int Interval = 15000;

    public boolean isAutoConnect() {
        return isAutoConnect;
    }

    public void setAutoConnect(boolean autoConnect) {
        isAutoConnect = autoConnect;
    }

    private boolean isAutoConnect = true;

    HashMap<String, XmlConnection> _connectionPool = new HashMap<String, XmlConnection>();

    public XmlConnectionManager() {
//        _connCheckThread.start();
    }

    public void connectXML(String connName, String ip, int port) {
        XmlConnection conn;
        if (_connectionPool.containsKey(connName)) {
            conn = _connectionPool.get(connName);
        } else {
            conn = new XmlConnection(connName, ip, port);
            _connectionPool.put(connName, conn);
        }
        if (!conn.isConnected()) {
            try {
                conn.connect();
                System.out.println(conn.getConnectionName() + " Connected!");
            } catch (CommunicationException e) {
                String errMsg = "无法连接" + conn.getConnectionName();
                System.out.println(errMsg);
                LogWriter.error(LoggerType.XMLMessageInfo, errMsg);
            }
        }
    }

    public void disconnect(String connName) throws IOException {
        if (_connectionPool.containsKey(connName)) {
            XmlConnection conn = _connectionPool.get(connName);
            _connectionPool.remove(connName);
            conn.disconnect();
        }
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(Interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogWriter.error(LoggerType.XMLMessageInfo, e.getMessage());
            }
            if (isAutoConnect) {
                for (XmlConnection conn : _connectionPool.values()) {
                    if (!conn.isConnected()) {
                        try {
                            conn.connect();
                            System.out.println(conn.getConnectionName() + " Connected!");
                        } catch (CommunicationException e) {
                            String errMsg = "无法连接" + conn.getConnectionName();
                            System.out.println(errMsg);
                            e.printStackTrace();
                            LogWriter.error(LoggerType.XMLMessageInfo, errMsg);
                        }
                    }

                    if(conn.isResponseTimeOut()){
                        try {
                            conn.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String errMsg = "应答超时，重新连接" + conn.getConnectionName();
                        LogWriter.error(LoggerType.XMLMessageInfo, errMsg);

                        try {
                            conn.connect();
                        } catch (CommunicationException e) {
                            errMsg = "无法连接" + conn.getConnectionName();
                            System.out.println(errMsg);
                            e.printStackTrace();
                            LogWriter.error(LoggerType.XMLMessageInfo, errMsg);
                        }
                    }
                }
            }
        }
    }
}
