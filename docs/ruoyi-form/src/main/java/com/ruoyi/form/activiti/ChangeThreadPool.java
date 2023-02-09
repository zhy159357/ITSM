package com.ruoyi.form.activiti;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ChangeThreadPool {
    public final Map<String, ThreadPoolExecutor> THREAD_POOL_EXECUTOR_MAP = new HashMap<>();

    public void submit(String key, Runnable runTask) {
        ThreadPoolExecutor threadPoolExecutor = THREAD_POOL_EXECUTOR_MAP.get(key);
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS
                    , new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.DiscardPolicy());
            THREAD_POOL_EXECUTOR_MAP.put(key, threadPoolExecutor);
        }
        threadPoolExecutor.submit(runTask);
    }

    public void shutDown(String key) {
        ThreadPoolExecutor threadPoolExecutor = THREAD_POOL_EXECUTOR_MAP.get(key);
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
            THREAD_POOL_EXECUTOR_MAP.remove(key);
        }
    }
}
