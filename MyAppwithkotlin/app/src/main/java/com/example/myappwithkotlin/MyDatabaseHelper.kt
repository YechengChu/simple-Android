package com.example.myappwithkotlin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context, name: String, version: Int)
    : SQLiteOpenHelper(context, name, null, version){
    override fun onCreate(db: SQLiteDatabase) {
        // 第一次使用数据库时自动建表
        db.execSQL("CREATE TABLE news_inf(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "news_title VARCHAR(50), news_content VARCHAR(255))")
    } // onCreate
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        println("----------onUpdate Called----------" + oldVersion + "--->" + newVersion)
        // 当数据库发生更新时，在此处更新数据库的表
    } // onUpgrade
} // MyDatabaseHelper