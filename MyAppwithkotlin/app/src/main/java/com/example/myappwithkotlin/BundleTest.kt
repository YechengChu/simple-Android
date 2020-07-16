package com.example.myappwithkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast

class BundleTest : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bundle_test)
    } // onCreate

    fun registerAccount(view: View) {
        val NameBox = findViewById<EditText>(R.id.name)
        val PasswordBox = findViewById<EditText>(R.id.password)
        val name = NameBox.text.toString()
        val password = PasswordBox.text.toString()
        val male = findViewById<RadioButton>(R.id.male)
        val female = findViewById<RadioButton>(R.id.female)
        val other = findViewById<RadioButton>(R.id.other)
        var gender: String? = null
        if(male.isChecked){
            gender = "Male"
        } // if
        else if (female.isChecked){
            gender = "Female"
        } // else if
        else if (other.isChecked){
            gender = "Other"
        } // else if

        if(gender == null){
            Toast.makeText(this@BundleTest, "You must select your gender!", Toast.LENGTH_SHORT).show()
        } // if
        else{
            val p = Person(name, password, gender)
            // 创建一个Bundle对象
            val data = Bundle()
            data.putSerializable("person", p)
            // 创建一个Intent
            val intent = Intent(this@BundleTest, ResultActivity::class.java)
            intent.putExtras(data)
            // 启动intent对应的Activity
            startActivity(intent)
        } // else
    } // registerAccount

    fun backToMain(view: View) {
        val backIt = Intent(this@BundleTest, MainActivity::class.java)
        this@BundleTest.startActivity(backIt)
        finish()
    } // backToMain
} // BundleTest