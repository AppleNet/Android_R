package com.example.llc.android_r.coordinatorlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * com.example.llc.android_r.coordinatorlayout.FollowBehavior
 *
 * @author liulongchao
 * @since 2024/1/31
 */
public class FollowBehavior extends CoordinatorLayout.Behavior<View> {

    public FollowBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof DependencyView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        child.setX(dependency.getX());
        child.setY(dependency.getY() + 200);
        return true;
    }
}
