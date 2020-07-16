package com.example.myappwithkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyReceiver2: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = getResultExtras(true)
        // 解析前一个BroadcastReceiver所存入的key为msgChanged的消息
        val msg2 = bundle.getString("msgChanged")
        Toast.makeText(context, "接收到的Intent的Action为："+intent.action
                +"\n消息内容是："+intent.getStringExtra("msg")
                 +"\n第一个Broadcast存入的消息为："+msg2,Toast.LENGTH_LONG).show()
        Log.i("BroadcastReceiver","优先级低的receiver收到了修改后的消息")
    }
}
