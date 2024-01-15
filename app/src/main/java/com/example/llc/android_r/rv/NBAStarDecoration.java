package com.example.llc.android_r.rv;

import static com.example.base.utils.FlowLayout.dp2px;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * com.example.llc.android_r.rv.NBAStarDecoration
 *
 * @author liulongchao
 * @since 2024/1/14
 */
public class NBAStarDecoration extends RecyclerView.ItemDecoration {

    private Paint paint;
    private Paint textPaint;
    private Rect textRect;

    NBAStarDecoration(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30);
        textRect = new Rect();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof NBAStarAdapter) {
            NBAStarAdapter nbaStarAdapter = (NBAStarAdapter) adapter;
            int childCount = parent.getChildCount();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            for (int i = 0; i < childCount; i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildLayoutPosition(view);
                boolean groupHeader = nbaStarAdapter.isGroupHeader(position);
                if (groupHeader && view.getTop() - dp2px(50) - parent.getPaddingTop() >= 0) {
                    canvas.drawRect(new Rect(left, view.getTop() - dp2px(50), right, view.getTop()), paint);
                    String groupName = nbaStarAdapter.getGroupName(position);
                    textPaint.getTextBounds(groupName, 0 , groupName.length(), textRect);
                    // 文字中心绘制，上一层有讲解
                    // float centerHeight = view.getTop() - dp2px(50)/2 + textRect.height() / 2;
                    Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
                    float centerHeight = view.getTop() - dp2px(50)/2 + ((fontMetrics.descent - fontMetrics.ascent)/2) - fontMetrics.descent;
                    canvas.drawText(groupName, left + 20, centerHeight, textPaint);
                }
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof NBAStarAdapter) {
            NBAStarAdapter nbaStarAdapter = (NBAStarAdapter) adapter;
            // 获取屏幕可见的第一个 View 的位置
            int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
            // 获取这个位置的 itemView
            View view = parent.findViewHolderForLayoutPosition(position).itemView;
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = parent.getPaddingTop();
            boolean groupHeader = nbaStarAdapter.isGroupHeader(position + 1);
            if (groupHeader) {
                // 判断是不是 itemView 的高度比 头部的高度小
                int bottom = Math.min(dp2px(50), view.getBottom() - parent.getPaddingTop());
                // 区域绘制
                canvas.drawRect(left, top, right, bottom + top, paint);
                // 文字绘制
                String groupName = nbaStarAdapter.getGroupName(position);
                textPaint.getTextBounds(groupName, 0 , groupName.length(), textRect);
                if (top + bottom - dp2px(50) / 2 - parent.getPaddingTop() >= 0) {
                    canvas.drawText(groupName, left + 20, top + bottom - dp2px(50) / 2 + textRect.height() / 2, textPaint);
                }
            } else {
                // 区域绘制
                canvas.drawRect(left, top, right, top + dp2px(50), paint);
                // 文字绘制
                String groupName = nbaStarAdapter.getGroupName(position);
                textPaint.getTextBounds(groupName, 0 , groupName.length(), textRect);
                canvas.drawText(groupName, left + 20, top + dp2px(50) / 2 + textRect.height() / 2 , textPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof NBAStarAdapter) {
            NBAStarAdapter nbaStarAdapter = (NBAStarAdapter) adapter;
            int position = parent.getChildLayoutPosition(view);
            boolean groupHeader = nbaStarAdapter.isGroupHeader(position);
            if (groupHeader) {
                outRect.set(0, dp2px(50),0, 0);
            } else {
                outRect.set(0, 1, 0 , 0);
            }
        }
    }
}
