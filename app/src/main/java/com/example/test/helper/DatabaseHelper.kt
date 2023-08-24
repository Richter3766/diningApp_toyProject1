package com.example.test.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.test.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 1

        // 테이블과 컬럼 이름 정의
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_USERS " +
                "($COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_AGE INTEGER)"

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun insertUser(user:User){
        val values = ContentValues()

        values.put(COLUMN_ID, user.id)
        values.put(COLUMN_NAME, user.name)
        values.put(COLUMN_AGE, user.age)

        val wd = writableDatabase
        wd.insert(TABLE_USERS, null, values)
        wd.close()

    }

    @SuppressLint("Recycle", "Range")
    fun selectUser():MutableList<User>{
        val list = mutableListOf<User>()

        val selectAll = "select * from users"

        val rd = readableDatabase
        val cursor = rd.rawQuery(selectAll, null)
        while(cursor.moveToNext()){
            val id= cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE))

            list.add(User(id, name, age))
        }
        cursor.close()
        rd.close()

        return list
    }
}
