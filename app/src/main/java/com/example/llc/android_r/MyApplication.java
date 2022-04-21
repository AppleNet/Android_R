package com.example.llc.android_r;

import android.app.Application;

/**
 * com.example.llc.android_r.MyApplication
 *
 * @author liulongchao
 * @since 2022/4/19
 */
public class MyApplication extends Application {

    public static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Application getApplication() {
        return sApplication;
    }
}
