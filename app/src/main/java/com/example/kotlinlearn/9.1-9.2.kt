package com.example.kotlinlearn

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

//泛型类型参数
val listString: ArrayList<String> = arrayListOf()

//编译器会推导出类型实参
val listString2 = listOf("Dmitry", "Svetlana")

//kotlin类型实参必须定义，不支持原生态类型，比如java可以定义List而不指定其包含哪种类型
val readers: MutableList<String> = mutableListOf()

val readers2 = mutableListOf<String>()

//---------------------------泛型函数和属性-------------------------------
//fun <T> List<T>.slice(indices:IntRange):List<T>

//调用泛型函数
fun invokeGenericParam() {
    val letters = ('a'..'z').toList()
    println(letters.slice<Char>(0..2))//显示指定类型实参
    println(letters.slice(0..3))//编译器推导出T是Char类型
}

//fun <T> List<T>.filter(predicate:(T)->Boolean):List<T>

//声明泛型的扩展属性
//返回列表倒数第二个元素
//val <T> List<T>.penultimate: T
//    get() = this[size - 2]

//声明泛型类
/*interface List<T> {
    operator fun get(index: Int): T
}*/

//继承一个泛型类(接口)需要提供泛型实参，实参可以是具体的类型实参
//比如下面的String
/*class StringList : List<String> {
    override fun get(index: Int): String = ""
}*/

//继承泛型类提供的泛型形参也可以为另一个类型形参,比如下面这样
//class ArrayList<T> : List<T> {
//    override fun get(index: Int): T = ...
//
//}

//一个类甚至可以把它自己作为类型实参引用，例如
/*interface Comparable<T> {
    operator fun compareTo(other: T): Int
}*/

/*class String : Comparable<String> {
    override fun compareTo(other: String): Int = if (this == other) 0 else -1
}*/

//类型参数约束
//上界约束
//fun <T : Number> List<T>.sum(): T

//一旦指定了类型形参的上界(类型),就可以将T的值作为它的上界(类型)来使用
fun <T : Number> oneHalf(value: T): Double {
    return value.toDouble() / 2.0//调用Number类中的方法
}

fun <T : Comparable<T>> max(first: T, second: T): T {
    return if (first > second) first else second
}

//为一个类型指定多个约束
fun <T> ensureTrailingPeriod(seq: T): Any?
        where T : CharSequence, T : Appendable {//类型参数的约束列表
    if (!seq.endsWith('.')) {//调用CharSequence接口定义的扩展函数
        seq.append('.')//调用Appendable接口的方法
    }
    return null
}

//让类型形参非空
//任何类型实参，包括可空的类型实参，都可以替换其类型形参
//没有指定上界(类型)的类型形参将会使用Any?这个默认的上界
class Processor2<T> {
    fun process(value: T) {
        value?.hashCode()
    }
}

//如果除了可空性外没有任何限制，可以使用Any代替默认的Any?作为上界
class Processor3<T : Any> {
    fun process(value: T) {
        value.hashCode()
    }
}

//----------------星号投影---------------------
//kotlin规定了，不允许使用没有指定类型实参的泛型类型
//如果想检查一个值是否是列表，而不是其它对象，可以使用特殊的<星号投影>语法来做检查
fun <T> startProjectionGrammar(value: T) {
    if (value is List<*>) {//判断value是不是一个List
        println("value是List")
        value as? List<Int>
            ?: throw IllegalArgumentException("List is expected")//<as?>可空安全转换标识符,如果value转换失败会返回null
    } else {
        println("value不是List")
    }
}

//对已知类型实参做类型转换
//>>>?????这里和书中不一样，书中是kotlin1.0版本，可能新版本特性被移除了
/*
fun printSum(c:Collection<Int>){
    if (c is List<Int>){
        println(c.sum())
    }
}*/

//声明带实化类型参数的函数
//因为泛型擦除的特性，导致泛型函数或者泛型类中不能决定其中的类型实参
//>>>fun <T> isA(value : Any) = value is T
//但是：内联函数可以避免这种限制

//通过关键字reified标记类型参数,实化参数必须使用内联关键字inline
//inline函数只有在当实参为lambda并且和函数一起被内联的时候才有性能优化的作用
inline fun <reified T> isA(value: Any) = value is T

fun testInlineReifiedFunction() {
    println(isA<String>(123))
}

//实化类型参数的有意义的例子
fun testRelification() {
    val items = listOf("one", 2, "three", "xiaojun")
    println(items.filterIsInstance<String>())
}

//使用实化类型参数代替类引用
//语法::class.java对应java中的Service.class
val serviceImpl = ServiceLoader.load(Service::class.java)

//使用带实化类型参数的函数重写后:
inline fun <reified T> loadService() =
    ServiceLoader.load(T::class.java)

val serviceImpl2 = loadService<Service>()

//使用实化类型参数重写startActivity
inline fun <reified T : Activity> Context.startActivity(activity: Activity) =
    startActivity(Intent(activity, T::class.java))

//调用:startActivity<MainActivity>(this)

//实例化类型参数的限制
//1.用在类型检查和类型转换中(is,!is,as,as?)
//使用kotlin反射API(::class)
//获取相应的java.lang.Class(::class.java)
//作为调用其他函数的类型实参

fun main(args: Array<String>) {
    testInlineReifiedFunction()
    testRelification()
}