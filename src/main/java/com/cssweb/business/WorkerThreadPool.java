package com.cssweb.business;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkerThreadPool {
    private static final Logger logger = LogManager.getLogger(WorkerThreadPool.class.getName());

    private static WorkerThreadPool instance = null;
    private static Object mutex = new Object();

    private ExecutorService executor;

    private WorkerThreadPool() {

    }

    public static WorkerThreadPool getInstance() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null)
                    instance = new WorkerThreadPool();
            }
        }
        return instance;
    }

    public void init(int threadPoolSize) {
        executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void put(Runnable task) {
        if (executor == null) {
            logger.info("executor is null");
            return;
        }

        executor.execute(task);
    }
}
