package com.asrs.thread;

/**
 * Created by Administrator on 2016/12/20.
 */
public class ThreadMain {
    public static void main(String[] args) {
        Thread thread2 = new Thread(new RetrievalThread());
        thread2.setName("RetrievalThread");
        thread2.start();

        Thread thread1 = new Thread(new PutawayThread());
        thread1.setName("PutawayThread");
        thread1.start();

    }
}
