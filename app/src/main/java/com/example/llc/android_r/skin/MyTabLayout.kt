package com.example.llc.android_r.skin

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.example.llc.android_r.R
//import com.example.skin.SkinCustomViewSupport
//import com.example.skin.SkinResources
import com.google.android.material.tabs.TabLayout

/**
 * com.example.llc.android_r.skin.MyTabLayout
 *
 * @author liulongchao
 * @since 2024/1/2
 */
class MyTabLayout : TabLayout/*, SkinCustomViewSupport*/ {

    var tabIndicatorColorResId = 0
    var tabTextColorResId = 0
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr) {
        val typeArrayed = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,
            defStyleAttr, 0)
        tabIndicatorColorResId = typeArrayed.getResourceId(R.styleable.TabLayout_tabIndicatorColor, 0)
        tabTextColorResId = typeArrayed.getResourceId(R.styleable.TabLayout_tabTextColor, 0)
        typeArrayed.recycle()
    }

//    override fun applySkin() {
//        if (tabIndicatorColorResId != 0) {
//            val tabIndicatorColor: Int =
//                SkinResources.getInstance().getColor(tabIndicatorColorResId)
//            setSelectedTabIndicatorColor(tabIndicatorColor)
//        }
//
//        if (tabTextColorResId != 0) {
//            val tabTextColor: ColorStateList =
//                SkinResources.getInstance().getColorStateList(tabTextColorResId)
//            tabTextColors = tabTextColor
//        }
//    }

}