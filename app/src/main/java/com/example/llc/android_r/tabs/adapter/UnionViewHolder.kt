package com.example.llc.android_r.tabs.adapter

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.llc.android_r.R

/**
 * com.example.llc.android_r.tabs.adapter.PlaceHolderViewHolder
 * @author liulongchao
 * @since 2024/5/16
 */
class UnionViewHolder(private var view: View): RecyclerView.ViewHolder(view) {

    val textView: TextView = view.findViewById(R.id.title)
    val rootView: FrameLayout = view.findViewById(R.id.rootView)
}