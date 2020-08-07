package com.example.llc.screen.emui;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.llc.screen.INotch;
import com.example.llc.screen.utils.ScreenUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * com.example.llc.screen.emui.EmuiNotch
 *
 * @author liulongchao
 * @since 2020-08-07
 */
public class EmuiNotch implements INotch {


    @Override
    public boolean isNotchScreen(Window window) {
        return ScreenUtils.hasEMUI(window.getContext());
    }

    @Override
    public int getNotchHeight(Window window) {
        int[] ret = new int[]{0, 0};
        ClassLoader classLoader = window.getContext().getClassLoader();
        try {
            Class<?> HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
            return ret[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void setNotchAndCallBack(Window window) {
        // 设置使用刘海屏里面的内容
        if (window == null) {
            return;
        }
        int FLAG_NOTCH_SUPPORT = 0x00010000;
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        try {
            Class<?> layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor<?> constructor = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExClsObj = constructor.newInstance(layoutParams);
            Method method = layoutParamsExCls.getMethod("addHwFlags", int.class);
            method.invoke(layoutParamsExClsObj, FLAG_NOTCH_SUPPORT);
        } catch (Exception e) {
            Log.i(TAG, "emui setNotchAndCallBack error.");
        }
    }
}
