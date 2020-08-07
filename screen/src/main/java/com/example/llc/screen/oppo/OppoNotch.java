package com.example.llc.screen.oppo;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.example.llc.screen.INotch;
import com.example.llc.screen.utils.ScreenUtils;

/**
 * com.example.llc.screen.oppo.OppoNotch
 *
 * @author liulongchao
 * @since 2020-08-07
 */
public class OppoNotch implements INotch {

    @Override
    public boolean isNotchScreen(Window window) {
        return ScreenUtils.hasOPPO(window.getContext());
    }

    @Override
    public int getNotchHeight(Window window) {
        return ScreenUtils.getStatusBarHeight(window);
    }

    @Override
    public void setNotchAndCallBack(Window window) {
        // 遵循了 google 的开发规范
        // 设置全面屏  // 把状态栏去掉
        ScreenUtils.setFullScreenWithSystemUi(window);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // 刘海屏区域
        layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
    }


}
