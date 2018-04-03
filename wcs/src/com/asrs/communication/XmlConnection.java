package com.asrs.communication;


import com.domain.consts.xmlbean.XMLConstant;
import com.util.common.LogWriter;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-9-17
 * Time: 上午10:49
 * To change this template use File | Settings | File Templates.
 */
public class XmlConnection {

    private Socket _socket;
    private XmlCenter _proxy;

    public int RECEIVED_BUFFER_SIZE = 10000;
    public int SEND_BUFFER_SIZE = 10000;
    protected DataInputStream _reader;
    protected DataOutputStream _writer;

//    private long lastRecvTime = System.currentTimeMillis();
//    private static long TIME_OUT = 60*60*1000;

    private static long SLEEP_FOR_EOF = 500;
    private Thread _threadRecv;
    private Thread _threadSend;

    public XmlConnection(Socket socket) {
        _socket = socket;
        _proxy = XmlCenter.instance();
        try {
            _socket.setReceiveBufferSize(RECEIVED_BUFFER_SIZE);
            _socket.setSendBufferSize(SEND_BUFFER_SIZE);

            _reader = new DataInputStream(new BufferedInputStream(_socket.getInputStream()));
            _writer = new DataOutputStream(_socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveHandler(){
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

                    continue;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    LogWriter.writeError(this.getClass(), ex.getMessage());
                    try {
                        disconnect();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }
            }
        }        LogWriter.writeError(this.getClass(), "XMLRecvConnection receiveHandler终止");
    }

    public void sendHandler(){
        if (_writer != null){
            while (isConnected()){
                try {
//                    String msg = _proxy.getSendXML();
                    String msg = XMLConstant.PROTOCOL_STX + _proxy.getSendXML() + XMLConstant.PROTOCOL_ETX;
                    LogWriter.writeXmlInfo("xmlLog",msg);
                    if(msg.length() <= 7){
                        System.out.println("XMLRecv发送Message:" + msg);
                    } else {
                        System.out.println("XMLRecv发送XML");

                        send(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LogWriter.writeError(this.getClass(), "XMLRecvConnection SendHandler被中断");
                    break;
                } catch (CommunicationException e) {
                    e.printStackTrace();
                    LogWriter.writeError(this.getClass(), e.getMessage());
                    try
                    {
                        disconnect();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                    break;
                }
            }
            LogWriter.writeError(this.getClass(), "XMLRecvConnection SendHandler终止");
        }
    }

    public void send(String str) throws CommunicationException
    {
        send(str.getBytes());
    }

    public void send(byte[] bytes) throws CommunicationException {
        if (_writer != null) {
            try {
                _writer.write(bytes);
            } catch (IOException e) {
                String errMsg = String.format("发送数据给%1$s时发生错误.\n" +
                        "%2$s\n" +
                        e.getMessage(),
                        "WassReceiver", new String(bytes));
                LogWriter.writeError(this.getClass(), errMsg);
                throw new CommunicationException(errMsg);
            }
        } else {
            throw new CommunicationException("OutStream为Null,无法发送! " + new String(bytes));
        }
    }


    public void connect(){
        _threadRecv = new Thread() {
            public void run() {
                receiveHandler();
            }
        };
        _threadRecv.start();

        _threadSend = new Thread() {
            public void run() {
                sendHandler();
            }
        };
        _threadSend.start();
    }

    public void disconnect() throws IOException
    {
        if (isConnected())
        {
            _socket.close();
            _socket = null;
            _reader = null;
            _writer = null;
            System.out.println(this.getClass().getName() + " disconnected!");
            _threadSend.interrupt();
        }
    }

    public boolean isConnected()
    {
        return _socket != null
                && _socket.isConnected()
                && !_socket.isClosed();
    }
}
