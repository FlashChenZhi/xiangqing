package com.asrs.communication;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-4
 * Time: 8:56:08
 * Copy right Worgsoft.
 */
public class Sender implements Runnable
{
    PlcConnection conn;
    public Sender(PlcConnection conn)
    {
        this.conn = conn;
    }

    public void run()
    {
        conn.sendHandler();
    }
}
