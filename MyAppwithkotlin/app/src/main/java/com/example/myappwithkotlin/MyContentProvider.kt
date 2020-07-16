package com.example.myappwithkotlin

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class MyContentProvider: ContentProvider() {

    // 第一次创建ContentProvider时回调该方法
    override fun onCreate(): Boolean {
        println("=====ContentProvider的onCreate方法被调用=====")
        return true
    } // onCreate

    // 实现插入方法，该方法应返回新插入的记录的Uri
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        println("$uri=====insert方法被调用=====")
        println("values参数为：$values")
        return null
    } // insert

    // 实现查询方法，该方法应该返回查询得到的Cursor
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        println("$uri=====query方法被调用=====")
        println("selection参数为：$selection")
        return null
    } // query

    // 实现更新方法，该方法应该返回被更新的记录条数
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        println("$uri=====update方法被调用=====")
        println("selection参数为：$selection，values参数为：$values")
        return 0
    } // update

    // 实现删除方法，该方法应该返回被删除的记录条数
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        println("$uri=====delete方法被调用=====")
        println("selection参数为：$selection")
        return 0
    } // delete

    // 该方法的返回值代表了此ContentProvider所提供数据的MIME类型
    override fun getType(uri: Uri): String? {
        return null
    } // getType
} // MyContentProvider