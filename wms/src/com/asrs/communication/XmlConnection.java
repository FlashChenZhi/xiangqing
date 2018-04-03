package com.asrs.communication;


import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.util.common.LogWriter;
import com.util.common.LoggerType;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.rmi.RemoteException;

import static java.lang.Thread.sleep;

/**
 * Created with IntelliJ IDEA.
 * User: Nan
 * Date: 13-6-17
 * Time: 下午2:36
 * CMC和WASS的连接类
 */
public class XmlConnection {
    private XmlCenter _proxy;
    private static long SLEEP_FOR_RESPONSE = 500;
    protected String _connectionName;
    protected String _ip;
    protected int _port;
    protected Socket _socket;
    protected DataInputStream _reader;
    protected DataOutputStream _writer;
    public int RECEIVED_BUFFER_SIZE = 10000;
    public int SEND_BUFFER_SIZE = 10000;
    Thread _threadSend;
    Thread _threadRcv;
    protected boolean flagrun = true;

    private boolean waitForResponseFlag = false;
    private long waitForResponseStartTime;

    public static long RESPONSE_TIME_OUT = 5 * 1000; //5秒

    public XmlConnection(String connName, String ip, int port) {
        this._connectionName = connName;
        this._ip = ip;
        this._port = port;
        _proxy = XmlCenter.instance();
    }

    public void connect() throws CommunicationException {
        if (!isConnected()) {
            SocketAddress address = new InetSocketAddress(_ip, _port);
            _socket = new Socket();
            try {
                _socket.setReceiveBufferSize(RECEIVED_BUFFER_SIZE);
                _socket.setSendBufferSize(SEND_BUFFER_SIZE);
                _socket.connect(address);
                _reader = new DataInputStream(new BufferedInputStream(_socket.getInputStream()));
                _writer = new DataOutputStream(_socket.getOutputStream());
            } catch (IOException e) {
                String errMsg = "连接" + _connectionName + "时发生异常 |" + e.getMessage();
                System.out.println("Connection Error! address:" + address.toString());
                throw new CommunicationException(errMsg);
            }

//            establishConnection();
            System.out.println("[Info]XML Sender和Wass建立连接成功");

            _threadSend = new Thread() {
                public void run() {
                    sendHandler();
                }
            };
            _threadSend.start();

            _threadRcv = new Thread(){
                public void run(){
                    receiveHandler();
                }
            };
            _threadRcv.start();
        }
    }

    public void receiveHandler() {
        System.out.println("Receive Handler is running");
        if (_reader != null) {
            byte rcvByte;
            StringBuilder dataStr = new StringBuilder();

            while (isConnected()) {
                try {
                    rcvByte = _reader.readByte();
                    //lastRecvTime = System.currentTimeMillis();
                    if (rcvByte == 2) {
                        dataStr = new StringBuilder();
                    } else if (rcvByte == 3) {
                        if (dataStr.length() > 0) {
                            String msg = dataStr.toString();
                            _proxy.addRcvdXML(dataStr.toString());
                            if (msg.length() <= 7) {
                                System.out.println("XMLSend接收:" + dataStr.toString());
                            } else {
                                System.out.println("XMLRecv接收到XML");
                            }
                        }
                        dataStr = new StringBuilder();
                    } else {
                        dataStr.append((char) rcvByte);
                    }
                } catch (EOFException e) {
                    try {
                        sleep(SLEEP_FOR_RESPONSE);
                        //System.out.println("XMLSend EOFException");
                    } catch (InterruptedException interrupt) {
                        interrupt.printStackTrace();
                        LogWriter.error(LoggerType.XMLMessageInfo, "XMLRecvConnection receiveHandler被中断");
                        break;
                    }
                    continue;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    LogWriter.error(LoggerType.XMLMessageInfo, ex.getMessage());
                    try {
                        disconnect();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }
            }
        }
        LogWriter.error(LoggerType.XMLMessageInfo, "XMLRecvConnection receiveHandler终止");
    }

//    private void establishConnection() throws CommunicationException {
//        String msg = XMLConstant.PROTOCOL_STX + XMLConstant.PROTOCOL_SOH + XMLConstant.PROTOCOL_ETX;
//        send(msg);
//
//        String response = getResponse();
//        if (XMLConstant.PROTOCOL_SYN.equals(response)) {
//            System.out.println("[Info]XML Sender和Wass建立连接成功");
//            return;
//        } else {
//            String errMsg = "[Error]XML Sender无法和Wass建立连接";
//            System.out.println(errMsg);
//            throw new CommunicationException(errMsg);
//        }
//    }

    public void sendHandler() {
        System.out.println("Send Handler is running");
        if (_writer != null) {
            while (flagrun & isConnected()) {
                try {
                    XmlProxy proxy = XmlCenter.instance();
                    String msg = XMLConstant.PROTOCOL_STX + proxy.getSendXML() + XMLConstant.PROTOCOL_ETX;
                    send(msg);

                    if (msg.length() <= 7) {
                        System.out.println("XMLSend发送Message:" + msg);
                    } else {
                        System.out.println("XMLSend发送XML");
                    }

//                    String response = getResponse();
//                    proxy.addRcvdXML(response);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LogWriter.error(LoggerType.XMLMessageInfo, "XMLSend SendHandler被中断");
                    break;
                } catch (CommunicationException e) {
                    e.printStackTrace();
                    LogWriter.error(LoggerType.XMLMessageInfo, e.getMessage());
                    try {
                        disconnect();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    LogWriter.error(LoggerType.XMLMessageInfo, e.getMessage());
                    try {
                        disconnect();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

//    private String getResponse() throws CommunicationException {
//        waitForResponseFlag = true;
//        waitForResponseStartTime = System.currentTimeMillis();
//
//        byte rcvByte;
//        StringBuilder dataStr = new StringBuilder();
//        while (flagrun && isConnected()) {
//            try {
//                rcvByte = _reader.readByte();
//                if (rcvByte == 2) {
//                    dataStr = new StringBuilder();
//                } else if (rcvByte == 3) {
//                    if (dataStr.length() > 0) {
//                        System.out.println("XMLSend接收:" + dataStr.toString());
//                        waitForResponseFlag = false;
//                        return dataStr.toString();
//                    } else {
//                        throw new CommunicationException("应答为空");
//                    }
//                } else {
//                    dataStr.append((char) rcvByte);
//                }
//            } catch (IOException e) {
//                throw new CommunicationException(e.getMessage());
//            }
//        }
//        throw new CommunicationException("未连接，无法获得应答");
//    }

    public boolean isConnected() {
        return _socket != null
                && _socket.isConnected()
                && !_socket.isClosed();
    }

    public void disconnect() throws IOException {
        if (isConnected()) {
            _socket.close();
            _socket = null;
            _reader = null;
            _writer = null;
            System.out.println(_connectionName + " disconnected!");
            _threadSend.interrupt();
        }
        LogWriter.error(LoggerType.XMLMessageInfo, "XMLSend 链接中断");
    }

    public void send(String str) throws CommunicationException {
        send(str.getBytes());
    }

    public void send(byte[] bytes) throws CommunicationException {
        if (isConnected()) {
            if (_writer != null) {
                try {
                    _writer.write(bytes);
                } catch (IOException e) {
                    String errMsg = String.format("发送数据给%1$s时发生错误.\n" +
                                    "%2$s\n" +
                                    e.getMessage(),
                            _connectionName, new String(bytes));
                    LogWriter.error(LoggerType.XMLMessageInfo, errMsg);
                    throw new CommunicationException(errMsg);
                }
            } else {
                throw new CommunicationException("OutStream为Null,无法发送! " + new String(bytes));
            }
        } else {
            throw new CommunicationException("未连接,无法发送! " + new String(bytes));
        }
    }

    public String getConnectionName() {
        return _connectionName;
    }

    public boolean isResponseTimeOut() {
        if (waitForResponseFlag
                && System.currentTimeMillis() - RESPONSE_TIME_OUT > waitForResponseStartTime) {
            return true;
        } else {
            return false;
        }
    }
}


