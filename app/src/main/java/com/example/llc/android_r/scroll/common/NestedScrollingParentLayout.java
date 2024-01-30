package com.example.llc.android_r.scroll.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;

/**
 * com.example.llc.android_r.scroll.common.NestedScrollingParentLayout
 *
 * @author liulongchao
 * @since 2024/1/23
 */
public class NestedScrollingParentLayout extends LinearLayout implements NestedScrollingParent {

    private static String TAG = "Mars";
    private NestedScrollingParentHelper helper;

    public NestedScrollingParentLayout(Context context) {
        super(context);
        init();
    }

    public NestedScrollingParentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedScrollingParentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        init();
    }

    private void init() {
        helper = new NestedScrollingParentHelper(this);
    }
    int realHeight = 0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        realHeight = 0;
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        for(int i = 0; i < getChildCount(); i++){
            View view = getChildAt(i);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec),MeasureSpec.UNSPECIFIED);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
            realHeight += view.getMeasuredHeight();
        }
        Log.i(TAG,"realHeight: " + realHeight);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        Log.i(TAG, "onStartNestedScroll--" + "child:" + child + ",target:" + target + ",nestedScrollAxes:" + nestedScrollAxes);
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        Log.i(TAG, "onNestedScrollAccepted" + "child:" + child + ",target:" + target + ",nestedScrollAxes:" + nestedScrollAxes);
        helper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        Log.i(TAG, "onStopNestedScroll--target:" + target);
        helper.onStopNestedScroll(target);
    }

//    @Override
//    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
//        Log.i(TAG, "onNestedScroll--" + "target:" + target + ",dxConsumed" + dxConsumed + ",dyConsumed:" + dyConsumed
//                + ",dxUnconsumed:" + dxUnconsumed + ",dyUnconsumed:" + dyUnconsumed);
//    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i(TAG, "onNestedScroll--" + "target:" + target + ",dxConsumed" + dxConsumed + ",dyConsumed:" + dyConsumed
                + ",dxUnconsumed:" + dxUnconsumed + ",dyUnconsumed:" + dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        boolean show = showImg(dy);
        boolean hide = hideImg(dy);
        if(show || hide) {
            consumed[1] = dy; // 全部消费
            scrollBy(0, dy);
            Log.i(TAG,"Parent滑动："+dy);
        }
        Log.i(TAG, "onNestedPreScroll--getScrollY():" + getScrollY() + ",dx:" + dx + ",dy:" + dy + ",consumed:" + consumed[1]);
    }

    private boolean showImg(int dy) {
        View view = getChildAt(0);
        Log.i(TAG, "showImg: " + dy + " height: "
                + view.getHeight() + " getScrollY: " + getScrollY()
                + " can: " + view.canScrollVertically(-1));
        if(dy < 0 && getScrollY() > 0 && !view.canScrollVertically(-1)) {
            return true;
        }
        return false;
    }

    private boolean hideImg(int dy) {
        View view = getChildAt(0);
        Log.i(TAG,"showImg: " + dy + " height: " + view.getHeight() + " getScrollY: " + getScrollY() + " can: " + view.canScrollVertically(-1));
        if(dy > 0 && getScrollY() < view.getHeight()){
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.i(TAG, "onNestedFling--target:" + target);
        return true;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.i(TAG, "onNestedPreFling--target:" + target);
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {
        View view = getChildAt(0);
        if (y < 0)
        {
            y = 0;
        }
        if (y > view.getHeight())
        {
            y = view.getHeight();
        }
        if (y != getScrollY())
        {
            super.scrollTo(x, y);
        }
    }
}
