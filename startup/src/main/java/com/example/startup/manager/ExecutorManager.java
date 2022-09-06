package com.example.startup.manager;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorManager {

    public static ThreadPoolExecutor cpuExecutor;
    public static ExecutorService ioExecutor;
    public static Executor mainExecutor;

    //获得CPU核心数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 5));
    private static final int MAX_POOL_SIZE = CORE_POOL_SIZE;

    static {
        long KEEP_ALIVE_TIME = 5L;
        cpuExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(),
                Executors.defaultThreadFactory());
        cpuExecutor.allowCoreThreadTimeOut(true);

        ioExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

        mainExecutor = new Executor() {
            final Handler handler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }
}
