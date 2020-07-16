package com.example.myappwithkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView

class ResultActivity: Activity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)
        val name = findViewById(R.id.nameResult) as TextView
        val password = findViewById(R.id.passwordResult) as TextView
        val gender = findViewById(R.id.genderResult) as TextView
        // 获取启动该Activity的Intent
        val intent = intent
        // 直接通过Intent取出它所携带的Bundle数据包中的数据
        val p = intent.getSerializableExtra("person") as Person
        name.text = "Your account name is: ${ p.name!!}"
        password.text = "Your password is: ${p.passwd!!}"
        gender.text = "Your gender is: ${p.gender!!}"
    } // onCreate

    fun backMethod(view: View) {
        val backIt = Intent(this@ResultActivity, BundleTest::class.java)
        this@ResultActivity.startActivity(backIt)
        finish()
    } // backMethod
} // ResultActivity