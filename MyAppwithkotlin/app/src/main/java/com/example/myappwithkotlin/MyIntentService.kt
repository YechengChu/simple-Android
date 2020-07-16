package com.example.myappwithkotlin

import android.app.IntentService
import android.content.Intent
import android.util.Log

class MyIntentService : IntentService("MyIntentService") {
    // IntentService会使用单独的线程来执行该任务的代码
    override fun onHandleIntent(intent: Intent?) {
        // 模拟执行耗时任务，该方法内可执行任何耗时任务，比如下载文件等
        // 模拟耗时任务需运行30s
        val endTime = System.currentTimeMillis() + 30 * 1000
        Log.i("Intent Service", "Service started")
        while (System.currentTimeMillis() < endTime){
            synchronized(this){
                (this as Object).wait(endTime - System.currentTimeMillis())
            }
        } // while
        Log.i("Intent Service", "耗时任务完成")
    }

    override fun onDestroy() {
        Log.i("Intent Service", "Service ended")
        super.onDestroy()
    }
}