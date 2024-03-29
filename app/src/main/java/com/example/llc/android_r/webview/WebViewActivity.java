package com.example.llc.android_r.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.base.utils.NestedScrollWebView;
import com.example.llc.android_r.R;

/**
 * com.example.llc.android_r.webview.WebViewActivity
 *
 * @author liulongchao
 * @since 2022/4/22
 */
public class WebViewActivity extends AppCompatActivity {

    private boolean showFeatures = false;


    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_coordinator_layout);
        final ImageView imageView = findViewById(R.id.imageView);
        WebView webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.loadUrl("https://m.baidu.com/s?from=1026372p&word=李子柒");
        webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    // 滚动到顶部
                    imageView.setVisibility(View.VISIBLE);
                }
                showFeatures = true;
                // Log.d("SwanHostImpl", "onScrollChange: shouFeatures -> " + showFeatures + ", scrollY: " + scrollY);
            }
        });

        Handler handler = new Handler(Looper.myLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!showFeatures) {
                    // Log.d("SwanHostImpl", "onScrollChange: showFeatures -> " + showFeatures);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        }, 5000);
    }
}
