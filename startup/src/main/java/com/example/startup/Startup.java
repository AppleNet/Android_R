package com.example.startup;

import android.content.Context;

import java.util.List;

/**
 * com.example.startup.StartUp
 *
 * @author liulongchao
 * @since 2022/9/1
 */
public interface Startup<T> extends Dispatcher {

    /**
     * 创建当前任务
     * @param context context
     * @return T 任务执行结果，返回值类型
     */
    T create(Context context);

    /**
     * 当前任务 依赖了哪些任务
     *
     * @return List<Class<? extends StartUp<?>>>
     */
    List<Class<? extends Startup<?>>> dependencies();

    /**
     * 当前任务依赖了多少个任务
     * @return int
     */
    int getDependenciesCount();
}
