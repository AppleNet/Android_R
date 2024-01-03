package com.example.llc.android_r.skin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.llc.android_r.R
import com.example.llc.android_r.skin.fragment.MusicFragment
import com.example.llc.android_r.skin.fragment.RadioFragment
import com.example.llc.android_r.skin.fragment.VideoFragment
import com.example.skin.SkinManager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_skin.changSkin

/**
 * com.example.llc.android_r.skin.SkinActivity
 * @author liulongchao
 * @since 2024/1/2
 */
class SkinActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // factory 在这里设置，设置完之后，才会走 setContentView
        setContentView(R.layout.activity_skin)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val list = ArrayList<Fragment>()
        list.add(MusicFragment())
        list.add(RadioFragment())
        list.add(VideoFragment())
        val listTitle = ArrayList<String>()
        listTitle.add("音乐")
        listTitle.add("视频")
        listTitle.add("电台")
        val myFragmentPagerAdapter = MyFragmentPagerAdapter(getSupportFragmentManager(), list, listTitle)
        viewPager.adapter = myFragmentPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        changSkin.setOnClickListener {
            SkinManager.getInstance().loadSkin("/data/data/com.example.llc.android_r/skin/skin-debug.apk")
        }
    }

}