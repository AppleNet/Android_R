package com.example.llc.android_r.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.llc.android_r.R
import com.example.llc.screen.NotchManager
import com.example.llc.screen.OnNotchCallBack

/**
 * com.example.llc.android_r.screen.ScreenActivity
 * @author liulongchao
 * @since 2021/3/4
 */
class ScreenActivity: AppCompatActivity(), OnNotchCallBack {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen)
        NotchManager.getInstance().setOnNotchListener(window, this)
    }

    override fun onNotchLandscapeCallback(margin: Int) {
        // 设置控件的 margin -> topMargin  = margin
    }

    override fun onNotchPortraitCallback(margin: Int) {
        // 判断水平方向是 270 度 还是 90
        // leftMargin or rightMargin
    }
}