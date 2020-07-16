package com.example.myappwithkotlin

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.*

class NewActivity3: Activity() {
    private lateinit var listView : ListView
    private lateinit var dpHelper : MyDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_layout2)
        listView = findViewById(R.id.listView1111)
        // 创建或打开数据库(此时需要使用绝对路径)
        // 创建MyDatabaseHelper对象，该对象会负责打卡数据库
        dpHelper = MyDatabaseHelper(this, this.filesDir.toString() + "/myTestDb.db3", 1)
        loadData()
    } // onCreate

    fun back(view: View) {
        val backIt = Intent(this@NewActivity3, NewActivity::class.java)
        this@NewActivity3.startActivity(backIt)
    } // back

    fun insertData(view: View) {
        // 获取用户输入
        val titleBox = findViewById<EditText>(R.id.textBox1111)
        val contentBox = findViewById<EditText>(R.id.textBox2222)
        val title = titleBox.text.toString()
        val content = contentBox.text.toString()
        insertDataHelper(title,content)
        loadData()
        Toast.makeText(this@NewActivity3, "Insert successfully!", Toast.LENGTH_SHORT).show()
        titleBox.setText("")
        contentBox.setText("")
    } // insertData

    // 执行查询语句，把底层数据表中的记录查询出来
    // 且使用ListView将查询结果(Cursor)显示出来
    private fun loadData(){
        val cursor = dpHelper.readableDatabase.rawQuery("SELECT * FROM news_inf", null)
        inflateList(cursor)
    } // loadData

    // 向底层数据表中插入一行记录
    private fun insertDataHelper(title: String, content: String){
        // 执行插入语句
        dpHelper.readableDatabase.execSQL("INSERT INTO news_inf VALUES(null, ?, ?)", arrayOf(title,content))
    } // insertDataHelper

    private fun inflateList(cursor: Cursor){
        // 填充SimpleCursorAdapter
        // 将Cursor封装成SimpleCursorAdaptor，这个SimpleCursorAdaptor实现了Adapter接口
        // 因此可以作为ListView或GridView的内容适配器
        val adapter = SimpleCursorAdapter(this@NewActivity3,
            R.layout.line, cursor, arrayOf("news_title", "news_content"),
            intArrayOf(R.id.my_title, R.id.my_content),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        // 显示数据
        listView.adapter = adapter
    } // inflateList

    override fun onDestroy() {
        super.onDestroy()
        // 退出程序时关闭MyDatabaseHelper里的SQLiteDatabase
        dpHelper.close()
    } // onDestroy

} // NewActivity3