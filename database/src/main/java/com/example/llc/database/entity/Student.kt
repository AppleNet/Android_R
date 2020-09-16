package com.example.llc.database.entity;

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Student {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "name")
    lateinit var name: String

    @ColumnInfo(name = "age")
    var age: Int = 0

}
