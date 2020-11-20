package com.example.kotlinlearn

import java.util.*

//while循环和do while循环（和java一样）

//区间的概念
//i表示 1(不包含) 到 100(包含)
//如果i既包含1又包含100，这个区间就叫做数列
val section = 1..100

//Fizz-Buzz游戏
fun fizzbuzzGame(i: Int) = when {
    i % 15 == 0 -> "Fizz-Buzz"
    i % 3 == 0 -> "Fizz"
    i % 5 == 0 -> "Buzz"
    else -> "$i"
}

fun main(args: Array<String>) {
//    println(fizzbuzzGame(10))
    //通过for循环在1到100的区间内玩Fizz-Buzz游戏
//    for(i in 1..100){
//        println(fizzbuzzGame(i))
//    }
    //倒序，并且只执行偶数的游戏
//    for (i in 100 downTo 1 step 2){
//        println(fizzbuzzGame(i))
//    }
    //通过until关键字迭代0-99
//    for (i in 0 until 100){
//        println(i)
//    }
    //或者
//    for (i in 0..99){
//        println(i)
//    }
//    iterationMap()
    iterationArrayList()
    println(inWithWhen('0'))
    setWithIn()
}

//使用for循环迭代Map
fun iterationMap() {
    val myMap = TreeMap<Char, String>()
    //..区间不仅可以使用Int，还可以使用字符区间
    for (c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.toInt())
        myMap[c] = binary
    }
    //开始迭代map
    for ((key, value) in myMap) {
        println("key=$key,value=$value")
    }
}

//使用for循环迭代ArrayList
fun iterationArrayList() {
    val list = arrayListOf("10", "11", "12")
    for ((index, element) in list.withIndex()) {
        println("index=$index element=$element")
    }
}

//使用in关键字检查值是否在区间内
//检查输入的i值是否在1到100的区间内
fun checkNumberInSection(i: Int): Boolean = i in 1..100

//使用!in检查值是否不在区间内
//检查输入的i值是否不在1到100的区间内
fun checkNumberNotInSection(i: Int): Boolean = i !in 1..100

//将in关键字使用于when表达式
fun inWithWhen(i:Char):String = when(i){
    in '0'..'9'->"字符位于区间0到9"
    in 'a'..'z',in 'A'..'Z'->"字符位于区间a-z或者A-Z"
    else -> "我不知道输入的值属于哪个区间ß"
}

//in检查也同样适用于集合,用于判断是否在集合内
fun setWithIn(){
    println("kotlin" in setOf("Java","Scala"))
}

//kotlin中的异常
//和java不同，throw结构在kotlin中是表达式(有值)