package com.cssweb.business;

import com.cssweb.msg.CustomMessage;

import java.util.concurrent.*;

/**
 * Created by chenhf on 14-2-21.
 */
public class Business {

    private BlockingQueue<CustomMessage> queue = new LinkedBlockingDeque<CustomMessage>();

    private ExecutorService threadpool;
    private int threadpoolSize;


    public Business() {
        threadpoolSize = 10;
    }

    public BlockingQueue<CustomMessage> getQueue() {
        return queue;
    }

    public void start() {
        // create queue


        // create worker thread pool
        threadpool = Executors.newFixedThreadPool(threadpoolSize);

        for (int i = 0; i < threadpoolSize; i++) {
            BusinessThread businessThread = new BusinessThread(queue);
            threadpool.execute(businessThread);
        }
    }

    public void stop() {

    }

}
