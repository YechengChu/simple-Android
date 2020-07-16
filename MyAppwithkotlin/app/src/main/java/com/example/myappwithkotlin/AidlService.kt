package com.example.myappwithkotlin

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class AidlService : Service(){
    private lateinit var catBinder: CatBinder
    private var timer = Timer()
    private var colours = arrayOf("red", "yellow", "black", "pink", "blue", "purple", "white")
    private var weights = doubleArrayOf(2.3, 3.1, 1.58, 3.24, 2.98, 1.65, 2.8)
    private lateinit var colour: String
    private var weight: Double = 0.0

    // 继承Stub，也就是实现了ICat接口，并实现了IBinder接口
    // 必须要加inner，不加的话就是静态的内部类，无法访问非静态的colour和weight
    inner class CatBinder : ICat.Stub()
    {
        override fun getColour(): String
        {
            return this@AidlService.colour
        }

        override fun getWeight(): Double
        {
            return this@AidlService.weight
        }
    }

    override fun onCreate()
    {
        super.onCreate()
        catBinder = CatBinder()
        //schedule(task, delay,period)
        //task-所要安排执行的任务 delay-执行任务之前delay的时间 period-执行一次task的时间间隔，单位毫秒
        //作用：在等待delay毫秒后首次执行task，每隔period毫秒重复执行task
        timer.schedule(object : TimerTask()
        {
            override fun run()
            {
                // 随机地改变Service组件内colour、weight属性的值。
                // Math.random()得到一个大于等于0，小于等于1的数
                val rand1 = (Math.random() * 7).toInt()
                val rand2 = (Math.random() * 7).toInt()
                colour = colours[rand1]
                weight = weights[rand2]
            }
        }, 0, 500)
    }

    override fun onBind(intent: Intent): IBinder?
    {
        /* 返回catBinder对象
         * 在绑定本地Service的情况下，该catBinder对象会直接
         * 传给客户端的ServiceConnection对象
         * 的onServiceConnected方法的第二个参数；
         * 在绑定远程Service的情况下，只将catBinder对象的代理
         * 传给客户端的ServiceConnection对象
         * 的onServiceConnected方法的第二个参数；
         */
        return catBinder
    }

    override fun onDestroy()
    {
        timer.cancel()
    }
}