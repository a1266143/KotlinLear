package com.example.kotlinlearn

import java.io.BufferedReader
import java.io.FileReader
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.LockSupport
import kotlin.concurrent.withLock

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
inline fun <T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}

//下面两个函数
fun foo(l: Lock) {
    println("同步之前")
    synchronized(l) {
        println("Action")
    }
    println("同步之后")
}

//编译后的foo函数
fun __foo__(l: Lock) {
    println("同步之前")
    l.lock()
    try {
        println("Action")
    } finally {
        l.unlock()
    }
    println("同步之后")
}

// 2020/12/2
// 内联:被内联的函数，函数体会被直接替换到被调用的地方，并且内联的lambda表达式也会被内联（替换到被调用的地方）
//因为lambda表达式会被正常编译成匿名类，每用一次lambda表达式，都会创建一个额外的类，这样就会影响性能
//并且如果lambda捕捉了某个变量，那么每次调用的时候都会创建一个新的对象

//在调用内联函数的时候也可以传递函数类型的变量作为参数
class LockOwner(val lock: Lock) {
    fun runUnderLock(body: () -> Unit) {
        //synchronized本身已经被内联
        //同时还可以传递函数类型的参数
        //这种情况下，body是不会被内联的，
        synchronized(lock, body)
        //如果传递的是lambda表达式，就会被内联
        synchronized(lock, { println("locked") })
        //当lambda表达式为最后一个参数的时候，可以将lambda表达式移动到小括号外面
        synchronized(lock) {
            println("locked")
        }
        //一般来说，lambda参数如果直接被内联函数调用，或者传递给另外一个inline函数，它是可以内联的
    }
}

class MySequence<T, R>(transform: ((T) -> R)) : Sequence<String> {
    override fun iterator(): Iterator<String> {
        return object : Iterator<String> {
            override fun hasNext(): Boolean = false

            override fun next(): String = "None"

        }
    }

}

//map函数并没直接调用lambda参数transform，而是传给了一个类的构造方法保存到属性中，
//为了支持这一点，transform会被编译成一个实现了函数接口的匿名类
fun <T, R> Sequence<T>.map(transform: (T) -> R): Sequence<R> {
    return MySequence(transform) as Sequence<R>
}

//有些函数可能有多个lambda参数，可以选择只内联其中一些参数，可以通过关键字noinline来标识不允许内联使用
//像下面这种
inline fun foo(inlined: () -> Unit, noinline notInlined: () -> Unit) {

}

data class Person8(val name: String, val age: Int)

val people = listOf(Person8("xiaojun", 29), Person8("qiuyue", 27))

fun testInlineArrayOperation() {
    println(people.filter {
        it.age < 29
    })
}

fun testInlineArrayOperation2() {
    val result = mutableListOf<Person8>()
    for (person in people) {
        if (person.age < 29) result.add(person)
    }
    println(result)
}

//withLock的使用
val lock:Lock = object:Lock{
    override fun lock() {

    }

    override fun tryLock(): Boolean = false

    override fun tryLock(time: Long, unit: TimeUnit?): Boolean = false

    override fun unlock() {

    }

    override fun lockInterruptibly() {

    }

    override fun newCondition(): Condition = object:Condition{
        override fun signal() {

        }

        override fun await() {

        }

        override fun await(time: Long, unit: TimeUnit?): Boolean=false

        override fun signalAll() {
        }

        override fun awaitNanos(nanosTimeout: Long): Long = 1L

        override fun awaitUninterruptibly() {
            TODO("Not yet implemented")
        }

        override fun awaitUntil(deadline: Date?): Boolean = false

    }

}

fun testWithLock(){
    lock.withLock {
        //将需要加锁的操作放入此模块
    }
}

fun readFirstLineFromFile(path:String):String{
    BufferedReader(FileReader(path)).use {
        return it.readLine()
    }
}

fun main(args: Array<String>) {
//    testInlineArrayOperation()
    testInlineArrayOperation2()
//    testMethodTypeParam()
//    println("abcdef".filter { it > 'c' })
//    methodTypeParam2()//使用默认的函数参数
//    methodTypeParam2 { it * 2 }//传递一个函数
//    println(arrayListOf(1, 2, 3).joinToString3(
//        prefix = "{",
//        postfix = "}",
//        transform = { it.toString() }
//    ))
//    val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
//    println(calculator(Order(3)))
//    println(averageWindowsDuration)
//    println(log.averageDurationFor(OS.MAC))
//    //同时对IOS和Android的访问用户
//    println(log.averageDurationFor { it.os in setOf(OS.IOS, OS.ANDROID) })

}
