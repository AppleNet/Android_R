package com.example.llc.android_r.vp;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 同学们大家好：专门用来显示 ViewPager Item 的适配器
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "MyFragmentPagerAdapter";

    private List<Fragment> fragmentList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> flist) {
        super(fm);
        fragmentList = flist;
    }
    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
