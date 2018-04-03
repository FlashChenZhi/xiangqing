package com.asrs.communication;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-4
 * Time: 8:56:17
 * Copy right Worgsoft.
 */
public class Receiver implements Runnable
{
    PlcConnection conn;

    public Receiver(PlcConnection conn)
    {
        this.conn = conn;
    }

    public void run()
    {
        conn.receiveHandler();       
    }
}
