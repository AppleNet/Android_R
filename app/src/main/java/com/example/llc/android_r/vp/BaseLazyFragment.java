package com.example.llc.android_r.vp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

/**
 * com.example.llc.android_r.vp.BaseLazyFragment
 *
 * @author liulongchao
 * @since 2024/1/22
 */
public abstract class BaseLazyFragment extends Fragment {

    private static final String TAG = "MyFragment";
    private View rootView = null;
    private boolean isViewCreated = false;
    private boolean isVisibleStateUp = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView: ");
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false);
        }
        initView(rootView);
        isViewCreated  = true;
        if (getUserVisibleHint()) {
            setUserVisibleHint(true);
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG,"setUserVisibleHint: ");
        if (isViewCreated) {
            if (isVisibleToUser && !isVisibleStateUp) {
                // 当前可见，但是上一次不可见
                dispatchVisibleHint(true);
            } else if (!isVisibleToUser && isVisibleStateUp) {
                // 当前不可见，但是上一次可见
                dispatchVisibleHint(false);
            }
        }
    }

    private void dispatchVisibleHint(boolean isVisibleToUser) {
        isVisibleStateUp = isVisibleToUser;
        if (isVisibleStateUp && isParentVisible()) {
            return;
        }
        if (isVisibleToUser) {
            // 加载数据
            onFragmentLoad();
            // 手动 分发嵌套执行
            dispatchChildVisibleState(true);
        } else {
            // 停掉数据
            onFragmentLoadStop();
            dispatchChildVisibleState(false);
        }
    }

    protected boolean isParentVisible() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BaseLazyFragment) {
            BaseLazyFragment baseLazyFragment = (BaseLazyFragment) parentFragment;
            return !baseLazyFragment.isVisibleStateUp;
        }
        return false;
    }

    protected void dispatchChildVisibleState(boolean isVisibleToUser) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof BaseLazyFragment
                        && !fragment.isHidden()
                        && fragment.getUserVisibleHint()) {
                    ((BaseLazyFragment) fragment).dispatchVisibleHint(isVisibleToUser);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        if (getUserVisibleHint() && !isVisibleStateUp) {
            // 当前可见，并且上一次不可见的时候 分发
            dispatchVisibleHint(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG,"onPause");
        if (!getUserVisibleHint() && isVisibleStateUp) {
            dispatchVisibleHint(false);
        }
    }

    // 让子类完成，初始化布局，初始化控件
    protected abstract void initView(View rootView);
    protected abstract int getLayoutRes();

    public void onFragmentLoadStop() {
        Log.e(TAG, "onFragmentLoadStop");
    }

    public void onFragmentLoad() {
        Log.e(TAG,"onFragmentLoad");
    }
}
