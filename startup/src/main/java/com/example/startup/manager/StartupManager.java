package com.example.startup.manager;

import android.content.Context;
import android.os.Looper;
import com.example.startup.Startup;
import com.example.startup.impl.AndroidStartup;
import com.example.startup.run.StartupRunnable;
import com.example.startup.sort.TopologySort;
import com.example.startup.store.StartupSortStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * com.example.startup.manager.StartupManager
 *
 * @author liulongchao
 * @since 2022/9/2
 */
public class StartupManager {

    /**
     * context
     */
    private final Context mContext;
    /**
     * 任务结合
     */
    private final List<AndroidStartup<?>> mStartupList;
    /**
     * 任务执行结果
     */
    private StartupSortStore mStartupSortStore;
    /**
     * CountDownLatch
     */
    private final CountDownLatch mCountDownLatch;

    /**
     * 构造
     * @param mContext mContext
     * @param mStartupList mStartupList
     */
    public StartupManager(Context mContext, List<AndroidStartup<?>> mStartupList, CountDownLatch countDownLatch) {
        this.mContext = mContext;
        this.mStartupList = mStartupList;
        this.mCountDownLatch = countDownLatch;
    }

    /**
     * 等待任务 await
     */
    public void await() {
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动任务，主线程调用
     * @return StartupManager
     */
    public StartupManager start() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("please start task on main thread");
        }
        // 执行拓扑排序，核心
        mStartupSortStore = TopologySort.sort(mStartupList);
        for (Startup<?> startup : mStartupSortStore.getResult()) {
            StartupRunnable startupRunnable = new StartupRunnable(this, startup, mContext);
            if (startup.callCreateOnMainThread()) {
                startupRunnable.run();
            } else {
                startup.executor().execute(startupRunnable);
            }
        }
        return this;
    }

    /**
     * 通知依赖任务 notify
     * @param mStartup mStartup
     */
    public void notifyChildren(Startup<?> mStartup) {
        if (!mStartup.callCreateOnMainThread() && mStartup.waitOnMainThread()) {
            mCountDownLatch.countDown();
        }
        if (mStartupSortStore.getStartupChildrenMap().containsKey(mStartup.getClass())) {
            List<Class<? extends Startup>> childStartupCls = mStartupSortStore.getStartupChildrenMap().get(mStartup.getClass());
            for (Class<? extends Startup> childStartupCl : childStartupCls) {
                Startup<?> startup = mStartupSortStore.getStartupMap().get(childStartupCl);
                startup.toNotify();
            }
        }
    }

    public static class Builder {
        private final List<AndroidStartup<?>> startupList = new ArrayList<>();

        public Builder addTask(AndroidStartup<?> task) {
            startupList.add(task);
            return this;
        }

        public Builder addAllStartup(List<Startup<?>> startups) {
            // startupList.addAll((Collection<? extends AndroidStartup<?>>) startups);
            return this;
        }

        public StartupManager build(Context context) {
            AtomicInteger atomicInteger = new AtomicInteger();
            for (AndroidStartup<?> startup : startupList) {
                // 当前任务在线程调用，并且需要主线程等待其执行结束
                if (!startup.callCreateOnMainThread() && startup.waitOnMainThread()) {
                    atomicInteger.decrementAndGet();
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(atomicInteger.get());
            return new StartupManager(context, startupList, countDownLatch);
        }
    }

}
