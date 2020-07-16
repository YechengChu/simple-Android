package com.example.myappwithkotlin

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.lang.IllegalArgumentException

class NewActivity : Activity() {
    var myTextViewBox: TextView? = null
    var messageReceived: String? = null
    var tv2: TextView? = null
    var tvTask2: TextView? = null

    // 保持所启动的Service的IBuilder对象
    private lateinit var binder: BindService.MyBinder
    // 定义启动Service的intent
    lateinit var serviceIntent: Intent

    // 定义一个ServiceConnection对象
    private val conn = object : ServiceConnection
    {
        // 当该Activity与Service连接成功时回调该方法
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            println("--Service Connected--")
            // 获取Service的onBind方法所返回的MyBinder对象
            binder = service as BindService.MyBinder
        } // onServiceConnected

        // 当取消绑定的时候被回调。但正常情况下是不被调用的，它的调用时机是当Service服务被意外销毁时，
        // 例如内存的资源不足时这个方法才被自动调用。
        override fun onServiceDisconnected(name: ComponentName) {
            println("--Service Disconnected--")
        } // onServiceDisconnected
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relative_main)
        tv2 = findViewById<View>(R.id.view11) as TextView
        // 展示当前activity实例序列号
        tv2!!.text = this.toString()
        tvTask2 = findViewById<View>(R.id.view22) as TextView
        // 展示当前栈结构的id
        tvTask2!!.text = "current task id:" + this.taskId
        // 相当于java的 Intent myIntent = getIntent()
        val myIntent = intent
        messageReceived = myIntent.getStringExtra("passed message")
        myTextViewBox = findViewById<View>(R.id.myTextView) as TextView
        myTextViewBox!!.text = messageReceived

        // 创建启动Service的intent
        serviceIntent = Intent(this,BindService::class.java)
    }

    fun back(view: View) {
        val backIt = Intent(this@NewActivity, MainActivity::class.java)
        this@NewActivity.startActivity(backIt)
    }

    fun bind(view: View) {
        // 绑定指定Service
        bindService(serviceIntent,conn, Service.BIND_AUTO_CREATE)
        Toast.makeText(this@NewActivity, "Service is binded", Toast.LENGTH_SHORT).show()
    } // bind

    fun unBind(view: View) {
        try {
            // 解除绑定Service
            unbindService(conn)
            Toast.makeText(this@NewActivity, "Service is unbinded", Toast.LENGTH_SHORT).show()
        } // try
        catch (e: IllegalArgumentException){
            Toast.makeText(this@NewActivity, "Service is already unbinded!!!", Toast.LENGTH_SHORT).show()
            // Create an error log for the exception
            Log.e("Service Info","Tried to unbind an unbinded service! \n Exception: $e")
        } // catch
        catch (e: Exception){
            Toast.makeText(this@NewActivity, "Exception info: $e", Toast.LENGTH_SHORT).show()
            // Create an error log for the exception
            Log.e("Service Info","Exception: $e")
        } // catch
    } // unBind

    fun serStatus(view: View) {
        try{
            // 获取并显示Service的count值
            Toast.makeText(this@NewActivity, "Service的count值为：" + binder.count, Toast.LENGTH_SHORT).show()
        } // try
        catch (e: Exception){
            // Create an error log for the exception
            Log.e("Service Info","Exception: $e")
        } // catch
    } // serStatus

    fun nextPage(view: View) {
        val nextIt = Intent(this@NewActivity, NewActivity2::class.java)
        this@NewActivity.startActivity(nextIt)
    } // nextPage

    fun toDbPage(view: View) {
        val nextIt = Intent(this@NewActivity, NewActivity3::class.java)
        this@NewActivity.startActivity(nextIt)
    } // toDbPage

    fun toCPPage(view: View) {
        val nextIt = Intent(this@NewActivity, NewActivity4::class.java)
        this@NewActivity.startActivity(nextIt)
    } // toCPPage
} // NewActivity