package com.example.myappwithkotlin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context, name: String, version: Int)
    : SQLiteOpenHelper(context, name, null, version){
    private val STUDENTS_TABLE_NAME = "students"
    private val CREATE_DB_TABLE =
        " CREATE TABLE " + STUDENTS_TABLE_NAME +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " name TEXT NOT NULL, " +
                " grade TEXT NOT NULL);"
    override fun onCreate(db: SQLiteDatabase) {
        // 第一次使用数据库时自动建表
        db.execSQL(CREATE_DB_TABLE)
    } // onCreate
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $STUDENTS_TABLE_NAME")
        onCreate(db)
    } // onUpgrade
} // DatabaseHelper