package com.example.myappwithkotlin

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class MainActivity : Activity() {
    var myEditTextBox: EditText? = null
    var tv: TextView? = null
    var tvTask: TextView? = null
    lateinit var receiver: BatteryReceiver
    lateinit var filter: IntentFilter
    lateinit var receiver2: LocalReceiver
    lateinit var filter2: IntentFilter
    lateinit var localBroadcastManager: LocalBroadcastManager

    // 定义启动Service的intent
    lateinit var serviceIntent: Intent

    // 选择在Activity生命周期方法中的onResume()中注册
    override fun onResume() {
        super.onResume()
        receiver = BatteryReceiver()
        filter = IntentFilter("android.intent.action.BATTERY_CHANGED")
        registerReceiver(receiver, filter)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        receiver2 = LocalReceiver()
        filter2 = IntentFilter("com.example.LOCALMSG")
        localBroadcastManager.registerReceiver(receiver2,filter2)
    }

    override fun onPause() {
        super.onPause()
        //销毁在onResume()方法中的广播
        unregisterReceiver(receiver)
        localBroadcastManager.unregisterReceiver(receiver2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById<View>(R.id.view1) as TextView
        tv!!.text = this.toString()
        tvTask = findViewById<View>(R.id.view2) as TextView
        // 展示当前栈结构的id
        tvTask!!.text = "current task id:" + this.taskId

        // 创建启动Service的intent
        serviceIntent = Intent(this,FirstService::class.java)

        val sendBtn = findViewById<View>(R.id.bn2) as Button
        sendBtn.setOnClickListener {
            myEditTextBox = findViewById<View>(R.id.editTextBox) as EditText
            val myText = myEditTextBox!!.text.toString()
            myEditTextBox!!.setText("")
            val myIntent = Intent(this@MainActivity, NewActivity::class.java)
            myIntent.putExtra("passed message", myText)
            this@MainActivity.startActivity(myIntent)
        }
    } // onCreate

    fun reOpen(view: View) {
        startActivity(Intent(this@MainActivity, MainActivity::class.java))
    } // reOpen

    fun sendMsg(view: View) {
        val msgIntent = Intent()
        msgIntent.setAction("com.example.MYAPPKOTLIN")
        msgIntent.`package` = packageName
        msgIntent.putExtra("msg", "有序广播信息")
        sendOrderedBroadcast(msgIntent,null)
        Log.i("BroadcastReceiver","发出了有序广播信息")
    } // sendMsg

    fun sendLocalMsg(view: View) {
        val localMsgIntent = Intent()
        localMsgIntent.setAction("com.example.LOCALMSG")
        localMsgIntent.`package` = packageName
        localMsgIntent.putExtra("msgLocal", "本地广播信息")
        localBroadcastManager.sendBroadcast(localMsgIntent)
        Log.i("BroadcastReceiver","发出了本地广播信息")
    } // sendLocalMsg

    fun start(view: View) {
        // 启动指定Service
        startService(serviceIntent)
    } // start

    fun end(view: View) {
        // 停止指定Service
        stopService(serviceIntent)
    } // end

    fun toBundleTest(view: View) {
        val nextIt = Intent(this@MainActivity, BundleTest::class.java)
        this@MainActivity.startActivity(nextIt)
    } // toBundleTest
}
