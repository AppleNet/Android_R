package com.example.llc.database.db;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.llc.database.dao.StudentDao
import com.example.llc.database.entity.Student

@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class StudentDb: RoomDatabase() {


    abstract fun getStudentDao(): StudentDao

    companion object{
        private var INSTANCE: StudentDb? = null

        fun getDataBase(context: Context): StudentDb {
            if (INSTANCE == null) {
                synchronized(StudentDb::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context, StudentDb::class.java, "student_database.db")
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }

        fun getDataBase(): StudentDb? = INSTANCE
    }

}

