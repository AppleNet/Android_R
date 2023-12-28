package com.example.llc.android_r

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.llc.android_r.flowlayout.FlowLayoutActivity
import com.example.llc.android_r.room.RoomDbActivity
import com.example.llc.android_r.scope.FileArchitectureActivity
import com.example.llc.android_r.scope.ScopeStorageArchitectureActivity
import com.example.llc.android_r.scope.StorageAccessFrameworkActivity
import com.example.llc.android_r.screen.ScreenActivity
import com.example.llc.android_r.webview.WebViewActivity
import com.example.llc.android_r.webview.WebViewActivity1

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

/**
 * com.example.llc.android_r.ScopeStorageActivity
 * @author liulongchao
 * @since 2021/3/4
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        storage.setOnClickListener {
            // startActivity(Intent(this, ScopeStorageActivity::class.java))

        }

        screen.setOnClickListener {
            startActivity(Intent(this, ScreenActivity::class.java))
        }

        room.setOnClickListener {
            startActivity(Intent(this, RoomDbActivity::class.java))
        }

        saf.setOnClickListener {
            startActivity(Intent(this, StorageAccessFrameworkActivity::class.java))
        }

        arc.setOnClickListener {
            startActivity(Intent(this, ScopeStorageArchitectureActivity::class.java))
        }

        file.setOnClickListener {
            startActivity(Intent(this, FileArchitectureActivity::class.java))
        }

        webview.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }

        webview1.setOnClickListener {
            startActivity(Intent(this, WebViewActivity1::class.java))
        }
        flowLayout.setOnClickListener {
            startActivity(Intent(this, FlowLayoutActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}
