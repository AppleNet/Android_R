package com.example.llc.android_r.blockcanary;

import android.os.Looper;

/**
 * com.example.llc.android_r.blockcanary.BlockCanary
 *
 * @author liulongchao
 * @since 2023/11/17
 */
public class BlockCanary {

    public static void install() {
        LogMonitor logMonitor = new LogMonitor();
        Looper.getMainLooper().setMessageLogging(logMonitor);
    }
}
