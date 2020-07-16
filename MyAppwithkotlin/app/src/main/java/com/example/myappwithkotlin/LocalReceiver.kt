package com.example.myappwithkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class LocalReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "接收到的Intent的Action为："+intent.action
                +"\n消息内容是："+intent.getStringExtra("msgLocal"), Toast.LENGTH_LONG).show()
        Log.i("BroadcastReceiver","Localreceiver收到了本地广播信息")
    }
}