package com.asrs.thread;

/**
 * Created by Administrator on 2016/12/20.
 */
public class ThreadMain {
    public static void main(String[] args) {

        Thread thread1 = new Thread(new PutawayThread());
        thread1.setName("PutawayThread");
        thread1.start();

    }
}
