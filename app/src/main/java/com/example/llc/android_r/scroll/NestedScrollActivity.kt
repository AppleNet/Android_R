package com.example.llc.android_r.scroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.llc.android_r.R
import com.example.llc.android_r.scroll.common.RecyclerViewFragment
import com.example.llc.android_r.scroll.common.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_nested_scroll.swipe_refresh_layout
import kotlinx.android.synthetic.main.activity_nested_scroll.tablayout
import kotlinx.android.synthetic.main.activity_nested_scroll.viewpager_view

/**
 * com.example.llc.android_r.scroll.NestedScrollActivity
 * @author liulongchao
 * @since 2024/1/4
 */
class NestedScrollActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_custom)
//        setContentView(R.layout.activity_nested_scroll)
//        val viewPagerAdapter = ViewPagerAdapter(this, getFragments())
//        viewpager_view.adapter = viewPagerAdapter
//        val labels = arrayOf("linear", "scroll", "recycler")
//        val tabLayoutMediator = TabLayoutMediator(tablayout, viewpager_view) { tab, position -> tab.text = labels[position] }
//        tabLayoutMediator.attach()
//        swipe_refresh_layout.setOnRefreshListener {
//            swipe_refresh_layout.postDelayed({
//                swipe_refresh_layout.isRefreshing = false
//            }, 500)
//        }
    }

    private fun getFragments(): ArrayList<Fragment> {
        val list = ArrayList<Fragment>()
        list.add(RecyclerViewFragment())
        list.add(RecyclerViewFragment())
        list.add(RecyclerViewFragment())
        return list
    }
}