package com.example.myappwithkotlin

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class NormalService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // 模拟执行耗时任务，可能导致ANR(Application Not Responding)异常
        // 模拟耗时任务需运行30s
        val endTime = System.currentTimeMillis() + 30 * 1000
        Log.i("Normal Service", "Service started")
        while (System.currentTimeMillis() < endTime){
            synchronized(this){
                (this as Object).wait(endTime - System.currentTimeMillis())
            }
        } // while
        Log.i("Normal Service", "耗时任务完成")
        return START_STICKY
    }

    override fun onDestroy() {
        Log.i("Normal Service", "Service ended")
        super.onDestroy()
    }
}