package com.example.llc.android_r.scroll.common

import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * com.example.llc.android_r.scroll.common.DisabledRecyclerView
 * @author liulongchao
 * @since 2024/1/4
 */
class DisabledRecyclerView: RecyclerView {
    constructor(context: android.content.Context) : super(context) {
        init()
    }

    constructor(context: android.content.Context, attrs: android.util.AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: android.content.Context, attrs: android.util.AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        adapter = RecyclerAdapter(getBanner())
    }

    override fun onTouchEvent(e: MotionEvent?) = false

    override fun onInterceptTouchEvent(e: MotionEvent?) = false

    private fun getBanner(): ArrayList<String> {
        val data: ArrayList<String> = ArrayList()
        data.add("ParentView item 0")
        data.add("ParentView item 1")
        data.add("ParentView item 2")
        data.add("ParentView item 3")
        return data
    }
}