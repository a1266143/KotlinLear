//通知编译器将此kotlin文件中的顶层函数生成的对应java类的名称改为ScreenUtils
@file:JvmName("ScreenUtils")

package com.example.kotlinlearn
import com.example.kotlinlearn.lastChar as last

//申明顶层常量属性
//java可以通过 ClassName.Value调用
const val FUCK = "CONST VALUE"

//申明顶层变量属性
//java可以通过getter方法或者setter访问
var FUCK_VAR = "fuck_var"

//kotlin中的顶层函数，可以被java像ClassName.staticMethod()方式调用
fun getScreenWidth():Int{
    return 1
}

//测试使用其它文件中定义的扩展函数(3.3-3.4.kt文件中的String类的扩展函数lastChar)
fun testExtendsFunction(){
    println("Kotlin".last())
}

fun main(args:Array<String>){
    testExtendsFunction()
}