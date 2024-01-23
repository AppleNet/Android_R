package com.example.llc.android_r.vp;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.llc.android_r.R;

/**
 * com.example.llc.android_r.vp.MyFragment
 *
 * @author liulongchao
 * @since 2024/1/21
 */
public class MyFragment extends BaseLazyFragment {
    private static final String TAG = "MyFragment";
    public static final String POSITION = "Position";

    private ImageView imageView;
    private TextView textView;
    private int tabIndex;

    public static MyFragment newInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("Position", position);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        Log.d(TAG, tabIndex + " fragment " + "initView: " );
        tabIndex = getArguments().getInt(POSITION);
        imageView = view.findViewById(R.id.iv_content);
        textView = view.findViewById(R.id.tv_loading);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_vp;
    }

    @Override
    public void onFragmentLoad() {
        super.onFragmentLoad();
        textView.setText("startLoad");
    }

    @Override
    public void onFragmentLoadStop() {
        super.onFragmentLoadStop();
        textView.setText("stopLoad");
    }
}