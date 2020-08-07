package com.example.llc.screen.vivo;

import android.view.Window;
import android.view.WindowManager;

import com.example.llc.screen.INotch;
import com.example.llc.screen.utils.ScreenUtils;

/**
 * com.example.llc.screen.vivo.VivoNotch
 *
 * @author liulongchao
 * @since 2020-08-07
 */
public class VivoNotch implements INotch {

    @Override
    public boolean isNotchScreen(Window window) {
        return ScreenUtils.hasViVo();
    }

    @Override
    public int getNotchHeight(Window window) {
        return ScreenUtils.getStatusBarHeight(window);
    }

    @Override
    public void setNotchAndCallBack(Window window) {
        // 遵循了 google 的开发规范
        ScreenUtils.setFullScreenWithSystemUi(window);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // 刘海屏区域
        layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
    }
}
