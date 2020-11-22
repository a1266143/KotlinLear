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

//可变参数关键字
fun tests(vararg param:String):String{
    for (str in param){
        println(str)
    }
    return ""
}

//使用infix关键字声明中缀调用
infix fun String.to(other:Any) = Pair(this,other)

//中缀调用
fun zhongzuiInvoke(){
    val hashMap = hashMapOf(1 to "one",2 to "Two",3 to "Three")
    for ((index,element) in hashMap){
        println("index=$index , element = $element")
    }
    val pairVal:Pair<String,Any> = "" to 1
}

//"解构声明"
//val (index,element) = 1 to "One"

fun main(args: Array<String>) {
//    println("Test".lastChar())
//    val listArray = listOf(1, 2.1, 3.5)
//    println(listArray.joinToString(separator = ":", prefix = "[", postfix = "]"))
//    val array = arrayOf(1,2,3)
    //将数组传递给可变参数
    //需要使用展开运算符*
//    val list = listOf("args: ",*array)
//    list.withIndex()
//    println(list)
//    tests("hahahahah","hahahaha","xixixixi")
    //测试中缀调用
    zhongzuiInvoke()
}