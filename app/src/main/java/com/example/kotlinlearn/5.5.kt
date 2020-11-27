package com.example.kotlinlearn

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import java.lang.StringBuilder

//先来看一个函数
fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append(letter)
    }
    result.append("\nEnd")
    return result.toString()
}

//通过with函数重写上面的函数
fun alphabetWith(): String {
    val stringBuilder = StringBuilder()
    return with(stringBuilder) {
        for (letter in 'A'..'Z') {
            this.append(letter)
        }
        append("\nEnd")
        toString()
    }
}

//简化上面的函数
fun alphabetWithOptimize(): String =
        with(StringBuilder()) {
            for (letter in 'A'..'Z') {
                append(letter)
            }
            append("\nEnd")
            //这里注意：
            //如果外部已经有一个同名的toString()方法，如果想要调用外部的toString()方法,可以向下面这样做
            //this@OuterClass.toString()
            toString()
        }

//apply函数，始终会返回接收者对象
fun alphabetApply():String = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nEnd")
}.toString()

//使用apply初始化一个TextView
fun createViewWithCustomAttributes(context:Context) =
        TextView(context).apply {
            text = "lixiaojun"
            setTextColor(Color.parseColor("#000000"))
            textSize = 100F
        }

//使用库函数buildString重写alphabet函数
fun alphabetBuildString()= buildString {
    for (letter in 'A'..'Z'){
        append(letter)
    }
    append("\nEnd")
}