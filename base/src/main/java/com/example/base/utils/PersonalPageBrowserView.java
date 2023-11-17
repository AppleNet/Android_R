package com.example.base.utils;

import static androidx.core.view.ViewCompat.TYPE_TOUCH;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

/**
 * com.example.base.utils.PersonalPageBrowserView
 *
 * @author liulongchao
 * @since 2023/4/20
 */
public class PersonalPageBrowserView extends WebView implements NestedScrollingChild3 {

    /** 嵌套滑动helper */
    private NestedScrollingChildHelper mScrollingChildHelper;
    /** 最小滚动速度 */
    private int mMinFlingVelocity;
    /** 最大滚动速度 */
    private int mMaxFlingVelocity;
    /** 速度器 */
    private VelocityTracker mVelocityTracker;
    /** scroller */
    private Scroller mScroller;
    /** 滑动补偿 偏移量 */
    private int mNestedYOffset;
    /** 滑动补偿 偏移量 */
    private int mNestedXOffset;
    /** 是否fling */
    private boolean fling;
    /** fling y */
    private int mLastFlingY;
    /** 滚动剩余量 */
    private final int[] mScrollConsumed = new int[2];
    /** 父容器滑动偏移量 */
    private final int[] mScrollOffset = new int[2];
    /** 点击的x */
    private int mLastMotionX;
    /** 点击的y */
    private int mLastMotionY;
    /** 滑动判断阀值 */
    private int mTouchSlop;
    /** 判断当次滑动是否是从parent开始fling开始的 */
    private boolean mLastParentScrollEnable = true;

    /** 定制errorView */
    // private final CustomNetworkErrorView errorView = new CustomNetworkErrorView(getContext());


    public PersonalPageBrowserView(@NonNull Context context) {
        super(context);
    }

    public PersonalPageBrowserView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonalPageBrowserView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PersonalPageBrowserView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        mTouchSlop = vc.getScaledTouchSlop();
        mScroller = new Scroller(getContext());
        post(new Runnable() {
            @Override
            public void run() {
                // resetStateView();
            }
        });
        // getHostContext().setName("PersonalPageBrowserView");
        // addDynamicDispatcher();
        // disableLongPress();
        // setOnCommonEventHandler(new TouchEventHandler());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        // 停止惯性滑动
        cancelFling();
        // 添加速度检测器，用于处理fling效果
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        View targetSubView = this;
        MotionEvent trackedEvent = MotionEvent.obtain(event);
        final int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedXOffset = 0;
            mNestedYOffset = 0;
        }
        int x = (int) (event.getX() + 0.5f);
        int y = (int) (event.getY() + 0.5f);
        event.offsetLocation(mNestedXOffset, mNestedYOffset);
        // 一定注意滑动补偿，不然算的速度不对
        trackedEvent.offsetLocation(mNestedXOffset, mNestedYOffset);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL | ViewCompat.SCROLL_AXIS_VERTICAL, TYPE_TOUCH);
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(trackedEvent);
                }
                trackedEvent.recycle();
                return super.onTouchEvent(event);
            case MotionEvent.ACTION_MOVE: {
                int deltaX = mLastMotionX - x;
                int deltaY = mLastMotionY - y;

                if (dispatchNestedPreScroll(deltaX, deltaY, mScrollConsumed, mScrollOffset, TYPE_TOUCH)) {
                    deltaX -= mScrollConsumed[0];
                    deltaY -= mScrollConsumed[1];
                    mNestedXOffset += mScrollOffset[0];
                    mNestedYOffset += mScrollOffset[1];
                    mLastParentScrollEnable = true;
                }

                if (Math.abs(deltaX) > mTouchSlop) {
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (Math.abs(deltaY) > mTouchSlop) {
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }

                mLastMotionX = x - mScrollOffset[0];
                mLastMotionY = y - mScrollOffset[1];

                int oldX = targetSubView.getScrollX();
                int newScrollX = Math.max(0, oldX + deltaX);
                int dxConsumed = newScrollX - oldX;
                int dxUnconsumed = deltaX - dxConsumed;

                int oldY = targetSubView.getScrollY();
                int newScrollY = Math.max(0, oldY + deltaY);
                int dyConsumed = newScrollY - oldY;
                int dyUnconsumed = deltaY - dyConsumed;

                if (dispatchNestedScroll(dxConsumed, dyConsumed,
                        dxUnconsumed, dyUnconsumed, mScrollOffset, TYPE_TOUCH)) {
                    mLastMotionX -= mScrollOffset[0];
                    mLastMotionY -= mScrollOffset[1];
                }
                mNestedXOffset += mScrollOffset[0];
                mNestedYOffset += mScrollOffset[1];
                // 由于不不支持父view和子view同时各自嵌套滚动(即各自消费一部分的情况)，
                // 所以父view如果有消费距离则直接拦住事件派发(子view此处是NgWebView，它可不管你有没有嵌套消费)
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(trackedEvent);
                }
                trackedEvent.recycle();
                if (mScrollConsumed[0] <= 0 && mScrollConsumed[1] <= 0) {
                    // 由于down事件后的move可能没给到子view，但是down事件已经给了子view去做标记，
                    // 所以反向补偿一下嵌套滑动时父view消费总距离，否则子view会突然跳跃这个距离，
                    // 注意此处是event而非trackedEvent，只有event和父子view有绑定
                    event.offsetLocation(-mNestedXOffset, -mNestedYOffset);
                }
                return false;
            }
            case MotionEvent.ACTION_UP: /* FALL-THROUGH */
            case MotionEvent.ACTION_CANCEL:
                // 触摸滑动停止
                stopNestedScroll(TYPE_TOUCH);

                // 开始判断是否需要惯性滑动
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int xvel = (int) velocityTracker.getXVelocity();
                int yvel = (int) velocityTracker.getYVelocity();
                fling(xvel, yvel);
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(trackedEvent);
                }
                trackedEvent.recycle();
                return super.onTouchEvent(event);
            default:
                return super.onTouchEvent(event);
        }
    }

    /**
     * fling
     *
     * @param velocityX x速度
     * @param velocityY y速度
     */
    private void fling(int velocityX, int velocityY) {
        // 判断速度是否足够大。如果够大才执行fling
        if (Math.abs(velocityX) < mMinFlingVelocity) {
            velocityX = 0;
        }
        if (Math.abs(velocityY) < mMinFlingVelocity) {
            velocityY = 0;
        }
        if (velocityX == 0 && velocityY == 0) {
            return;
        }
        // 此方法确定开始滑动的方向和类型，为垂直方向，触摸滑动
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH);

        velocityX = Math.max(-mMaxFlingVelocity, Math.min(velocityX, mMaxFlingVelocity));
        velocityY = Math.max(-mMaxFlingVelocity, Math.min(velocityY, mMaxFlingVelocity));
        // 开始惯性滑动
        doFling(velocityX, velocityY);
    }

    /**
     * 实际的fling处理效果
     */
    private void doFling(int velocityX, int velocityY) {
        fling = true;
        mScroller.fling(0, 0, velocityX, velocityY, Integer.MIN_VALUE,
                Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        mLastFlingY = getScrollY();
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset() && fling) {
            int y = mScroller.getCurrY();
            int unconsumed = mLastFlingY - y;
            mLastFlingY = y;
            // 在子控件处理fling之前，先判断父控件是否消耗
            boolean parentScroll = dispatchNestedPreScroll(0, unconsumed,
                    mScrollConsumed, null, ViewCompat.TYPE_NON_TOUCH);
            if (parentScroll) {
                mLastParentScrollEnable = true;
            }
            // 如果父布局不能滑动，并且当前View也不能滑动，那么取消Fling，防止消耗性能。
            if (!parentScroll && !canScrollInVertical(-1) && unconsumed < 0) {
                mScrollConsumed[1] = 0;
                stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
                cancelFling();
                return;
            }
            unconsumed -= mScrollConsumed[1];
            if (unconsumed != 0) {
                int scrolledByMe = childFlingY(unconsumed, mLastParentScrollEnable);
                unconsumed -= scrolledByMe;
                mScrollConsumed[1] = 0;
                // 将最后剩余的部分，再次还给父控件
                dispatchNestedScroll(0, scrolledByMe, 0, unconsumed, null, ViewCompat.TYPE_NON_TOUCH);
            }
            postInvalidate();
        } else {
            mScrollConsumed[1] = 0;
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
            cancelFling();
        }
    }

    /**
     * 停止Fling
     */
    public void cancelFling() {
        mLastParentScrollEnable = false;
        fling = false;
        mLastFlingY = 0;
    }

    /**
     * webview touch事件
     */
    class TouchEventHandler implements NgWebView.OnCommonEventHandler {

        @Override
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return onWebViewTouchEvent(motionEvent);
        }

        @Override
        public boolean onKeyDown(int i, KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                handleBack();
            }
            return false;
        }

        @Override
        public void onScrollChanged(int i, int i1, int i2, int i3) {
            // 为了解决头部露出的时候，手指往上滑，头部吸顶效果，有两种解决方案：
            // 方法一：onWebViewTouchEvent的move和up方法返回true，拦截webview的滑动事件，优先头部滑动。但是有一个问题：因为只给了webview down事件，导致webview认为是长按，会触发item的长按透明度变化效果。
            // 方法二：不拦截webview的move和up事件，让webview滑动，同时触发头部的滑动，通过监听onScrollChanged方法，判断当头部可滑动的时候，强制webview滑回来。
            ViewParent parent = getParent().getParent().getParent();
            if (parent instanceof NestedScrollingParent2Layout) {
                boolean canScroll = ((NestedScrollingParent2Layout) parent)
                        .isCanScroll(i1, PersonalPageBrowserView.this);
                if (canScroll) {
                    scrollWebViewTo(0, 0);
                }
            }
        }
    }

    /**
     * 子控件消耗多少竖直方向上的fling,由子控件自己决定
     *
     * @param dy 父控件消耗部分竖直fling后,剩余的距离
     * @return 子控件竖直fling，消耗的距离
     */
    private int childFlingY(int dy, boolean lastParentScrollEnable) {
        if (dy > 0 && lastParentScrollEnable) {
            // 使用flingScroll 可以在头部滑不动的时候让webview平滑滚动 不要使用scrollTo，会滑的特别快
            float currVelocity = mScroller.getCurrVelocity();
            flingScroll(0, ((int) currVelocity));
            mLastParentScrollEnable = false;
            return dy;
        }
        return dy;
    }

    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {

    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return false;
    }

    @Override
    public void stopNestedScroll(int type) {

    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return false;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return false;
    }

    /**
     * 获取指定子View
     *
     * @return 返回LightBrowserView中的NgWebView
     */
//    private View getTargetSubView() {
//        View target;
//        BdSailorWebView webView = getKernel();
//        WebView currentWebView = webView.getCurrentWebView();
//        if (currentWebView != null) {
//            View absoluteLayout = currentWebView.getWebView();
//            if (absoluteLayout != null) {
//                target = absoluteLayout;
//            } else {
//                target = currentWebView;
//            }
//        } else {
//            target = webView;
//        }
//        return target;
//    }
}
