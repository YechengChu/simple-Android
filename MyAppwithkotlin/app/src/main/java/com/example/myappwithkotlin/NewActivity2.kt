package com.example.myappwithkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.*

class NewActivity2 : Activity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    // 指定文件的名字
    private  val FILE_NAME = "testIO.bin"
    lateinit var readBox: TextView
    lateinit var writeBox: EditText

    lateinit var intBox: EditText

    private var catService: ICat? = null
    private val aidlConn = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // 获取远程Service的onBind方法返回的对象的代理
            catService = ICat.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            catService = null
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_layout)
        val serviceInfo = findViewById<Button>(R.id.bn666)
        val colourBox = findViewById<TextView>(R.id.view111)
        val weightBox = findViewById<TextView>(R.id.view222)
        // 创建所需绑定的Service的Intent
        val aidlIntent = Intent()
        aidlIntent.action = "com.example.myappwithkotlin.ALID_SERVICE"
        aidlIntent.`package` = packageName
        // 绑定远程Service
        bindService(aidlIntent,aidlConn, Service.BIND_AUTO_CREATE)
        serviceInfo.setOnClickListener{
            // 获取并显示远程Service的状态
            colourBox.text = "Colour: ${catService?.colour}"
            weightBox.text = "Weight: ${catService?.weight}"
        }

        // 获取只能被本程序读写的SharedPreferences对象
        preferences = getSharedPreferences("NewAct2", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解除绑定
        this.unbindService(aidlConn)
    }

    fun back(view: View) {
        val backIt = Intent(this@NewActivity2, NewActivity::class.java)
        this@NewActivity2.startActivity(backIt)
    }

    fun intentService(view: View) {
        // 创建需要启动Service的Intent
        val intent = Intent(this, MyIntentService::class.java)
        // 启动Service
        startService(intent)
    }

    fun writePre(view: View) {
        var intSaved : Boolean = false
        // 存入一个用户输入的数
        intBox = findViewById(R.id.editTextBox111)
        var inputInt: Int
        var inputString : String = intBox.text.toString()
        println(inputString)
        try{
            inputInt = inputString.toInt()
            editor.putInt("userInput", inputInt)
            intBox.setText("")
            intBox.hint = "Enter an Integer"
            Toast.makeText(this@NewActivity2,"Integer saved successfully!", Toast.LENGTH_SHORT).show()
            intSaved = true
        }
        catch (e: Exception){
            intBox.setText("")
            intBox.hint = "The type isn't int, try again!"
            Toast.makeText(this@NewActivity2,"Woops! Exception met! \nPlease try again!", Toast.LENGTH_SHORT).show()
            Log.e("SharedPreferences","Exception: $e")
        }
        if (intSaved){
            val dateAndTime = SimpleDateFormat("dd/mm/yyyy" + " hh:mm:ss")
            // 存入当前时间
            editor.putString("date & time", dateAndTime.format(Date()))
            println("d&t saved")
        }
        // 提交所有存入的数据
        editor.apply()
    } // writePre

    fun readPre(view: View) {
        // 读取字符串数据
        val dateAndTime = preferences.getString("date & time", null)
        // 读取int类型的数据
        val userInput = preferences.getInt("userInput",0)
        val result = if(dateAndTime == null) "You haven't save anything yet"
        else "The saved time and date is: $dateAndTime \nThe number input is: $userInput"
        // 使用Toast提示信息
        Toast.makeText(this@NewActivity2, result, Toast.LENGTH_SHORT).show()
    } // readPre

    fun writeFile(view: View) {
        writeBox = findViewById(R.id.editTextBox222)
        // 将writeBox的内容写入文件中
        writeHelper(writeBox.text.toString())
        Toast.makeText(this@NewActivity2,"Message saved to file!",Toast.LENGTH_SHORT).show()
        writeBox.setText("")
    } // writeFile

    fun readFile(view: View) {
        readBox = findViewById(R.id.view333)
        readBox.setMovementMethod(ScrollingMovementMethod.getInstance())
        // 读取指定文件中的内容，并显示出来
        readBox.text = readHelper()
    } // readFile

    private fun writeHelper(content: String){
        // 如果原先就有这个文件的话删除那个文件
        deleteFile(FILE_NAME)
        // 以追加方式打开文件输出流(FileOutputStream)
        val fos = openFileOutput(FILE_NAME, Context.MODE_APPEND)
        // 将FileOutputStream包装成PrintStream
        val ps = PrintStream(fos)
        // 输出文件内容
        ps.println(content)
        // 关闭文件输出流
        ps.close()
    } // writeHelper

    private fun readHelper(): String? {
        try{
            // 打开文件输入流(FileInputStream)
            val fis = openFileInput(FILE_NAME)
            val buff = ByteArray(1024)
            val sb = StringBuilder("")
            // 读取文件内容
            var hasRead = fis.read(buff)
            while (hasRead > 0) {
                sb.append(String(buff,0,hasRead))
                hasRead = fis.read(buff) // 循环读取
            }
            // 关闭文件输入流
            fis.close()
            return sb.toString()
        } // try
        catch(e:Exception){
            return "Exception: $e"
        } // catch
    } // readHepler
} // NewActivity2