package com.example.llc.android_r.scroll.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.llc.android_r.R

/**
 * com.example.llc.android_r.scroll.common.RecyclerAdapter
 * @author liulongchao
 * @since 2024/1/4
 */
class RecyclerAdapter(val data: ArrayList<String>): RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
       holder.bind(data[position])
    }

    class RecyclerViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(data: String) {
            view.findViewById<TextView>(R.id.text).text = data
        }
    }
}