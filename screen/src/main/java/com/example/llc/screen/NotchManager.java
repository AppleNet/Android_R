package com.example.llc.screen;

import android.content.res.Configuration;
import android.view.View;
import android.view.Window;

import com.example.llc.screen.emui.EmuiNotch;
import com.example.llc.screen.miui.MiUiNotch;
import com.example.llc.screen.oppo.OppoNotch;
import com.example.llc.screen.utils.DeviceTools;
import com.example.llc.screen.vivo.VivoNotch;

/**
 * com.example.llc.screen.NotchManager
 *
 * @author liulongchao
 * @since 2020-08-07
 */
public class NotchManager {

    private INotch iNotch;

    private NotchManager() {
        if (iNotch == null) {
            if (DeviceTools.isHuaWei()) {
                iNotch = new EmuiNotch();
            } else if (DeviceTools.isMiUi()) {
                iNotch = new MiUiNotch();
            } else if (DeviceTools.isOppo()) {
                iNotch = new OppoNotch();
            } else if (DeviceTools.isViVo()) {
                iNotch = new VivoNotch();
            }
        }
    }

    private static final class Holder{
        private static final NotchManager NOTCH_MANAGER = new NotchManager();
    }

    public static NotchManager getInstance() {
        return Holder.NOTCH_MANAGER;
    }

    public void setOnNotchListener(final Window window, final OnNotchCallBack onNotchCallBack) {

        window.getDecorView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                // 只有绑定到 window 之后在进行获取
                boolean isNotchScreen = iNotch.isNotchScreen(window);
                if (isNotchScreen) {
                    // 是刘海屏
                    int height = iNotch.getNotchHeight(window);
                    // 设置使用刘海屏内的内容
                    iNotch.setNotchAndCallBack(window);
                    Configuration configuration = window.getContext().getResources().getConfiguration();
                    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        onNotchCallBack.onNotchPortraitCallback(height);
                    } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                        onNotchCallBack.onNotchLandscapeCallback(height);
                    }
                }

            }

            @Override
            public void onViewDetachedFromWindow(View view) {

            }
        });
    }
}
