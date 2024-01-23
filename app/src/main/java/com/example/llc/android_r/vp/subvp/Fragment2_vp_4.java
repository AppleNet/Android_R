package com.example.llc.android_r.vp.subvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.llc.android_r.R;
import com.example.llc.android_r.vp.BaseLazyFragment;

public class Fragment2_vp_4 extends BaseLazyFragment {

    public static Fragment newIntance() {
        Fragment2_vp_4 fragment = new Fragment2_vp_4();
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_vp_4;
    }

    @Override
    protected void initView(View view) { }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
