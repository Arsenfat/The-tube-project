package com.tubeproject.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CentralPooling {
    private static CentralPooling pool;
    private ExecutorService threadPool;

    private CentralPooling() {
        this.threadPool = new ThreadPoolExecutor(1, 2, 300, TimeUnit.SECONDS, new ArrayBlockingQueue<>(15));
    }

    public static void execute(Runnable runnable) {
        if (pool == null)
            pool = new CentralPooling();
        pool.threadPool.execute(runnable);
    }

    public static ExecutorService getExecutor() {
        if (pool == null)
            pool = new CentralPooling();
        return pool.threadPool;
    }
}
