package com.example.startup.manager;

import com.example.startup.Startup;
import com.example.startup.store.Result;
import java.util.concurrent.ConcurrentHashMap;

/**
 * com.example.startup.manager.StartupCacheManager
 *
 * @author liulongchao
 * @since 2022/9/2
 */
public class StartupCacheManager {

    /**
     * 执行结束的任务集合
     */
    private final ConcurrentHashMap<Class<? extends Startup>, Result> mInitializedComponents = new ConcurrentHashMap<>();
    /**
     * 单例
     */
    private static StartupCacheManager mInstance;

    /**
     * 私有构造
     */
    private StartupCacheManager() {}

    /**
     * 对外暴漏 StartupCacheManager
     * @return StartupCacheManager
     */
    public static StartupCacheManager getInstance() {
        if (mInstance == null) {
            synchronized (StartupCacheManager.class) {
                if (mInstance == null) {
                    mInstance = new StartupCacheManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * save result of initialized component.
     * @param zClass zClass
     * @param result result
     */
    public void saveInitializedComponent(Class<? extends Startup> zClass, Result result) {
        mInitializedComponents.put(zClass, result);
    }

    /**
     * check initialized.
     * @param zClass zClass
     * @return true
     */
    public boolean hadInitialized(Class<? extends Startup> zClass) {
        return mInitializedComponents.containsKey(zClass);
    }

    /**
     * 获取 result
     * @param zClass zClass
     * @param <T> T
     * @return Result<T>
     */
    public <T> Result<T> obtainInitializedResult(Class<? extends Startup<T>> zClass) {
        return mInitializedComponents.get(zClass);
    }


    /**
     * 移除 result
     * @param zClass zClass
     */
    public void remove(Class<? extends Startup> zClass) {
        mInitializedComponents.remove(zClass);
    }

    /**
     * 清空 result
     */
    public void clear() {
        mInitializedComponents.clear();
    }
}
