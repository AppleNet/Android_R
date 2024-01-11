package com.example.llc.android_r;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;

//import com.example.skin.SkinManager;

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
        // SkinManager.getInstance().init(this);
    }

    public static Application getApplication() {
        return sApplication;
    }

    @Override
    public Resources getResources() {
//        Resources skinResources = SkinManager.getInstance().getSkinResources();
//        if (skinResources != null) {
//            if (skinResources.getConfiguration() != getSuperResources().getConfiguration()
//                    || skinResources.getDisplayMetrics() != getSuperResources().getDisplayMetrics()) {
//                skinResources.updateConfiguration(getSuperResources().getConfiguration(), getSuperResources().getDisplayMetrics());
//            }
//            return skinResources;
//        }
        return getSuperResources();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 适配 Android 12 屏幕切换 resources 不更新的问题
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            getSuperResources().updateConfiguration(newConfig, Resources.getSystem().getDisplayMetrics());
        }
    }

    public Resources getSuperResources() {
        return super.getResources();
    }
}
