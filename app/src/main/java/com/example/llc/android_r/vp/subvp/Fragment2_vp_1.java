package com.example.llc.android_r.vp.subvp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.llc.android_r.R;
import com.example.llc.android_r.vp.BaseLazyFragment;

// 同学们：这是T2  嵌套了一层 ViewPager的Fragment2_vp_1
public class Fragment2_vp_1 extends BaseLazyFragment {

    private static final String TAG = "Fragment2_vp_1";

    public static Fragment newIntance() {
        Fragment2_vp_1 fragment = new Fragment2_vp_1();
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_vp_1;
    }

    @Override
    protected void initView(View view) {}


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onFragmentLoadStop() {
        super.onFragmentLoadStop();
        Log.d(TAG, "onFragmentLoadStop" + " 停止一切更新");
    }

    @Override
    public void onFragmentLoad() {
        super.onFragmentLoad();
        Log.d(TAG, "onFragmentLoad" + " 真正更新数据");
    }
}
