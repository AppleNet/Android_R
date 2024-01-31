package com.example.llc.android_r.coordinatorlayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * com.example.llc.android_r.coordinatorlayout.MyFragmentAdapter
 *
 * @author liulongchao
 * @since 2024/1/30
 */
public class MyFragmentAdapter extends FragmentStateAdapter {
    private List<Fragment> fragments;
    public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments == null ? 0:fragments.size();
    }
}
