package com.example.llc.android_r.room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.llc.android_r.R
// import com.example.llc.database.db.StudentDb

/**
 * com.example.llc.android_r.room.RoomDbActivity
 * @author liulongchao
 * @since 2021/3/4
 */
class RoomDbActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_db)
        // StudentDb.getDataBase(this)
    }
}