package com.example.myappwithkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent){
        Toast.makeText(context, "接收到的Intent的Action为："+intent.action
                +"\n消息内容是："+intent.getStringExtra("msg"),Toast.LENGTH_LONG).show()
        Log.i("BroadcastReceiver","优先级高的receiver收到了信息")
        // 创建一个Bundle对象，并存入数据
        val bundle = Bundle()
        bundle.putString("msgChanged", "第一个BroadcastReceiver存入的消息")
        // 将bundle放入结果中
        setResultExtras(bundle)
        // 取消Broadcast的继续传播
        // abortBroadcast()
        Log.i("BroadcastReceiver","第一个receiver发出了修改后的信息")
    }
}