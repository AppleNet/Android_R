package com.example.llc.android_r.scroll.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * com.example.llc.android_r.scroll.common.ViewPagerAdapter
 * @author liulongchao
 * @since 2024/1/4
 */
class ViewPagerAdapter(private val fragmentActivity: FragmentActivity, val data: List<Fragment>): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment = data[position]
}