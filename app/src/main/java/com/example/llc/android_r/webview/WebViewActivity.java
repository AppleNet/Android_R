package com.example.llc.android_r.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.base.utils.NestedScrollLayout;
import com.example.base.utils.NestedScrollWebView;
import com.example.llc.android_r.R;

/**
 * com.example.llc.android_r.webview.WebViewActivity
 *
 * @author liulongchao
 * @since 2022/4/22
 */
public class WebViewActivity extends AppCompatActivity {

    boolean shouFeatures = false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_coordinator_layout);
        final ImageView imageView = findViewById(R.id.imageView);
        NestedScrollWebView webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.baidu.com/");
        webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    imageView.setVisibility(View.VISIBLE);
                }
                shouFeatures = true;
                Log.d("SwanHostImpl", "onScrollChange: shouFeatures -> " + shouFeatures + ", scrollY: " + scrollY);
            }
        });


        Handler handler = new Handler(Looper.myLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!shouFeatures) {
                    Log.d("SwanHostImpl", "onScrollChange: flag -> " + shouFeatures);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        }, 5000);
    }
}
