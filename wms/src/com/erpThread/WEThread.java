package com.erpThread;

import com.asrs.thread.PutawayThread;
import com.asrs.thread.RetrievalThread;

/**
 * @Author: ed_chen
 * @Date: Create in 22:45 2018/7/26
 * @Description:
 * @Modified By:
 */
public class WEThread {

    public static void main(String[] args) {
        Thread truckThread = new Thread(new TruckThread());
        truckThread.setName("TruckThread");
        truckThread.start();

        Thread WESkuThread = new Thread(new WESkuThread());
        WESkuThread.setName("WESkuThread");
        WESkuThread.start();

        Thread WEInOrOutStockOverThread = new Thread(new WEInOrOutStockOverThread());
        WEInOrOutStockOverThread.setName("WEInOrOutStockOverThread");
        WEInOrOutStockOverThread.start();

        Thread WEInStockThread = new Thread(new WEInStockThread());
        WEInStockThread.setName("WEInStockThread");
        WEInStockThread.start();

        Thread WEOutStockThread = new Thread(new WEOutStockThread());
        WEOutStockThread.setName("WEOutStockThread");
        WEOutStockThread.start();


    }
}
