package com.example.myappwithkotlin

import  java.io.Serializable

class Person (var name: String?, var passwd: String?, var gender: String?) : Serializable
{
    var id: Int? = null
}