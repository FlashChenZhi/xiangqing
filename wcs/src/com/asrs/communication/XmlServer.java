package com.asrs.communication;

import com.asrs.domain.Wcs;
import com.util.common.LogWriter;
import com.util.hibernate.Transaction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-9-17
 * Time: 上午10:22
 * To change this template use File | Settings | File Templates.
 */
public class XmlServer implements Runnable {
    private int _port;
    public XmlServer() {
        Transaction.begin();
        Wcs w = Wcs.getWcs();
        Transaction.commit();

        if (w == null || w.getPort() == 0) {
            String errMsg = "WmsReceiver port not initialized.";
            LogWriter.writeError(this.getClass(), errMsg);
            return;
        }

        _port = w.getPort();
    }

    public XmlServer(int port){
        _port = port;
    }

    @Override
    public void run() {
        final ServerSocket server;

        try {
            server = new ServerSocket(_port);
            System.out.println("server start okay!");
        } catch (IOException e) {
            e.printStackTrace();
            String errMsg = "socket接收服务器无法启动.";
            LogWriter.writeError(this.getClass(), errMsg);
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Socket socket;
        XmlConnection conn = null;
        while (true) {
            try {
                socket = server.accept();
            } catch (IOException e) {
                String errMsg = "获得用户请求时发生错误";
                LogWriter.writeError(this.getClass(), errMsg);
                return;
            }

            System.out.println("Client连接到WcsRecvServer");
            if(conn != null){
                try {
                    conn.disconnect();
                    System.out.println("Client重新连接，关闭旧连接");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            conn = new XmlConnection(socket);
            conn.connect();
        }
    }
}
