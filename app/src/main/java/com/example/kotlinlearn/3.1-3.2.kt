package com.example.kotlinlearn

//创建HashSet并初始化三个元素
val set = hashSetOf(1, 3, 7)

//创建ArrayList
val arrayList = arrayListOf(1, 2, 3)

//创建一个HashMap
val hashMap = hashMapOf(1 to "one", 2 to "two", 3 to "three")

//获取列表中的最后一个数
fun test() {
    println(set.last())
    println(arrayList.last())
    println(set.max())
    println(set.min())
}

//重写toString()方法
fun <T> joinToString(
    collection: Collection<T>,
    separator: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)
    for((index,element) in collection.withIndex()){
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

//重新toString()方法2
//支持默认参数
fun <T> joinToString2(
    collection:Collection<T>,
    separator:String = ",",
    prefix:String = "[",
    postfix:String = "]"
):String{
    val sb = StringBuilder(prefix)
    for ((index,element) in collection.withIndex()){
        if (index > 0)sb.append(separator)
        sb.append(element)
    }
    sb.append(postfix)
    return sb.toString()
}

fun main(args:Array<String>){
    val list = listOf("A","B","C")
    println(joinToString(collection = list, separator = ";", prefix = "(", postfix = ")"))
    println(joinToString2(collection = list,prefix = "(",postfix = ")",separator = ",,,"))
}