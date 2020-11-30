package com.example.kotlinlearn

import java.io.BufferedReader
import java.lang.NumberFormatException

//创建一个包含可空值的集合
fun readNumbers(reader: BufferedReader): List<Int?> {
    val result = ArrayList<Int?>()//创建包含可空Int值的列表
    for (line in reader.lineSequence()) {
        try {
            val number = line.toInt()
            result.add(number)
        } catch (e: NumberFormatException) {
            result.add(null)
        }
        //在kotlin1.1中可以像下面这样简化
//        result.add(line.toIntOrNull())
    }
    return result
}
//类型参数的可空性和变量自己类型的可空性是有区别的。。
//List<Int?>和List<Int>? ,前者是元素可能为空，后者是List集合本身可能为空

//使用可空值的集合
fun addValidNumbers(numbers: List<Int?>) {
    var sumOfValidNumber = 0
    var invalidNumbers = 0
    for (number in numbers) {
        if (number != null)
            sumOfValidNumber += number
        else
            invalidNumbers++
    }
    println("和为:$sumOfValidNumber")
    println("无效数字为:$invalidNumbers")
}

//可以使用标准库函数filterNotNull来简化上面的addValidNumbers函数
fun addValidNumbers2(numbers: List<Int?>) {
    val validNumbers = numbers.filterNotNull()//注意，filterNotNull函数会将类型转换为List<Int>
    println("和为:${validNumbers.sum()}")
    println("为空的数字的数量为:${numbers.size - validNumbers.size}")
}

//只读集合与可变集合
fun <T> copyElements(
    source: Collection<T>,
    target: MutableCollection<T>
) {
    for (item in source){
        target.add(item)
    }
}



fun main(args: Array<String>) {
    /*val list:ArrayList<Int?> = ArrayList<Int?>()
    list.add(null)
    list.add(null)
    list.add(1)
    list.add(2)
    list.add(3)
    addValidNumbers2(list)
//    addValidNumbers(list)*/
    /*val source :Collection<Int> = arrayListOf(3,5,7)
    val target:Collection<Int> = arrayListOf(1)
    copyElements(source,target)*/

}