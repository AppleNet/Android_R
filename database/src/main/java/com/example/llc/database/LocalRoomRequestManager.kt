package com.example.llc.database;

import com.example.llc.database.dao.StudentDao
import com.example.llc.database.db.StudentDb
import com.example.llc.database.entity.Student

class LocalRoomRequestManager: ILocalRequest, IDatabaseRequest {

    var studentDao: StudentDao? = null

    init{
        val studentDb: StudentDb? = StudentDb.getDataBase()
        studentDao = studentDb?.getStudentDao()
    }

    companion object{

        private var INSTANCE: LocalRoomRequestManager? = null

        fun getInstance(): LocalRoomRequestManager {
            if (INSTANCE == null) {
                synchronized(LocalRoomRequestManager::class) {
                    if (INSTANCE == null) {
                        INSTANCE = LocalRoomRequestManager()
                    }
                }
            }
            return INSTANCE!!
        }
    }


    override fun insertStudents(vararg student: Student) {
        studentDao?.insertStudent(*student)
    }

    override fun updateStudents(vararg student: Student) {
        studentDao?.updateStudent(*student)
    }

    override fun deleteStudents(vararg student: Student) {
        studentDao?.deleteStudent(*student)
    }

    override fun deleteAllStudent() {
        studentDao?.deleteAllStudents()
    }

    override fun queryAllStudent(): List<Student>? {
        return studentDao?.queryAllStudents()
    }
}
