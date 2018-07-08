package com.asrs.communication;

import com.asrs.domain.MessageLog;
import com.asrs.domain.Plc;
import com.asrs.message.*;
import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.time.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-3
 * Copyright Dsl.Worgsoft.
 * Time: 16:53:09
 */
public class PlcConnection
{
      private int RECEIVED_BUFFER_SIZE = 10000;
      private int SEND_BUFFER_SIZE = 10000;


      public String getPlcName()
      {
            return _plcName;
      }

      private String _plcName;
      private String _ip;
      private int _port;
      private Socket _socket;
      DataInputStream _reader;
      DataOutputStream _writer;
      private boolean flagrun = true;
      SeqGenerator seq = new SeqGenerator();
      Thread _threadSend;
      Thread _threadReceiver;


      public PlcConnection(String plcName, String ip, int port)
      {
            this._plcName = plcName;
            this._ip = ip;
            this._port = port;
      }

      public void connect() throws CommunicationException
      {
            if (!IsConnected())
            {
                  seq.reset();
                  SocketAddress address = new InetSocketAddress(_ip, _port);
                  _socket = new Socket();

                  try
                  {
                        _socket.setReceiveBufferSize(RECEIVED_BUFFER_SIZE);
                        _socket.setSendBufferSize(SEND_BUFFER_SIZE);
                        //_socket.connect(address);
                        _socket.connect(address,2000);
                        _reader = new DataInputStream(new BufferedInputStream(_socket.getInputStream()));
                        _writer = new DataOutputStream(_socket.getOutputStream());
                  }
                  catch (IOException e)
                  {
                        String errMsg = "连接" + _plcName + "时发生异常 |" + e.getMessage();
                        throw new CommunicationException(errMsg);
                  }


                  _threadReceiver = new Thread(new Receiver(this), "ReceiverThread");
                  _threadReceiver.start();

                  _threadSend = new Thread(new Sender(this), "SenderThread");
                  _threadSend.start();
            }
      }

      public void disconnect() throws CommunicationException {
            if (IsConnected())
            {
                try {
                    _socket.close();
                    _socket = null;
                    _reader = null;
                    _writer = null;
                      try {
                            Transaction.begin();
                            //PLC连接失败
                            System.out.println(getPlcName()+"disconnect连接失败");
                            Plc plc = Plc.getPlcByPlcName(getPlcName());
                            plc.setStatus("2");
                            Transaction.commit();
                      } catch (Exception e1) {
                            Transaction.rollback();
                            e1.printStackTrace();
                      }
                }catch (IOException e)
                {
                      _socket = null;
                      _reader = null;
                      _writer = null;
                      try {
                            Transaction.begin();
                            //PLC连接失败
                            System.out.println(getPlcName()+"disconnect连接失败");
                            Plc plc = Plc.getPlcByPlcName(getPlcName());
                            plc.setStatus("2");
                            Transaction.commit();
                      } catch (Exception e1) {
                            Transaction.rollback();
                            e1.printStackTrace();
                      }
                    String errMsg = "断开" + _plcName + "时发生异常 |" + e.getMessage();
                    throw new CommunicationException(errMsg);
                }
                  System.out.println(_plcName + " disconnected!");
                  _threadSend.interrupt();
                  _threadReceiver.interrupt();
            }
      }

      public boolean IsConnected()
      {
            return _socket != null
                    && _socket.isConnected()
                    && !_socket.isClosed();
      }

      public void send(byte[] bytes) throws CommunicationException
      {
            if (IsConnected())
            {
                  if (_writer != null)
                  {
                        try
                        {
                              _writer.write(bytes);
                        }
                        catch (IOException e)
                        {
                              String errMsg = String.format("发送数据给%1$s时发生错误.\n" +
                                      "%2$s\n" +
                                      e.getMessage(),
                                      _plcName,new String(bytes));
                              LogWriter.writeError(this.getClass(),errMsg);
                              throw new CommunicationException(errMsg);
                        }
                  }
                  else
                  {
                        throw new CommunicationException("OutStream为Null,无法发送! " + new String(bytes));
                  }
            }
            else
            {
                  throw new CommunicationException("未连接,无法发送! " + new String(bytes));
            }
      }

      public void send(String str) throws  CommunicationException
      {
            send(str.getBytes());
      }

      public void send(Message msg) throws CommunicationException, InterruptedException {
            MessageBuilder mb = new MessageBuilder();
            mb.ID = msg.getID();
            mb.IdClassification = msg.IdClassification;
            mb.McTime = DateFormatUtils.format(new Date(), "HHmmss");
            mb.SeqNo = seq.getSeqNoToBeTransmitted();
            mb.DataString = msg.toString();
            String bcc = BccGenerator.GetBcc(mb.toString());
            String msgStr = "2" + mb.toString() + bcc + "3";
            byte[] bytes = msgStr.getBytes();
            bytes[0] = 0x02;
            bytes[bytes.length - 1] = 0x03;
            send(bytes);
            if(!mb.ID.equals("06") && !mb.ID.equals("10")) {
                  LogWriter.writeInfo("ComLog", String.format("[S] [%1$s] [%2$s]", _plcName, msgStr.substring(1, msgStr.length() - 1)));
            }
            try{
                Transaction.begin();
                MessageLog log = new MessageLog();
                log.setType(MessageLog.TYPE_SEND);
                log.setMsg(msgStr.substring(1, msgStr.length() - 1));
                log.setCreateDate(new Date());
                HibernateUtil.getCurrentSession().save(log);
                Transaction.commit();

            }catch (Exception e){
                Transaction.rollback();
                e.printStackTrace();
            }
      }

      public void receiveHandler()
      {
            if (_reader != null)
            {
                  byte rcvByte;
                  StringBuilder dataStr = new StringBuilder();
                  while (flagrun && IsConnected())
                  {
                        try
                        {
                              rcvByte = _reader.readByte();
                              if (rcvByte == 2)
                              {
                                    dataStr = new StringBuilder();
                              }
                              else if (rcvByte == 3)
                              {
                                    DoDataStr(dataStr.toString());
                                    dataStr = new StringBuilder();
                              }
                              else
                              {
                                    if (dataStr.length() < 1024)
                                    {
                                          dataStr.append((char) rcvByte);
                                    }
                              }
                        }
                        catch (IOException ex)
                        {
                              ex.printStackTrace();
                              LogWriter.writeError(this.getClass(), ex.getMessage());

                              try
                              {
                                    disconnect();
                              }
                              catch (CommunicationException e)
                              {
                                    e.printStackTrace();
                              }
                              break;
                        }
                  }
            }
      }

      public void sendHandler()
      {
            if (_writer != null)
            {
                  while (flagrun && IsConnected())
                  {
                        try
                        {
                              Message msg = MessageCenter.instance().getSndMsg(_plcName);
                              send(msg);
                              Thread.sleep(35);
                        }
                        catch (InterruptedException e)
                        {
                              e.printStackTrace();
                              LogWriter.writeError(this.getClass(), "SendHandler被中断");
                              break;
                        }
                        catch (CommunicationException e)
                        {
                              e.printStackTrace();
                              LogWriter.writeError(this.getClass(), e.getMessage());
                              try
                              {
                                    disconnect();
                              } catch (CommunicationException e1) {
                                  e1.printStackTrace();
                              }
                            break;
                        }
                  }
            }
      }

      private void DoDataStr(String dataStr)
      {
            if (dataStr.isEmpty())
            {
                  return;
            }


//          try{
//
//              Transaction.begin();
//              MessageLog log = new MessageLog();
//              log.setType(MessageLog.TYPE_RECV);
//              log.setMsg(dataStr);
//              log.setCreateDate(new Date());
//              HibernateUtil.getCurrentSession().save(log);
//              Transaction.commit();
//
//          }catch (Exception e){
//              e.printStackTrace();
//              Transaction.rollback();
//          }

            if (dataStr.length() >= 21)
            {
                  String content = dataStr.substring(0, dataStr.length() - 2);
                  String contentData = dataStr.substring(13, dataStr.length() - 2);
                  String bcc = dataStr.substring(dataStr.length() - 2, dataStr.length());
                  if (BccGenerator.IsBccRight(contentData, bcc))
                  {
                        try
                        {
                              MessageBuilder mb = MessageBuilder.Parse(content);
                              mb.PlcName = _plcName;
                              MessageCenter.instance().addRcvdMsg(mb);

                              if(!mb.ID.equals("26") && !mb.ID.equals("30")) {
                                    LogWriter.writeInfo("ComLog", String.format("[R] [%1$s] [%2$s]", _plcName, dataStr));
                              }
                        }
                        catch (MsgException e)
                        {
                              String errMsg = e.getMessage();
                              System.out.println(errMsg);
                              LogWriter.writeError(this.getClass(), errMsg);
                        }
                  }
            }
      }
}
