package com.example.startup;

import java.util.concurrent.Executor;

/**
 * com.example.startup.Dispatcher
 *
 * @author liulongchao
 * @since 2022/9/2
 */
public interface Dispatcher {

    /**
     * 当前任务是否在主线程执行
     * @return true 主线程调用
     */
    boolean callCreateOnMainThread();

    /**
     * 主线程是否需要等待该任务执行结束
     * @return true 等待主线程结束
     */
    boolean waitOnMainThread();

    /**
     * 等待
     */
    void toWait();

    /**
     * 唤醒
     */
    void toNotify();

    /**
     * 获取当前任务被执行的时候 需要的线程池
     * @return Executor
     */
    Executor executor();

    /**
     * 线程优先级
     * @return int
     */
    int getThreadPriority();

}
