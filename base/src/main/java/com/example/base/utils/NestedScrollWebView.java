package com.example.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.OverScroller;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;



public class NestedScrollWebView extends WebView implements NestedScrollingChild2, NestedScrollingChild3 {

    private static final String TAG = "SwanHostImpl";

    private final int[] mScrollConsumed = new int[2];
    private final int[] mScrollOffset = new int[2];
    private int mNestedYOffset;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mTouchSlop;
    private boolean mIsBeingDragged = false;
    private OverScroller mScroller;
    private NestedScrollingChildHelper mChildHelper;
    private int mLastMotionY;
    private int mLastScrollerY;
    private VelocityTracker mVelocityTracker;

    public NestedScrollWebView(Context context) {
        this(context, null);
    }

    public NestedScrollWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollView();
    }

    // NestedScrollingChild3
    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                     int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {
        mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow, type, consumed);
    }

    // NestedScrollingChild2
    @Override
    public boolean startNestedScroll(int axes, int type) {
        return mChildHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        mChildHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return mChildHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow, int type) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow,
                                           int type) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    // NestedScrollingChild
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
        return startNestedScroll(axes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void stopNestedScroll() {
        stopNestedScroll(ViewCompat.TYPE_TOUCH);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return hasNestedScrollingParent(ViewCompat.TYPE_TOUCH);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private void onNestedScrollInternal(int dyUnconsumed, int type, @Nullable int[] consumed) {
        final int oldScrollY = getScrollY();
        scrollBy(0, dyUnconsumed);
        final int myConsumed = getScrollY() - oldScrollY;
        if (consumed != null) {
            consumed[1] += myConsumed;
        }
        final int myUnconsumed = dyUnconsumed - myConsumed;
        mChildHelper.dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null, type, consumed);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        onNestedScrollInternal(dyUnconsumed, ViewCompat.TYPE_TOUCH, null);
    }

    @Override
    public boolean onNestedFling(
            @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            dispatchNestedFling(0, velocityY, true);
            fling((int) velocityY);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    private void initScrollView() {
        // 初始化嵌套滚动帮助类
        mChildHelper = new NestedScrollingChildHelper(this);
        // 允许嵌套滚动
        setNestedScrollingEnabled(true);
        // 快速滑动处理类
        mScroller = new OverScroller(getContext());
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        final int actionMasked = event.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }
        MotionEvent vtev = MotionEvent.obtain(event);
        vtev.offsetLocation(0, mNestedYOffset);

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "MotionEvent.ACTION_DOWN... ");
                if (!mScroller.isFinished()) {
                    abortAnimatedScroll();
                }
                mLastMotionY = (int) event.getRawY();
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                mScroller.computeScrollOffset();
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "MotionEvent.ACTION_UP... ");
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    Log.d(TAG, "initialVelocity: " + Math.abs(initialVelocity) + ", mMinimumVelocity: " + mMinimumVelocity);
                    if (!dispatchNestedPreFling(0, -initialVelocity)) {
                        dispatchNestedFling(0, -initialVelocity, true);
                        fling(-initialVelocity);
                    }
                } else if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0,
                        getScrollRange())) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                endDrag();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MotionEvent.ACTION_MOVE... ");
                final int y = (int) event.getRawY();
                int deltaY = mLastMotionY - y;
                Log.d(TAG, "deltaY : " + deltaY + " , mTouchSlop : " + mTouchSlop);
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    mIsBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                Log.d(TAG, "deltaY : " + deltaY );

                if (mIsBeingDragged) {
                    if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset, ViewCompat.TYPE_TOUCH)) {
                        Log.d(TAG, "onTouchEvent: deltaY : " + deltaY + " , mScrollConsumedY : " + mScrollConsumed[1] + " , mScrollOffset : " + mScrollOffset[1]);
                        deltaY -= mScrollConsumed[1];
                        mNestedYOffset += mScrollOffset[1];
                    }

                    mLastMotionY = y - mScrollOffset[1];

                    final int range = getScrollRange();
                    if (overScrollByCompat(0, deltaY, 0, getScrollY(), 0, range, 0,
                            0) && !hasNestedScrollingParent(ViewCompat.TYPE_TOUCH)) {
                        if (mVelocityTracker != null) {
                            mVelocityTracker.clear();
                        }
                    }

                    int scrollY = getScrollY();
                    int dyUnconsumed = 0;
                    if (scrollY == 0) {
                        dyUnconsumed = deltaY;
                    } else if (scrollY + deltaY < 0) {
                        dyUnconsumed = deltaY + scrollY;
                    }
                    mScrollConsumed[1] = 0;
                    dispatchNestedScroll(0, deltaY - dyUnconsumed, 0, dyUnconsumed, mScrollOffset,
                            ViewCompat.TYPE_TOUCH, mScrollConsumed);
                    mLastMotionY -= mScrollOffset[1];
                    mNestedYOffset += mScrollOffset[1];
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "MotionEvent.ACTION_CANCEL... ");
                if (mIsBeingDragged) {
                    if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0,
                            getScrollRange())) {
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                }
                endDrag();
                break;
            default:
                break;
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return super.onTouchEvent(vtev);
    }

    boolean overScrollByCompat(int deltaX, int deltaY,
                               int scrollX, int scrollY,
                               int scrollRangeX, int scrollRangeY,
                               int maxOverScrollX, int maxOverScrollY) {
        final int overScrollMode = getOverScrollMode();
        final boolean canScrollVertical =
                computeVerticalScrollRange() > computeVerticalScrollExtent();
        final boolean overScrollVertical = overScrollMode == View.OVER_SCROLL_ALWAYS
                || (overScrollMode == View.OVER_SCROLL_IF_CONTENT_SCROLLS && canScrollVertical);

        int newScrollY = scrollY + deltaY;

        if (!overScrollVertical) {
            maxOverScrollY = 0;
        }

        final int top = -maxOverScrollY;
        final int bottom = maxOverScrollY + scrollRangeY;
        Log.d(TAG, "newScrollY: " + newScrollY + ", top: " + top + ", bottom: " + bottom);

        boolean clampedY = false;
        if (newScrollY > bottom) {
            newScrollY = bottom;
            clampedY = true;
        } else if (newScrollY < top) {
            newScrollY = top;
            clampedY = true;
        }
        if (clampedY && !hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
            mScroller.springBack(0, newScrollY, 0, 0, 0, getScrollRange());
        }
        // onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);
        return clampedY;
    }

    int getScrollRange() {
        int parentSpace = getHeight() - getPaddingTop() - getPaddingBottom();
        return Math.max(0, parentSpace);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            Log.d(TAG, "computeScroll: mScroller.computeScrollOffset() = true");
            final int y = mScroller.getCurrY();
            int unconsumed = y - mLastScrollerY;
            mScrollConsumed[1] = 0;
            dispatchNestedPreScroll(0, unconsumed, mScrollConsumed, null,
                    ViewCompat.TYPE_NON_TOUCH);
            unconsumed -= mScrollConsumed[1];
            final int x = mScroller.getCurrX();

            Log.d(TAG, "computeScroll: x: " + x + ", y: " + y + ", unconsumed: " + unconsumed);

            if (unconsumed != 0) {
                int scrollY = getScrollY();

                int dyUnConsumed = 0;
                int consumedY = unconsumed;

                if (scrollY == 0) {
                    dyUnConsumed = unconsumed;
                    consumedY = 0;
                } else if (scrollY + unconsumed < 0) {
                    dyUnConsumed = unconsumed + scrollY;
                    consumedY = -scrollY;
                }

                dispatchNestedScroll(0, consumedY, 0, dyUnConsumed, null,
                        ViewCompat.TYPE_NON_TOUCH, mScrollConsumed);
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

    /**
     * 快速滑动
     *
     * @param velocityY Y轴滑动距离
     */
    public void fling(int velocityY) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH);
        mScroller.fling(getScrollX(), getScrollY(), // start
                0, velocityY, // velocities
                0, 0, // x
                Integer.MIN_VALUE, Integer.MAX_VALUE, // y
                0, 0); // overscroll
        runAnimatedScroll();
    }

    private void runAnimatedScroll() {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH);
        mLastScrollerY = getScrollY();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void abortAnimatedScroll() {
        mScroller.abortAnimation();
        stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
    }

    private void endDrag() {
        recycleVelocityTracker();
        stopNestedScroll(ViewCompat.TYPE_TOUCH);
    }
}
