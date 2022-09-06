package com.example.startup.run;

import android.content.Context;
import android.os.Process;

import com.example.startup.Startup;
import com.example.startup.manager.StartupCacheManager;
import com.example.startup.manager.StartupManager;
import com.example.startup.store.Result;

/**
 * com.example.startup.run.StartupRunnable
 *
 * @author liulongchao
 * @since 2022/9/5
 */
public class StartupRunnable implements Runnable {

    /**
     * StartupManager
     */
    private final StartupManager mStartupManager;
    /**
     * Startup
     */
    private final Startup<?> mStartup;
    /**
     * Context
     */
    private final Context mContext;

    /**
     * 构造任务启动器
     * @param mStartupManager mStartupManager
     * @param mStartup mStartup
     * @param mContext mContext
     */
    public StartupRunnable(StartupManager mStartupManager, Startup<?> mStartup, Context mContext) {
        this.mStartupManager = mStartupManager;
        this.mStartup = mStartup;
        this.mContext = mContext;
    }

    /**
     * 执行任务
     */
    @Override
    public void run() {
        Process.setThreadPriority(mStartup.getThreadPriority());
        mStartup.toWait();
        Object result = mStartup.create(mContext);
        StartupCacheManager.getInstance().saveInitializedComponent(mStartup.getClass(), new Result(result));
        mStartupManager.notifyChildren(mStartup);
    }
}
