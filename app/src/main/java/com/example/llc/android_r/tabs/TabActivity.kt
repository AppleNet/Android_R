package com.example.llc.android_r.tabs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.llc.android_r.R
import com.example.llc.android_r.tabs.adapter.TabCenterAdapter

/**
 * com.example.llc.android_r.tabs.TabActivity
 * @author liulongchao
 * @since 2024/5/16
 */
class TabActivity: AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val adapter = TabCenterAdapter(this)
        recyclerView.adapter = adapter
        adapter.bindRecyclerViewScrollListener(recyclerView)
    }
}