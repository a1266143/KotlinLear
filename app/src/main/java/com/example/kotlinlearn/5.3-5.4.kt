package com.example.kotlinlearn

import android.view.View
import android.widget.Button
import android.widget.Toast

//对集合执行链式操作的时候，每执行一个操作可能会产生临时的集合对象，当集合数量足够多的时候，会对性能产生严重影响
//kotlin推荐使用惰性集合:序列来解决这种问题
fun testSequence() {
    val list = listOf(Persons("xiaojun", 27), Persons("qiuyue", 25))
    val names = list.filter { it.age > 25 }.map { it.name }//大于25岁的人的姓名的集合
    println("大于25岁的人的姓名的集合:$names")
    //上面的链式调用会在集合数量非常大的时候产生性能问题，下面采用序列来解决这种问题
    val namesList = list.asSequence()
            .filter { it.age > 25 }
            .map { it.name }//映射成名称序列
            .toList()
    //和上面的效果一样
    println(namesList)
    //需要注意的是,序列的执行顺序是一个元素一个元素的执行，而集合的同样操作是变换会产生一个新的集合然后传递给下一个链式函数，具体请参考书本章节<执行序列操作:中间和末端操作>
    //还有个需要注意的点是：链式调用的顺序不一样，也会影响程序的性能(比如：map变换和filter的顺序)
}

//创建序列
fun createSquence() {
    val naturalNumbers: Sequence<Int> = generateSequence(0) { it + 1 }
    val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
    println(numbersTo100.sum())//sum()方法才是末端操作，才会执行上面的lambda表达式
}

val runnable = Runnable {}

//测试函数式接口
fun testFunctionInterface() {
    val btn = Button(null)
    //通过下面这种方式，每次调用setOnClickListener都会新建一个匿名的OnClickListener对象
    btn.setOnClickListener(object : View.OnClickListener {
        override fun onClick(v: View?) {
            Toast.makeText(null, "按钮被点击了", Toast.LENGTH_SHORT).show()
        }
    })
    //通过lambda方式，lambda没有捕获外面的变量时，相当于新建了一个全局匿名OnClickListener对象
    btn.setOnClickListener {
        Toast.makeText(null, "按钮被点击了lambda", Toast.LENGTH_SHORT).show()
    }
    //通过lambda方式,如果捕获了外面函数的变量时,那么每次调用setOnClickListener都会新建一个匿名对象
    var localParam = "localParam"
    btn.setOnClickListener {
        localParam = "localParamBeCaptured"
        Toast.makeText(null, "按钮被点击了lambda,捕获了外部变量，每次都会新建一个匿名对象,localParam=$localParam", Toast.LENGTH_SHORT).show()
    }

}

//将lambda显示转换成函数式接口
//比如：一个函数需要返回一个Runnable类，可以返回SAM构造方法
fun createAllDoneRunnable(): Runnable {
    //不能直接返回lambda，需要返回函数式接口，下面就是SAM构造方法

    return Runnable { println("all done") }
    //上面的返回语句和下面的对象声明同义,但是上面的lambda SAM构造方法更简洁
    /*return object:Runnable{
        override fun run() {
            println("all done")
        }
    }*/
}

//lambda内部是没有this对象的，lambda中的this代指包裹它的类的对象
//匿名内部类中的this就是指匿名类的对象

