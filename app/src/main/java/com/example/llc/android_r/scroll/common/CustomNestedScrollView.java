package com.example.llc.android_r.scroll.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * com.example.llc.android_r.scroll.common.CustomNestedScrollView
 *
 * @author liulongchao
 * @since 2024/1/4
 */
public class CustomNestedScrollView extends NestedScrollView {

    private View contentView;
    private View topView;

    public CustomNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.e("NestedScrollLayout", "onStartNestedScroll");
        return super.onStartNestedScroll(child, target, axes, type);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        Log.e("NestedScrollLayout", "onStartNestedScroll");
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.e("NestedScrollLayout", "onNestedScrollAccepted");
        super.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        Log.e("NestedScrollLayout", "onNestedScrollAccepted");
        super.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        // 我们需要处理不可滑动的区域的可见高度
        Log.e("NestedScrollLayout", getScrollY()+" ::onNestedPreScroll:: "+topView.getMeasuredHeight());
        boolean hideTop = dy > 0 && getScrollY() < topView.getMeasuredHeight();
        if (hideTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 根据 xml 的层级结构，我们需要获取的是第一个子 View 下的 第二个子 View
        topView = ((ViewGroup) getChildAt(0)).getChildAt(0);
        contentView = ((ViewGroup) getChildAt(0)).getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 重新设置 contentView 的高度为屏幕高度
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.height = getMeasuredHeight();
        contentView.setLayoutParams(layoutParams);
    }

//    private RecyclerView getRecyclerView(ViewGroup viewGroup) {
//        for (int i = 0; i < viewGroup.getChildCount(); i++) {
//            View child = viewGroup.getChildAt(i);
//            if (child instanceof RecyclerView && child.getClass() == DisabledRecyclerView.class) {
//                return (RecyclerView) child;
//            } else if (child instanceof ViewGroup) {
//                // 递归调用
//                RecyclerView recyclerView = getRecyclerView((ViewGroup) child);
//                if (recyclerView != null) {
//                    return recyclerView;
//                }
//            }
//        }
//        return null;
//    }
}
