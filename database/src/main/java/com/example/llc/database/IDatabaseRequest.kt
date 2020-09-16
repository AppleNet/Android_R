package com.example.llc.database;

import com.example.llc.database.entity.Student

/**
 *  数据库操作接口 抽象实现
 *
 * */
interface IDatabaseRequest {

    /**
     *  插入操作
     *
     * @param student student
     * */
    fun insertStudents(vararg student: Student)

    /**
     *  更新操作
     *
     * @param student student
     * */
    fun updateStudents(vararg student: Student)

    /**
     *  删除操作
     *
     * @param student student
     * */
    fun deleteStudents(vararg student: Student)

    /**
     *  删除所有操作
     *
     * @param student student
     * */
    fun deleteAllStudent()

    /**
     *  查询所有操作
     *
     * @param student student
     * */
    fun queryAllStudent(): List<Student>?

}
