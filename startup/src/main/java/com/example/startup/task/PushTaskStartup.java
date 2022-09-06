package com.example.startup.task;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.example.startup.Startup;
import com.example.startup.impl.AndroidStartup;

import java.util.List;

/**
 * com.example.startup.task.PushTaskStartup
 *
 * @author liulongchao
 * @since 2022/9/1
 */
public class PushTaskStartup extends AndroidStartup<Void> {

    /**
     * push sdk 的初始化
     * @param context context
     * @return void
     */
    @Override
    public Void create(Context context) {
        Log.d("Startup", "init push sdk start");
        SystemClock.sleep(3000);
        Log.d("Startup", "init push sdk end");
        return null;
    }

    /**
     * 返回 push sdk 初始化任务依赖的任务
     * @return List<Class<? extends Startup<?>>>
     */
    @Override
    public List<Class<? extends Startup<?>>> dependencies() {
        return super.dependencies();
    }

}
