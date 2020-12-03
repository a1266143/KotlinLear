package com.example.kotlinlearn

//-----------------------变型-----------------------

fun printList(list: List<Any>) {
    println(list.joinToString())
}

fun addAnswer(list: MutableList<Any>) {
    list.add(42)
}

//检查一个类型是否是另一个的子类型
fun test(i:Int){
    val n:Number = i
    fun f(s:String){

    }
    //不能将i传递给局部函数f
//    f(i)
}

//子类型和子类在简单情况下意味着一样的事物
//可空类型提供了一个例子说明子类型和子类不是同一个事物
//A是A?的子类型，但是A?却不是A的子类型，但它们却对应着同一个类

//一个泛型类，对应任何两种类型A和B，既不是子类型也不是它的超类型，就被称为"在该类型参数上是不变型的"

//协变：如果A是B的子类型，那么List<A>就是List<B>的子类型，这样的类或接口被称为协变的
//协变：保留子类型化关系

//要声明泛型类在某个类型参数上是可以协变的，需要加上关键字（在类型参数的名称前）out
interface Producer<out T> {
    fun produce():T
}


fun main(args: Array<String>) {
    val mutableList = mutableListOf("1", "2")
    addAnswer(mutableList)
}