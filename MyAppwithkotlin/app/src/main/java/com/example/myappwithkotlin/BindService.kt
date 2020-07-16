package com.example.myappwithkotlin

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class BindService : Service() {
    private var count: Int = 0
    // private var quit: Boolean = false
    // 也可以省略Boolean这个类型声明
    private var quit = false
    // 定义onBinder方法所返回的对象
    private val binder = MyBinder()
    // 通过继承Binder来实现IBinder类
    inner class MyBinder : Binder(){
        // 获取Service的运行状态: count
        val count: Int get() = this@BindService.count
    } // MyBinder
    // Service被绑定时回调该方法
    override fun onBind(intent: Intent): IBinder {
        Log.i("Service Info","Service is Binded")
        // 返回IBinder对象
        return binder
    } // onBind

    override fun onCreate() {
        super.onCreate()
        Log.i("Service Info","Service is Created")
        // 启动一条线程，动态的修改count状态值
        object: Thread() {
            override fun run() {
                while(!quit){
                    Thread.sleep(1000)
                    this@BindService.count++
                } // while
            } // run
        }.start()
    } // onCreate

    // Service被断开连接时回调该方法
    override fun onUnbind(intent: Intent?): Boolean {
        Log.i("Service Info","Service is Unbinded")
        return true
    } // onUnbind

    // Service被关闭之前回调该方法
    override fun onDestroy() {
        super.onDestroy()
        this.quit = true
        Log.i("Service Info","Service is Destroyed")
    } // onDestroy

} // BindService