package com.example.base.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * com.example.base.utils.NestedScrollLayout
 *
 * @author liulongchao
 * @since 2022/4/22
 */
public class NestedScrollLayout extends NestedScrollView {

    /**
     * TAG
     */
    private static final String TAG = "SwanHostImpl";
    /**
     *
     */
    private View mTopView;
    /**
     * ViewGroup
     */
    private ViewGroup mContentView;
    /**
     * 滑动帮助类
     */
    private FlingHelper mFlingHelper;

    /**
     * Y 轴总的偏移量
     */
    private int totalDy = 0;
    /**
     * 用于判断 View 是否在 fling
     */
    boolean isStartFling = false;
    /**
     * 记录当前滑动的 Y 轴加速度
     */
    private int velocityY = 0;
    /**
     *
     */
    private float mDownY;

    private boolean mIsWebViewOnBottom;

    public NestedScrollLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public NestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化相关配置信息
     */
    private void init() {
        mFlingHelper = new FlingHelper(getContext());
        setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (isStartFling) {
                    totalDy = 0;
                    isStartFling = false;
                }
                if (scrollY == 0) {
                    Log.d(TAG, "Top Scroll");
                }

                if (scrollY == (getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.d(TAG, "Bottom Scroll");
                    // 分发子类的 Fling
                    dispatchChildFling();
                }

                //在 View fling 情况下，记录当前 View 在 Y 轴的偏移
                totalDy += scrollY - oldScrollY;
            }
        });
    }

    /**
     * fling 回调
     * @param velocityY velocityY 距离
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
        if (velocityY <= 0) {
            this.velocityY = 0;
        } else {
            isStartFling = true;
            this.velocityY = velocityY;
        }
    }

    /**
     * inflate 完成回调
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = ((ViewGroup) getChildAt(0)).getChildAt(0);
        Log.d(TAG, "mTopView: "  + mTopView.getClass().getSimpleName());
        mContentView = (ViewGroup) getChildAt(0);
        Log.d(TAG, "mContentView: "  + mContentView.getClass().getSimpleName());
    }

    /**
     * 测量回调
     * 调整contentView的高度为父容器高度，使之填充布局，避免父容器滚动后出现空白
     * @param widthMeasureSpec widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams lp = mContentView.getLayoutParams();
        lp.height = getMeasuredHeight();
        mContentView.setLayoutParams(lp);
    }

    /**
     * onNestedPreScroll 回调
     * @param target 目标 View
     * @param dx x轴距离
     * @param dy y轴距离
     * @param consumed consumed
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.d(TAG, getScrollY() + "::onNestedPreScroll::" + mTopView.getMeasuredHeight());
        // 向上滑动。若当前 mTopView 可见，需要将 mTopView 滑动至不可见
        boolean hideTop = dy > 0 && getScrollY() < mTopView.getMeasuredHeight();
        if (hideTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    /**
     * 分发子类的 fling
     */
    private void dispatchChildFling() {
        if (velocityY != 0) {
            double splineFlingDistance = mFlingHelper.getSplineFlingDistance(velocityY);
            if (splineFlingDistance > totalDy) {
                childFling(mFlingHelper.getVelocityByDistance(splineFlingDistance - (double) totalDy));
            }
        }
        totalDy = 0;
        velocityY = 0;
    }

    /**
     * 设置子 View Fling 距离
     * @param velY velY
     */
    private void childFling(int velY) {
        WebView webView = getChildWebView(mContentView);
        if (webView != null) {
            Log.d(TAG, "flingScroll" + velY);
            webView.flingScroll(0, velY);
        } else {
            Log.d(TAG, "flingScroll " + "webView == null");
        }
    }

    /**
     * 获取 ViewGroup 中的 WebView 控件
     * @param viewGroup viewGroup
     * @return WebView
     */
    private WebView getChildWebView(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof WebView && view.getClass() == WebView.class) {
                return (WebView) viewGroup.getChildAt(i);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                WebView childWebView = getChildWebView((ViewGroup) viewGroup.getChildAt(i));
                if (childWebView != null) {
                    return childWebView;
                }
            }
        }
        return null;
    }
}
