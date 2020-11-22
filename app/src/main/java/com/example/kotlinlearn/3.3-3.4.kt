@file:JvmName("ExtendsFunctionClass")

package com.example.kotlinlearn

//扩展函数
//在String类外部重新为String添加一个成员函数lastChar
fun String.lastChar(): Char {
    return get(this.length - 1)
}

//重写joinToString的终极版本
/*fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = " ",
    postfix: String = " "
): String {
    val result = StringBuilder(prefix)
    for((index,element) in this.withIndex()){
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}*/

//重写特定类型的joinToString函数
fun Collection<Int>.joinToString(
    separator: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

//声明扩展属性
val String.lastChar: Char
    get() = get(this.length - 1)

fun main(args: Array<String>) {
    println("Test".lastChar())
    val listArray = listOf(1, 2.1, 3.5)
    println(listArray.joinToString(separator = ":", prefix = "[", postfix = "]"))
}