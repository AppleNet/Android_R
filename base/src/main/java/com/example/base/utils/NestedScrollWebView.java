package com.example.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.OverScroller;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

public class NestedScrollWebView extends WebView implements NestedScrollingChild2 {

    private static final String TAG = "SwanHostImpl";

    private final int[] mScrollConsumed = new int[2];
    private final int[] mScrollOffset = new int[2];
    /**
     *
     */
    private final int mMinimumVelocity;
    /**
     *
     */
    private final int mMaximumVelocity;
    /**
     * fling 滑动关键处理类
     */
    private final OverScroller mScroller;
    /**
     * 嵌套滑动关键处理类
     */
    private final NestedScrollingChildHelper mChildHelper;
    /**
     * Y轴 最后按下坐标
     */
    private int mLastMotionY;
    /**
     * 最新的滚动距离
     */
    private int mLastScrollerY;
    /**
     * 追踪器
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 构造方法
     * @param context context
     */
    public NestedScrollWebView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     * @param context context
     * @param attrs attrs
     */
    public NestedScrollWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     * @param context context
     * @param attrs attrs
     * @param defStyleAttr defStyleAttr
     */
    public NestedScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化嵌套滚动帮助类
        mChildHelper = new NestedScrollingChildHelper(this);
        // 允许嵌套滚动
        setNestedScrollingEnabled(true);
        // 快速滑动处理类
        mScroller = new OverScroller(getContext());
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    /**
     * 初始化 VelocityTracker
     */
    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    /**
     * 回收 VelocityTracker
     */
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 快速滑动
     * @param velocityY Y轴滑动距离
     */
    public void fling(int velocityY) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH);
        mScroller.fling(getScrollX(), getScrollY(), // start
                0, velocityY, // velocities
                0, 0, // x
                Integer.MIN_VALUE, Integer.MAX_VALUE, // y
                0, 0); // overscroll
        mLastScrollerY = getScrollY();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            final int x = mScroller.getCurrX();
            final int y = mScroller.getCurrY();
            Log.d(TAG, "computeScroll: y : " + y);
            int dy = y - mLastScrollerY;
            if (dy != 0) {
                int scrollY = getScrollY();
                int dyUnConsumed = 0;
                int consumedY = dy;
                if (scrollY == 0) {
                    dyUnConsumed = dy;
                    consumedY = 0;
                } else if (scrollY + dy < 0) {
                    dyUnConsumed = dy + scrollY;
                    consumedY = -scrollY;
                }

                if (!dispatchNestedScroll(0, consumedY, 0, dyUnConsumed, null,
                        ViewCompat.TYPE_NON_TOUCH)) {
                    // do nothing
                }
            }
            // Finally update the scroll positions and post an invalidation
            mLastScrollerY = y;
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            // We can't scroll any more, so stop any indirect scrolling
            if (hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
                stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
            }
            // and reset the scroller y
            mLastScrollerY = 0;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        MotionEvent vtev = MotionEvent.obtain(event);
        final int actionMasked = event.getAction();

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) event.getRawY();
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                mVelocityTracker.addMovement(vtev);
                mScroller.computeScrollOffset();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    fling(-initialVelocity);
                }
            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll();
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_MOVE:
                final int y = (int) event.getRawY();
                int deltaY = mLastMotionY - y;
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    Log.d(TAG, "onTouchEvent: deltaY : " + deltaY + " , mScrollConsumedY : " + mScrollConsumed[1] + " , mScrollOffset : " + mScrollOffset[1]);
                    vtev.offsetLocation(0, mScrollConsumed[1]);
                }
                mLastMotionY = y;

                int scrollY = getScrollY();

                int dyUnconsumed = 0;
                if (scrollY == 0) {
                    dyUnconsumed = deltaY;
                } else if (scrollY + deltaY < 0) {
                    dyUnconsumed = deltaY + scrollY;
                    vtev.offsetLocation(0, -dyUnconsumed);
                }
                mVelocityTracker.addMovement(vtev);
                boolean result = super.onTouchEvent(vtev);
                if (dispatchNestedScroll(0, deltaY - dyUnconsumed, 0, dyUnconsumed, mScrollOffset)) {
                    // do nothing
                }
                return result;
            default:

                break;
        }
        return super.onTouchEvent(vtev);
    }

    // NestedScrollingChild2
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return mChildHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public void stopNestedScroll(int type) {
        mChildHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return mChildHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow, int type) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow,
                                           int type) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
