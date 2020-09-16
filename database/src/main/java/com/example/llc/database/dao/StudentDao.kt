package com.example.llc.database.dao;

import androidx.room.*
import com.example.llc.database.entity.Student

@Dao
interface StudentDao {

    @Insert
    fun insertStudent(vararg student: Student)

    @Update
    fun updateStudent(vararg student: Student)

    @Delete
    fun deleteStudent(vararg student: Student)

    @Query(value = "DELETE FROM student")
    fun deleteAllStudents()

    @Query(value = "SELECT * FROM student ORDER BY ID DESC")
    fun queryAllStudents(): List<Student>



}
