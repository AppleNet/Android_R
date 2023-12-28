package com.example.base.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * com.example.base.utils.FlowLayout
 *
 * @author liulongchao
 * @since 2023/12/27
 */
public class FlowLayout extends ViewGroup {

    private final int mHorizontalSpacing = dp2px(16); //每个item横向间距
    private final int mVerticalSpacing = dp2px(8); //每个item横向间距
    private List<List<View>> allViews = new ArrayList<>(); // 记录所有行，用来layout
    private List<Integer> lineHeights = new ArrayList<>(); // 记录每行的高度，用来layout

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void clearMeasureParams() {
        allViews.clear();
        lineHeights.clear();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
       int lineCount = allViews.size();
       int paddingLeft =  getPaddingLeft();
       int paddingTop = getPaddingTop();
       for (int i = 0; i < lineCount; i++) {
            List<View> lineViews = allViews.get(i);
            int lineHeight = lineHeights.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View view = lineViews.get(j);
                int left = paddingLeft;
                int top = paddingTop;
                int right = left + view.getMeasuredWidth();
                int bottom = top + view.getMeasuredHeight();
                view.layout(left, top, right, bottom);
                paddingLeft = right + mHorizontalSpacing;
            }
            paddingTop = paddingTop + lineHeight + mVerticalSpacing;
            paddingLeft = getPaddingLeft();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        clearMeasureParams();

        // 先测量子View
        int childCount = getChildCount();
        // 获取子View的左 padding
        int paddingLeft = getPaddingLeft();
        // 获取子View的右 padding
        int paddingRight = getPaddingRight();
        // 获取子View的上 padding
        int paddingTop = getPaddingTop();
        // 获取子View的下 padding
        int paddingBottom = getPaddingBottom();

        int selfWidth = MeasureSpec.getSize(widthMeasureSpec); // ViewGroup解析父容器传递过来的宽度
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec); // ViewGroup解析父容器传递过来的高度
        List<View> lineViews = new ArrayList<>(); // 记录每行显示的所有View
        int lineWidthUsed = 0; // 记录当前行已经使用的宽度
        int lineHeightUsed = 0; // 记录当前行已经使用的高度
        int parentNeedWidth = 0; // measure过程中，子View要求的父ViewGroup的宽
        int parentNeedHeight = 0; // measure过程中，子View要求的父ViewGroup的高


        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = child.getLayoutParams();
            if(child.getVisibility() != View.GONE) {

                int childWidthMeasureSpec =
                        getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, layoutParams.width);
                int childHeightMeasureSpec =
                        getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, layoutParams.height);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                // 获取测量的子View的宽度
                int measuredWidth = child.getMeasuredWidth();
                // 获取测量的子View的高度
                int measuredHeight = child.getMeasuredHeight();
                // 判断是否需要换行
                if (measuredWidth + lineWidthUsed + mHorizontalSpacing > selfWidth ) {
                    allViews.add(lineViews);
                    lineHeights.add(lineHeightUsed);
                    // 换行，数据清除
                    parentNeedWidth = Math.max(parentNeedWidth, lineWidthUsed + mHorizontalSpacing);
                    parentNeedHeight = parentNeedHeight + lineHeightUsed + mVerticalSpacing;
                    lineViews = new ArrayList<>();
                    lineWidthUsed = 0;
                    lineHeightUsed = 0;
                }
                lineViews.add(child);
                lineWidthUsed = lineWidthUsed + measuredWidth + mHorizontalSpacing;
                lineHeightUsed = Math.max(lineHeightUsed, measuredHeight);
                // 判断是否是最后一行
                if (i == childCount - 1) {
                    allViews.add(lineViews);
                    lineHeights.add(lineHeightUsed);
                    parentNeedWidth = Math.max(parentNeedWidth, lineWidthUsed + mHorizontalSpacing);
                    parentNeedHeight = parentNeedHeight + lineHeightUsed + mVerticalSpacing;
                }
            }
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int realWidth = (widthMode == MeasureSpec.EXACTLY) ? selfWidth : parentNeedWidth;
        int realHeight = (heightMode == MeasureSpec.EXACTLY) ? selfHeight : parentNeedHeight;
        // 再测量自己的高度
        setMeasuredDimension(realWidth, realHeight);
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
