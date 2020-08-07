package com.example.llc.screen.common;

import android.view.DisplayCutout;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.example.llc.screen.INotch;
import com.example.llc.screen.utils.ScreenUtils;
/**
 * com.example.llc.screen.common.CommonNotch
 *
 * @author liulongchao
 * @since 2020-08-07
 */
public class CommonNotch implements INotch {

    @Override
    public boolean isNotchScreen(Window window) {
        WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
        if (windowInsets == null) {
            return false;
        }
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        if (displayCutout == null || displayCutout.getBoundingRects() == null) {
            return false;
        }
        return true;
    }

    @Override
    public int getNotchHeight(Window window) {
        return ScreenUtils.getStatusBarHeight(window);
    }

    @Override
    public void setNotchAndCallBack(Window window) {
        ScreenUtils.setFullScreenWithSystemUi(window);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // 刘海屏区域
        layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
    }
}
