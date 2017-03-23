package com.cssweb.business;

import com.cssweb.msg.CustomMessage;

import java.util.concurrent.BlockingQueue;

/**
 * Created by chenhf on 14-2-21.
 */
public class BusinessThread implements Runnable {
    private BlockingQueue<CustomMessage> queue;
    private boolean isRunning;

    public BusinessThread(BlockingQueue<CustomMessage> queue) {
        this.queue = queue;
        isRunning = true;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                // 循环从消息队列中获取消息

                CustomMessage msg = queue.take();
                if (msg != null) {

                    // create response message
                    // send response message
                    // msg.getChannelHandlerContext().writeAndFlush();
                }

            } // end while
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        isRunning = false;
    }
}
