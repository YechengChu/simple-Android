package com.example.myappwithkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*

class NewActivity4: Activity() {
    private var uri = Uri.parse("content://com.example.providers.mycontentprovider/")
    private lateinit var listView : ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_layout3)
        listView = findViewById(R.id.listViewShow)
    } // onCreate

    fun back(view: View) {
        val backIt = Intent(this@NewActivity4, NewActivity::class.java)
        this@NewActivity4.startActivity(backIt)
    } // back

    fun insertBtn(view: View) {
        val values = ContentValues()
        values.put("name","Hello wrold")
        // 调用ContentResolver的insert()方法
        // 实际返回的值是Uri对应的ContentProvider的insert()的返回值
        val newUri = contentResolver.insert(uri, values)
        Toast.makeText(this, "远程ContentProvider新插入记录的Uri为：" + newUri?.toString(), Toast.LENGTH_SHORT).show()
    } // insertBtn

    @SuppressLint("Recycle")
    fun queryBtn(view: View) {
        // 调用ContentResolver的query()方法
        // 实际返回的是该Uri对应的ContentProvider的query()的返回值
        val c = contentResolver.query(uri, null, "query_where", null, null)
        Toast.makeText(this, "远程ContentProvider返回的Cursor为：" + c?.toString(), Toast.LENGTH_SHORT).show()
    } // queryBtn

    fun updateBtn(view: View) {
        val values = ContentValues()
        values.put("hi", "Hello Earth")
        // 调用ContentResolver的update()方法
        // 实际返回的是该Uri对应的ContentProvider的update()的返回值
        val count = contentResolver.update(uri, values, "update_where", null)
        Toast.makeText(this, "远程ContentProvider更新记录数为：" + count, Toast.LENGTH_SHORT).show()
    } // updateBtn

    fun deleteBtn(view: View) {
        // 调用ContentResolver的delete()方法
        // 实际返回的是该Uri对应的ContentProvider的delete()的返回值
        val count = contentResolver.delete(uri, "delete_where", null)
        Toast.makeText(this, "远程ContentProvider删除记录数为：" + count, Toast.LENGTH_SHORT).show()
    } // deleteBtn

    // 执行插入信息
    @SuppressLint("Recycle")
    fun addInfo(view: View) {
        // 获取用户输入
        val inputNameBox = findViewById<EditText>(R.id.editTextName)
        val inputGradeBox = findViewById<EditText>(R.id.editTextGrade)
        val inputName = inputNameBox.text.toString()
        val inputGrade = inputGradeBox.text.toString()
        // 插入记录
        val values = ContentValues()
        values.put(StudentsProvider.NAME, inputName)
        values.put(StudentsProvider.GRADE, inputGrade)
        val returnedUri = contentResolver.insert(StudentsProvider.CONTENT_URI, values)
        // 显示提示信息
        Toast.makeText(this@NewActivity4, "记录添加成功！\n返回Uri为：${returnedUri.toString()}", Toast.LENGTH_LONG).show()
        // 重置输入框
        inputNameBox.setText("")
        inputGradeBox.setText("")
        // 查询数据（为了把数据表中的所有信息都显示在listView中，所以要得到一个cursor）
        // selection筛选条件为null，就是没有条件，所有的数据都会被选出
        val cursor = contentResolver.query(StudentsProvider.CONTENT_URI, null, null, null, StudentsProvider._ID)
        inflateList(cursor!!)
    } // addInfo

    // 查询数据
    @SuppressLint("Recycle")
    fun searchInfo(view: View) {
        // 获取用户输入
        val keyBox = findViewById<EditText>(R.id.editTextKey)
        val key = keyBox.text.toString()
        // 执行查询
        // SELECT * FROM students WHERE name LIKE $key or grade LIKE $key ORDER BY _id
        val returnedCursor = contentResolver.query(StudentsProvider.CONTENT_URI, null, "name LIKE ? or grade LIKE ?", arrayOf("%$key%", "%$key%"), StudentsProvider._ID)
        inflateList(returnedCursor!!)
        // 显示提示信息
        Toast.makeText(this@NewActivity4, "记录查询成功！\n返回Cursor为：$returnedCursor", Toast.LENGTH_LONG).show()
        // 重置输入框
        keyBox.setText("")
    } // viewInfo

    // 删除最新的一条记录
    @SuppressLint("Recycle")
    fun deleteLastInfo(view: View) {
        // 执行删除
        // DELETE FROM students WHERE  _id=(SELECT MAX(_id) FROM students
        val num = contentResolver.delete(StudentsProvider.CONTENT_URI, " _id=(SELECT MAX(_id) FROM ${StudentsProvider.STUDENTS_TABLE_NAME})", null)
        // 如果一切正常num值应该为1，因为只删了1条数据
        // 如果删除数据为0则说明没有删除成功，可能是数据库空了
        if(num > 0){
            // 显示提示信息
            Toast.makeText(this@NewActivity4, "记录删除成功！\n删除记录：$num 条", Toast.LENGTH_LONG).show()
            // 查询数据（为了把数据表中的所有信息都显示在listView中，所以要得到一个cursor）
            val cursor = contentResolver.query(StudentsProvider.CONTENT_URI, null, null, null, StudentsProvider._ID)
            inflateList(cursor!!)
        } // if
        else{
            // 显示提示信息
            Toast.makeText(this@NewActivity4, "记录删除失败！可能是由于数据库已空！\n删除记录：$num 条", Toast.LENGTH_LONG).show()
        } // else
    } // deleteLastInfo

    // 用于填充listView的方法
    private fun inflateList(cursor: Cursor){
        // 填充SimpleCursorAdapter
        // 将Cursor封装成SimpleCursorAdaptor，这个SimpleCursorAdaptor实现了Adapter接口
        // 因此可以作为ListView或GridView的内容适配器
        val adapter = SimpleCursorAdapter(this@NewActivity4,
            R.layout.line, cursor, arrayOf("name", "grade"),
            intArrayOf(R.id.my_title, R.id.my_content),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        // 显示数据
        listView.adapter = adapter
    } // inflateList
} // NewActivity