package com.example.llc.android_r.tabs.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.llc.android_r.R

/**
 * com.example.llc.android_r.tabs.adapter.TabCenterAdapter
 * @author liulongchao
 * @since 2024/5/16
 */
class TabCenterAdapter(var context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /** 使用头和尾空视图占位，来达到实际意义的view可以居中于屏幕 */
    private val EMPTY_VIEW_COUNT = 2
    /** 使用头和尾空视图占位，来达到实际意义的view可以居中于屏幕 */
    private val EMPTY_VIEW_CODE = -1
    /** 填充子View的宽度 */
    private var placeHolderWidth: Int = 0
    /** 当前需要高亮显示的 view 的位置 */
    private var centerIndex = 1
    /** 将高亮的 item 恢复为默认状态 */
    private var lastIndex = 1
    /** rv */
    private var mRecyclerView: RecyclerView? = null
    /** 点击回调 */
    private var onCenterTabPositionCallback: View.OnClickListener? = null
    private val tabs = listOf("拍照搜题", "拍照判卷")
    init {
        placeHolderWidth = dp2px(100)
    }

    fun setCenterIndex(index: Int) {
        if (index == centerIndex) {
            return
        }
        lastIndex = centerIndex
        centerIndex = index
        notifyItemChanged(lastIndex)
        notifyItemChanged(centerIndex)
    }

    fun bindRecyclerViewScrollListener(recyclerView: RecyclerView) {
        this.mRecyclerView = recyclerView
        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(recyclerView)
        onCenterTabPositionCallback = View.OnClickListener { v ->
            mRecyclerView?.let {
                val snapDistance = it.layoutManager?.let { it1 ->
                    linearSnapHelper.calculateDistanceToFinalSnap(it1, v!!)
                }
                it.smoothScrollBy(snapDistance?.get(0) ?: 0, snapDistance?.get(0) ?: 0)
            }
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val manager = recyclerView.layoutManager
                    if (manager is LinearLayoutManager) {
                        val snapView = linearSnapHelper.findSnapView(recyclerView.layoutManager)
                        val childLayoutPosition = snapView?.let {
                            recyclerView.getChildLayoutPosition(it)
                        }
                        childLayoutPosition?.let {
                            setCenterIndex(it)
                        }
                    }
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == EMPTY_VIEW_CODE) {
            val view = View(context)
            val viewWidth = parent.measuredWidth / 2 - placeHolderWidth / 2
            val layoutParams = RecyclerView.LayoutParams(viewWidth, RecyclerView.LayoutParams.WRAP_CONTENT)
            view.layoutParams = layoutParams
            return PlaceHolderViewHolder(view)
        }
        val view = LayoutInflater.from(context).inflate(R.layout.view_rc_item_layout, parent, false)
        return UnionViewHolder(view)
    }

    override fun getItemCount(): Int = EMPTY_VIEW_COUNT + 2

    override fun getItemViewType(position: Int): Int {
        if (position == 0 || position == itemCount - 1) {
            return EMPTY_VIEW_CODE
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UnionViewHolder) {
            val textView = holder.textView
            holder.rootView.setOnClickListener(onCenterTabPositionCallback)
            textView.text = tabs[position - 1]
            if (position == centerIndex) {
                textView.setTextColor(Color.WHITE)
            } else {
                textView.setTextColor(Color.GRAY)
            }
        }
    }

    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}