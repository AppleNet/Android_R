package com.example.llc.android_r

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

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
            startActivity(Intent(this, ScopeStorageActivity::class.java))
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
