package com.example.startup.task;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.example.startup.Startup;
import com.example.startup.impl.AndroidStartup;

import java.util.ArrayList;
import java.util.List;

/**
 * com.example.startup.task.ImTaskStartup
 *
 * @author liulongchao
 * @since 2022/9/2
 */
public class ImTaskStartup extends AndroidStartup<Void> {

    private static final List<Class<? extends Startup<?>>> dependencies;

    static {
        dependencies = new ArrayList<>();
        dependencies.add(DeviceIdTaskStartup.class);
        dependencies.add(CuidTaskStartup.class);
    }

    /**
     * 初始化 im sdk
     * @param context context
     * @return void
     */
    @Override
    public Void create(Context context) {
        Log.d("Startup", "init im sdk start");
        SystemClock.sleep(3000);
        Log.d("Startup", "init im sdk end");
        return null;
    }

    /**
     * 返回 im sdk 初始化任务依赖的任务
     * @return dependencies
     */
    @Override
    public List<Class<? extends Startup<?>>> dependencies() {
        return dependencies;
    }

}
