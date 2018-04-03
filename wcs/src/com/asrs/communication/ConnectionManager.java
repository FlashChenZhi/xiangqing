package com.asrs.communication;

import com.asrs.domain.Plc;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.Srm;
import com.util.common.*;
import com.util.hibernate.Transaction;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-3
 * Time: 16:41:59
 * Copy right Worgsoft.
 */

public class ConnectionManager implements Runnable {
    private int Interval = 5000;

    public boolean isAutoConnect() {
        return isAutoConnect;
    }

    public void setAutoConnect(boolean autoConnect) {
        isAutoConnect = autoConnect;
    }

    private boolean isAutoConnect = true;

    HashMap<String, PlcConnection> _connectionPool = new HashMap<String, PlcConnection>();
    private String plcName;

    public ConnectionManager() {
//        _connCheckThread.start();
    }

    public void Connect(String name, String ip, int port) {
        PlcConnection conn = null;
        if (_connectionPool.containsKey(name)) {
            conn = _connectionPool.get(name);
        } else {
            conn = new PlcConnection(name, ip, port);
            _connectionPool.put(name, conn);
        }
        plcName = name;
        if (!conn.IsConnected()) {
            try {
                conn.connect();
                Transaction.begin();
                Block block = Block.getByBlockNo(conn.getPlcName());
                if (block != null) {
                    if (block instanceof MCar) {
                        MCar mCar = (MCar) block;
                        mCar.setCheckLocation(false);
                    } else if (block instanceof Srm) {
                        Srm srm = (Srm) block;
                        srm.setCheckLocation(false);
                    }
                }

                //PLC连接成功
                Plc plc = Plc.getPlcByPlcName(name);
                plc.setStatus("1");
                Transaction.commit();
                System.out.println(conn.getPlcName() + " Connected!");
            } catch (CommunicationException e) {
                Transaction.rollback();
                String errMsg = "cannot connection" + conn.getPlcName();
                System.out.println(errMsg);
                LogWriter.writeError(this.getClass(), errMsg);
                try {
                    Transaction.begin();
                    //PLC连接成功
                    Plc plc = Plc.getPlcByPlcName(name);
                    plc.setStatus("2");
                    Transaction.commit();
                } catch (Exception e1) {
                    Transaction.rollback();
                    e1.printStackTrace();
                }

            }
        }

    }

    public void DisConnect(String name) throws CommunicationException {
        if (_connectionPool.containsKey(name)) {
            PlcConnection conn = _connectionPool.get(name);
            _connectionPool.remove(name);
            conn.disconnect();
        }
    }

    public void Send(String plcName, byte[] data) throws IOException, CommunicationException {
        if (_connectionPool.containsKey(plcName)) {
            PlcConnection conn = _connectionPool.get(plcName);
            conn.send(data);
        }
    }

    public void Send(String plcName, String data) throws IOException, CommunicationException {
        if (_connectionPool.containsKey(plcName)) {
            PlcConnection conn = _connectionPool.get(plcName);
            conn.send(data);
        }
    }


    public void run() {
        while (true) {
            try {
                Thread.sleep(Interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogWriter.writeError(this.getClass(), e.getMessage());
            }
            if (isAutoConnect) {
                for (PlcConnection conn : _connectionPool.values()) {
                    if (!conn.IsConnected()) {
                        try {
                            conn.connect();

                            Transaction.begin();
                            Block block = Block.getByBlockNo(conn.getPlcName());
                            if (block != null) {
                                if (block instanceof MCar) {
                                    MCar mCar = (MCar) block;
                                    mCar.setCheckLocation(false);
                                } else if (block instanceof Srm) {
                                    Srm srm = (Srm) block;
                                    srm.setCheckLocation(false);
                                }
                            }
                            //PLC连接成功
                            Plc plc = Plc.getPlcByPlcName(plcName);
                            plc.setStatus("1");

                            Transaction.commit();

                            System.out.println(conn.getPlcName() + " Connected!");
                        } catch (CommunicationException e) {
                            Transaction.rollback();
                            String errMsg = "cannot connection" + conn.getPlcName();
                            LogWriter.writeError(this.getClass(), errMsg);
                            try {
                                Transaction.begin();
                                //PLC连接成功
                                Plc plc = Plc.getPlcByPlcName(conn.getPlcName());
                                plc.setStatus("2");
                                Transaction.commit();
                            } catch (Exception e1) {
                                Transaction.rollback();
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
