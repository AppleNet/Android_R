package com.example.startup.impl;

import android.os.Process;

import com.example.startup.Startup;
import com.example.startup.manager.ExecutorManager;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

/**
 * com.example.startup.impl.AndroidStartup
 *
 * @author liulongchao
 * @since 2022/9/1
 */
public abstract class AndroidStartup<T> implements Startup<T> {

    /**
     * 执行线程调度，给每个任务 都创建一个闭锁，对应的状态码是依赖的任务数
     *
     * 如果状态码是0，那么调用await的时候，并不会被阻塞
     */
    private final CountDownLatch mWaitDownLatch = new CountDownLatch(getDependenciesCount());

    /**
     * 获取依赖的任务
     * @return 依赖的任务
     */
    @Override
    public List<Class<? extends Startup<?>>> dependencies() {
        return null;
    }

    /**
     * 获取依赖的任务数
     * @return int
     */
    @Override
    public int getDependenciesCount() {
        List<Class<? extends Startup<?>>> dependencies = dependencies();
        return dependencies == null ? 0 : dependencies.size();
    }

    /**
     * 唤醒
     */
    @Override
    public void toNotify() {
        mWaitDownLatch.countDown();
    }

    /**
     * 等待
     */
    @Override
    public void toWait() {
        try {
            mWaitDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取执行的线程池
     * @return Executor
     */
    @Override
    public Executor executor() {
        return ExecutorManager.mainExecutor;
    }

    /**
     * 任务优先级
     * @return int
     */
    @Override
    public int getThreadPriority() {
        return Process.THREAD_PRIORITY_DEFAULT;
    }

    /**
     * 是否在主线程执行
     * @return false
     */
    @Override
    public boolean callCreateOnMainThread() {
        return false;
    }

    /**
     * 是否等待主线程执行结束
     * @return false
     */
    @Override
    public boolean waitOnMainThread() {
        return false;
    }
}
