package com.example.llc.screen.miui;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;

import com.example.llc.screen.INotch;
import com.example.llc.screen.utils.ScreenUtils;

import java.lang.reflect.Method;

/**
 * com.example.llc.screen.miui.MiUiNotch
 *
 * @author liulongchao
 * @since 2020-08-07
 */
public class MiUiNotch implements INotch {

    @Override
    public boolean isNotchScreen(Window window) {
        return ScreenUtils.hasMiUi();
    }

    @Override
    public int getNotchHeight(Window window) {
        // 判断是否隐藏了刘海屏
        int result;
        if (!isHideNotch(window.getContext())) {
            int resourceId = window.getContext().getResources().getIdentifier("notch_height", "dimen", "android");
            if (resourceId > 0) {
                result = window.getContext().getResources().getDimensionPixelSize(resourceId);
            } else {
                result = ScreenUtils.getStatusBarHeight(window);
            }
        } else {
            // 隐藏刘海屏，直接获取状态栏高度
            result = ScreenUtils.getStatusBarHeight(window);
        }
        return result;
    }

    @Override
    public void setNotchAndCallBack(Window window) {
        // 设置使用刘海屏里面的内容
        int flag = 0x00000100 | 0x00000200 | 0x00000400;
        try {
            Method method = Window.class.getMethod("addExtraFlags",
                    int.class);
            method.invoke(window, flag);
        } catch (Exception e) {
            Log.i(TAG, "addExtraFlags not found.");
        }
    }

    private boolean isHideNotch(Context activity) {
        return Settings.Global.getInt(activity.getContentResolver(), "force_back", 0) == 1;
    }

}
