package com.example.kotlinlearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.tv)
        FUCK_VAR = "shit"
        tv.text = "${InvokeKotlinClass.getScreenWidth()},${FUCK_VAR}"

        Log.e("xiaojun","KotlinUtilsKt.getScreenWidth()=${InvokeKotlinClass.getScreenWidth()}")

        Log.e("xiaojun","ExtendsFunctionClass Invoke:${InvokeKotlinClass.extendsFunction("leike")}")
    }
}