package com.example.kotlinlearn

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock

//高阶函数:以另一个函数作为参数或者返回值的函数
fun testHighLevelMethod() {
    val list = arrayListOf<String>()
    list.filter { it == "li" }
}

//函数类型
fun testMethodType() {
    //编译器将会推导出下面两个变量(sum , action)为函数类型
    val sum = { x: Int, y: Int -> x + y }
    val action = { println(42) }
    //声明函数类型
    var sumDefine: (Int, Int) -> Int//两个int类型参数和int型返回值
    var actionDefine: () -> Unit//没有参数和返回值的函数
    //函数类型赋值
    sumDefine = sum
    sumDefine = { x, y -> x + y }//后面是lambda表达式
    actionDefine = action
    actionDefine = { println("???") }
    //函数类型的返回值可以标记为null
    val canReturnNull: (Int, Int) -> Int?
    canReturnNull = { _, _ -> null }
    //定义一个函数类型的可空变量
    val nullMethodType: ((Int) -> Unit)?
    nullMethodType = null//函数变量被赋值为null
}

//函数类型的参数名：可以为函数类型的参数命名
fun methodTypeParam(
    name: String,
    method: (f: Int, s: Int) -> Int
) {
    println("name=$name")
    //调用函数类型的参数
    println("methodResult=${method(1000, 20000)}")
}


fun testMethodTypeParam() {
    //虽然methodTypeParam的函数类型的参数名称声明了，但是传递lambda的时候并不一定需要传递一样的参数名称，只需要类型一样即可
    //这样做的好处是方便IDE的代码补全，提升代码可读性
    methodTypeParam("lixiaojun") { x, y -> x + y }

}

//简单重新实现标准库函数:filter函数(基于String类型)
fun String.filter(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    for (index in 0 until length) {
        val element = get(index)
        if (predicate(element))
            sb.append(element)
    }
    return sb.toString()
}

//在java中使用函数类TODO

//函数类型的参数默认值
fun methodTypeParam2(
    function: (Int) -> Int = { it }//传递一个lambda表达式作为函数类型的默认参数
) {
    println(function(10))
}

//函数类型的参数为null
fun methodTypeParam3(
    function: ((Int) -> Int)?
) {
    //第一种方法:判断function不等于空再调用
    if (function != null)
        function(10)
    //第二种方法:函数类型是一个包含invoke方法的接口的具体实现
    function?.invoke(5)
}

fun <T> Collection<T>.joinToString3(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: ((T) -> String)? = null
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        val str = transform?.invoke(element) ?: element.toString()
        result.append(str)
    }
    result.append(postfix)
    return result.toString()
}

//返回函数的函数
enum class Delivery { STANDARD, EXPEDITED }

class Order(val itemCount: Int)

//不同的条件下返回不同的逻辑
fun getShippingCostCalculator(delivery: Delivery):
            (Order) -> Double {
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount }
    }
    return { order -> 1.2 * order.itemCount }
}

//通过lambda去除重复代码
enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }
data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)

val log = listOf(
    SiteVisit("/", 34.0, OS.WINDOWS),
    SiteVisit("/", 22.0, OS.MAC),
    SiteVisit("/login", 12.0, OS.WINDOWS),
    SiteVisit("/signup", 8.0, OS.IOS),
    SiteVisit("/", 16.3, OS.ANDROID)
)

//显示Windows机器的平均访问时间
val averageWindowsDuration = log.filter { it.os == OS.WINDOWS }
    .map { it.duration }
    .average()

//如果要计算来自Mac用户的相同数据，可以改为如下
fun List<SiteVisit>.averageDurationFor(os: OS) =
    filter { it.os == os }.map(SiteVisit::duration).average()

//使用lambda将filter中的条件抽取为一个高阶函数来去除重复代码
fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) =
    filter(predicate).map(SiteVisit::duration).average()

//----------------内联函数:消除lambda运行时开销-------------------------
//当一个函数被声明为内联时，函数体会被直接替换到函数被调用的地方，而不是被正常调用
inline fun <T> synchronized(lock:Lock,action:()->T):T{
    lock.lock()
    try {
        return action()
    }
    finally {
        lock.unlock()
    }
}

//下面两个函数
fun foo(l:Lock){
    println("同步之前")
    synchronized(l){
        println("Action")
    }
    println("同步之后")
}

//编译后的foo函数
fun __foo__(l:Lock){
    println("同步之前")
    l.lock()
    try{
        println("Action")
    }
    finally {
        l.unlock()
    }
    println("同步之后")
}

//TODO 内联函数的运作

fun main(args: Array<String>) {
    testMethodTypeParam()
    println("abcdef".filter { it > 'c' })
    methodTypeParam2()//使用默认的函数参数
    methodTypeParam2 { it * 2 }//传递一个函数
    println(arrayListOf(1, 2, 3).joinToString3(
        prefix = "{",
        postfix = "}",
        transform = { it.toString() }
    ))
    val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
    println(calculator(Order(3)))
    println(averageWindowsDuration)
    println(log.averageDurationFor(OS.MAC))
    //同时对IOS和Android的访问用户
    println(log.averageDurationFor { it.os in setOf(OS.IOS, OS.ANDROID) })

    val l = object : Lock {
        override fun lock() {

        }

        override fun lockInterruptibly() {
            TODO("Not yet implemented")
        }

        override fun tryLock(): Boolean {
            TODO("Not yet implemented")
        }

        override fun tryLock(time: Long, unit: TimeUnit?): Boolean {
            TODO("Not yet implemented")
        }

        override fun unlock() {
            TODO("Not yet implemented")
        }

        override fun newCondition(): Condition {
            TODO("Not yet implemented")
        }
    }
    synchronized(l){

    }
}
