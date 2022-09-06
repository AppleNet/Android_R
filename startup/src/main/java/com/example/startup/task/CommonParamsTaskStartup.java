package com.example.startup.task;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.example.startup.Startup;
import com.example.startup.impl.AndroidStartup;

import java.util.ArrayList;
import java.util.List;

/**
 * com.example.startup.task.CommonParamsStartup
 *
 * @author liulongchao
 * @since 2022/9/2
 */
public class CommonParamsTaskStartup extends AndroidStartup<Void> {

    private static final List<Class<? extends Startup<?>>> dependencies;

    static {
        dependencies = new ArrayList<>();
        dependencies.add(PushTaskStartup.class);
    }

    /**
     * CommonParams sdk初始化
     * @param context context
     * @return void
     */
    @Override
    public Void create(Context context) {
        Log.d("Startup", "init commonParams sdk start");
        SystemClock.sleep(3000);
        Log.d("Startup", "init commonParams sdk end");
        return null;
    }

    /**
     * 返回 commonParam sdk 初始化任务依赖的任务
     * @return dependencies
     */
    @Override
    public List<Class<? extends Startup<?>>> dependencies() {
        return dependencies;
    }

}
