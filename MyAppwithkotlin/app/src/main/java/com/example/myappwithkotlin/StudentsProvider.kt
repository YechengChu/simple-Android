package com.example.myappwithkotlin

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.net.Uri


class StudentsProvider: ContentProvider() {
    // 用object 修饰的类为静态类，里面的方法和变量都为静态的
    // companion object 修饰为伴生对象,伴生对象在类中只能存在一个，类似于java中的静态方法
    companion object {
        val AUTHORITY = "com.example.providers.College"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/students")
        // 数据表的名称
        val STUDENTS_TABLE_NAME = "students"
        // 定义可操作的三个数据列
        val  _ID = "_id"
        val NAME = "name"
        val GRADE = "grade"
        // 其他数据库特定常量声明
        val DATABASE_NAME = "College"
        val DATABASE_VERSION = 1
        // 定义提供服务的2个Uri
        val STUDENTS = 1
        val STUDENT_ID = 2
        var uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init
        {
            uriMatcher.addURI(AUTHORITY, "students", STUDENTS)
            uriMatcher.addURI(AUTHORITY, "students/#", STUDENT_ID)
        } // init
    } // companion object

    private lateinit var dbHelper: DatabaseHelper

    // 第一次调用该StudentProvider的时，系统先创建StudentProvider对象，并回调该方法
    override fun onCreate(): Boolean {
        dbHelper = DatabaseHelper(this.context!!, DATABASE_NAME, DATABASE_VERSION)
        return true
    } // onCreate

    // 插入数据的方法
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        /**
         * 添加新学生记录
         */
        // 获取数据库实例
        val db = dbHelper.readableDatabase
        // 插入数据，返回插入记录的ID
        val rowID = db.insert(STUDENTS_TABLE_NAME, "", values)
        /**
         * 如果记录添加成功
         */
        if (rowID > 0) {
            // 在已有的Uri的后面追加ID
            val _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        } // if
        throw IllegalArgumentException("Unknown URI $uri")
    } // insert

    // 查询数据的方法
    @SuppressLint("Recycle")
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        // 获取数据库实例
        val db = dbHelper.readableDatabase
        // 如果没有指定查询后的排序方式，则默认按照名字排序
        // 否则，按照指定的顺序排列返回的结果
        var mySortOrder:String? = null
        if (sortOrder == null || sortOrder == ""){
            mySortOrder = NAME;
        } // if
        else{
            mySortOrder = sortOrder
        } // else
        return when(uriMatcher.match(uri)){
            // 如果URI参数代表操作全部数据项
            STUDENTS ->
                // 执行查询
                db.query(STUDENTS_TABLE_NAME, projection, selection, selectionArgs, null, null, mySortOrder)
            // 如果Uri参数代表操作指定数据项
            STUDENT_ID ->
            {
                // 解析出想查询记录的ID
                val recordId = ContentUris.parseId(uri)
                var selectionClause = "${Companion._ID}=$recordId"
                // 如果原来的selection子句存在，拼接selection子句
                if(selection != null && "" != selection){
                    selectionClause = "$selectionClause AND $selection"
                } // if
                // 执行查询
                db.query(STUDENTS_TABLE_NAME, projection, selectionClause, selectionArgs, null, null, mySortOrder)
            } // STUDENT_ID
            else ->
                throw SQLException("Failed to complete the query from $uri")
        } // when
    } // query

    // 修改数据的方法
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val db = dbHelper.writableDatabase
        // 记录所修改的记录数
        val count: Int
        when(uriMatcher.match(uri)){
            // 如果Uri参数代表操作全部数据项
            STUDENTS -> count = db.update(STUDENTS_TABLE_NAME, values, selection, selectionArgs)
            // 如果Uri参数代表操作指定数据项
            STUDENT_ID ->
            {
                // 解析出想查询记录的ID
                val recordId = ContentUris.parseId(uri)
                var selectionClause = "${Companion._ID}=$recordId"
                // 如果原来的selection子句存在，拼接selection子句
                if(selection != null && "" != selection){
                    selectionClause = "$selectionClause AND $selection"
                } // if
                count = db.update(STUDENTS_TABLE_NAME, values, selectionClause, selectionArgs)
            } // STUDENT_ID
            else -> throw IllegalArgumentException("Unknown URI $uri")
        } // when
        // 通知数据已经改变
        context!!.contentResolver.notifyChange(uri, null)
        return count
    } // update

    // 删除数据的方法
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.readableDatabase
        // 记录所删除的记录数
        val count: Int
        when(uriMatcher.match(uri)){
            // 如果Uri参数代表操作全部数据项
            STUDENTS -> count = db.delete(STUDENTS_TABLE_NAME,selection, selectionArgs)
            // 如果Uri参数代表操作指定数据项
            STUDENT_ID ->
            {
                // 解析出想查询记录的ID
                val recordId = ContentUris.parseId(uri)
                var selectionClause = "${Companion._ID}=$recordId"
                // 如果原来的selection子句存在，拼接selection子句
                if(selection != null && "" != selection){
                    selectionClause = "$selectionClause AND $selection"
                } // if
                count = db.delete(STUDENTS_TABLE_NAME, selectionClause, selectionArgs)
            } // STUDENT_ID
            else -> throw IllegalArgumentException("Unknown URI $uri")
        } // when
        // 通知数据已经改变
        context!!.contentResolver.notifyChange(uri, null)
        return count
    } // delete

    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)){
            // 如果Uri参数代表操作全部数据项
            // 获取所有学生记录
            STUDENTS -> "vnd.android.cursor.dir/vnd.example.students"
            // 如果Uri参数代表操作指定数据项
            // 获取一个特定的学生
            STUDENT_ID -> "vnd.android.cursor.item/vnd.example.students"
            else -> throw IllegalArgumentException("Unknown URI $uri")
        } // when
    } // getType

} // StudentsProvider