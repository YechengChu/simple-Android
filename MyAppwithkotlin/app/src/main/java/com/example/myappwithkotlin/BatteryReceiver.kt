package com.example.myappwithkotlin

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class BatteryReceiver: BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val sysBundle = intent.extras
        // 获取当前电量
        val current = sysBundle!!.getInt("level")
        // 获取总电量
        // 由于在上面已经声明了sysBundle不为null，所以在这里不用!!来声明
        val total = sysBundle.getInt("scale")
        // 如果当当前电量小于总电量的70%，进行提示
        if (current * 1.0 / total <= 0.1)
        {
            Toast.makeText(context,"接收到的Intent的Action为："+intent.action
                + "\n提示信息: 电量低于10%，请尽快充电!",Toast.LENGTH_LONG).show()
            Log.i("BatteryReceiver","电量低于10%信息发送")
        }
    }
}