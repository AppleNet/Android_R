package com.example.llc.android_r.vp.subvp;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.llc.android_r.R;
import com.example.llc.android_r.vp.BaseLazyFragment;
import java.util.ArrayList;

/**
 * 同学们，Fragment2 T2 里面的 Fragment 【的确一点绕，需要同学们理解下】
 */
public class Fragment2 extends BaseLazyFragment {

    private static final String TAG = "Fragment2";

    private ViewPager viewPager;  //对应的viewPager
    private ArrayList<Fragment> fragmentsList;//view数组


    public static Fragment newIntance() {
        Fragment2 fragment = new Fragment2();
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_2_vp;
    }

    @Override
    protected void initView(View view) {
        viewPager = view.findViewById(R.id.viewpager01);

        fragmentsList = new ArrayList<>();

        // 又加载四个 子Fragment
        fragmentsList.add(Fragment2_vp_1.newIntance());
        fragmentsList.add(Fragment2_vp_2.newIntance());
        fragmentsList.add(Fragment2_vp_3.newIntance());
        fragmentsList.add(Fragment2_vp_4.newIntance());

        /**
         * 实例化一个PagerAdapter
         * 必须重写的两个方法
         * getCount
         * getItem
         */
        PagerAdapter pagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentsList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentsList.size();
            }
        };

        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + "SubFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onResume: " + "SubFragment");
    }
}
