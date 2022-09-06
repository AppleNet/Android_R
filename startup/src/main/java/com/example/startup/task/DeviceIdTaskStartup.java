package com.example.startup.task;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.example.startup.Startup;
import com.example.startup.impl.AndroidStartup;

import java.util.ArrayList;
import java.util.List;

/**
 * com.example.startup.task.DeviceIdTaskStartup
 *
 * @author liulongchao
 * @since 2022/9/2
 */
public class DeviceIdTaskStartup extends AndroidStartup<Void> {

    private static final List<Class<? extends Startup<?>>> dependencies;

    static {
        dependencies = new ArrayList<>();
        dependencies.add(CommonParamsTaskStartup.class);
    }

    /**
     * 初始化设备sdk
     * @param context context
     * @return void
     */
    @Override
    public Void create(Context context) {
        Log.d("Startup", "init deviceId sdk start");
        SystemClock.sleep(3000);
        Log.d("Startup", "init deviceId sdk end");
        return null;
    }

    /**
     * 返回 device sdk 初始化任务依赖的任务
     * @return dependencies
     */
    @Override
    public List<Class<? extends Startup<?>>> dependencies() {
        return dependencies;
    }

}
