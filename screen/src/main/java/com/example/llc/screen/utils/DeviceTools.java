package com.example.llc.screen.utils;

import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * com.example.llc.screen.utils.DeviceTools
 *
 * @author liulongchao
 * @since 2020-08-07
 */
public class DeviceTools {

    public static boolean isHuaWei() {
        String manufacturer = Build.MANUFACTURER;
        if (!TextUtils.isEmpty(manufacturer)) {
            if (manufacturer.contains("HUAWEI")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOppo() {
        String manufacturer = Build.MANUFACTURER;
        if (!TextUtils.isEmpty(manufacturer)) {
            if ("oppo".equalsIgnoreCase(manufacturer)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMiUi() {

        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("get", String.class);
            String xiaomi = (String) get.invoke(null, "ro.niui.ui.version.name");
            return !TextUtils.isEmpty(xiaomi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isViVo() {

        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("get", String.class);
            String xiaomi = (String) get.invoke(null, "ro.vivo.os.name");
            return !TextUtils.isEmpty(xiaomi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
