package com.example.llc.android_r.scroll.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.llc.android_r.R

/**
 * com.example.llc.android_r.scroll.common.RecyclerViewFragment
 * @author liulongchao
 * @since 2024/1/4
 */
class RecyclerViewFragment: Fragment() {

    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recyclerview_view, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerAdapter(getData())
        recyclerView?.adapter = adapter
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val THRESHOLD_LOAD_MORE = 3
            var hasLoadMore = false
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hasLoadMore = false
                }
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING && !hasLoadMore) {
                    val lastPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val offset = recyclerView.adapter?.itemCount?.minus(lastPosition)?.minus(1)
                    if (offset != null && offset <= THRESHOLD_LOAD_MORE) {
                        hasLoadMore = true
                        adapter.data.addAll(getData())
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
        return view
    }


    private fun getData(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0..10) {
            list.add("ChildView item $i")
        }
        return list
    }
}