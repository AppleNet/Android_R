package com.example.llc.android_r.coordinatorlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.example.llc.android_r.R;

/**
 * com.example.llc.android_r.coordinatorlayout.DependencyView
 *
 * @author liulongchao
 * @since 2024/1/31
 */
public class DependencyView extends androidx.appcompat.widget.AppCompatImageView {

    private float mLastX;
    private float mLastY;
    private final int mDragSlop;

    public DependencyView(Context context) {
        this(context, null);
    }

    public DependencyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DependencyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.kobe);
        mDragSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getX() - mLastX);
                int dy = (int) (event.getY() - mLastY);
                if (Math.abs(dx) > mDragSlop || Math.abs(dy) > mDragSlop) {
                    ViewCompat.offsetTopAndBottom(this, dy);
                    ViewCompat.offsetLeftAndRight(this, dx);
                }
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            default:
                break;

        }
        return true;
    }
}
