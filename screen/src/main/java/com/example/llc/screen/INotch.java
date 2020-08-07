package com.example.llc.screen;

import android.view.Window;

/**
 * com.example.llc.screen.INotch
 *
 * @author liulongchao
 * @since 2020-08-07
 */
public interface INotch {

    String TAG = "INotch";

    /**
     * 判断当前是否是刘海屏
     *
     * @param window window
     * @return true
     */
    boolean isNotchScreen(Window window);

    /**
     * 获取刘海屏的高度
     *
     * @param window window
     * @return int
     */
    int getNotchHeight(Window window);

    /**
     * 设置刘海屏，使用刘海屏区域内容
     *
     * @param window window
     */
    void setNotchAndCallBack(Window window);
}
