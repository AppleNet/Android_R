package com.example.llc.screen.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class ScreenUtils {

    /**
     * MiUi 手机刘海屏适配
     */
    // 反射获取 SystemProperties.getInt 方法
    @SuppressLint("PrivateApi")
    public static boolean hasMiUi() {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("getInt", String.class, int.class);
            return (Integer) method.invoke(null, "ro.miui.notch", 0) == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ViVo 手机刘海屏适配
     */
    @SuppressLint("PrivateApi")
    public static boolean hasViVo() {
        try {
            int VIVO_NOTCH = 0x00000020;
            Class<?> clazz = Class.forName("android.util.FtFeature");
            Method method = clazz.getMethod("isFeatureSupport", int.class);
            return (boolean) method.invoke(null, VIVO_NOTCH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * OPPO 手机刘海屏适配
     */
    public static boolean hasOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    /**
     * 华为 手机刘海屏适配
     */
    public static boolean hasEMUI(Context context) {
        ClassLoader classLoader = context.getClassLoader();
        try {
            Class<?> HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            return (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     *  设置全屏，去掉状态栏
     *
     * */
    public static void setFullScreenWithSystemUi(final Window window) {
        int systemUiVisibility = 0;
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attributes);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
    }

    public static int getStatusBarHeight(Window window) {
        int resourceId = window.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return window.getContext().getResources().getDimensionPixelSize(resourceId);
        } else {
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object instance = clazz.newInstance();
                Field statusBarHeight = clazz.getField("status_bar_height");
                resourceId = Integer.parseInt(Objects.requireNonNull(statusBarHeight.get(instance)).toString());
                return window.getContext().getResources().getDimensionPixelSize(resourceId);
            } catch (Exception e) {
                //
            }
        }
        return 0;
    }

}
