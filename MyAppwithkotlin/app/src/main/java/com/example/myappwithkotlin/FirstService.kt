package com.example.myappwithkotlin

import android.app.Service
import android.content.Intent
import android.os.IBinder

class FirstService : Service()
{
    // Service被绑定时回调该方法
    override fun onBind(intent: Intent): IBinder?
    {
        return null
    } // onBind

    // Service被创建时回调该方法
    override fun onCreate()
    {
        super.onCreate()
        println("Service is Created")
    } // onCreate

    // Service被启动时回调该方法
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        println("Service is Started")
        return START_STICKY
    } // onStartCommand

    // Service被关闭之前回调该方法
    override fun onDestroy()
    {
        super.onDestroy()
        println("Service is Destroyed")
    } // onDestroy
} // FirstService