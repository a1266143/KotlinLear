package com.example.kotlinlearn

import android.content.Context
import android.util.AttributeSet

//声明接口
interface MyInterface {
    fun click()
}

//实现接口
class MyImp : MyInterface {
    override fun click() {
        println("click be invoke by MyImp")
    }
}

//接口中的默认带有实现的方法(java中需要使用default关键字)
interface View {
    fun click()

    //默认带实现的函数
    fun getId(): Int = 1
}

//如果实现了两个接口，并且两个接口中有同样的一个默认函数
interface View2 {
    fun getId(): Int = 2
}

//必须要实现同名的函数
class SubView : View, View2 {
    override fun click() {
    }

    override fun getId(): Int {
        println("View.getId=${super<View>.getId()}")
        println("View2.getId=${super<View2>.getId()}")
        return 3
    }

}

//在kotlin中，默认都是final，不能被子类重写，如果需要被子类重写，需要使用open关键字标注<属性>或者<方法>
open class MyClass {
    open fun method() {//此方法可以被子类重写

    }

    fun method2() {//此方法不能被重写

    }
}

open class SubClass : MyClass() {
    //可以重写的方法只有method()方法
    final override fun method() {//重写的方法默认也是open的，如果想要不再被重写，可以使用final标记方法
        super.method()
    }
}

class GrandSunClass : SubClass() {
    //不能重写任何方法
}

//kotlin中的抽象类，抽象方法默认是open的，但是抽象类中的非抽象方法不是默认open的
abstract class MyAbstractClass {
    //抽象类默认是open的，必须被重写
    abstract fun method()
    open fun method1() {//open方法可以被子类重写
        println("method1() be invoked")
    }
}

//内部类，默认嵌套类和java中的刚好相反，默认定义的内部类在java中是隐式含有外部包裹类的引用的，但是在kotlin中默认定义的内部类需要加上关键字static才是这样
//在kotlin中如果想和java的默认内部类行为一样，需要使用关键字inner
class ClassOut {

    class InnerClass0 {//默认是不持有外部包裹类的隐式引用的

    }

    inner class InnerClass1 {
        //使用inner关键字标记的类是持有外部包裹类的隐式引用的
        //引用外部类实例的示例
        fun getOuterObj(): ClassOut = this@ClassOut
    }
}

//嵌套类和内部类的区别
//嵌套类是不含有外部包裹类的引用的
//内部类是含有外部包裹类的引用的

//关键字sealed:用于表明一个类是密封类
//sealed标记的类说明：这个类的子类只能在此类的内部定义
//Kotlin1.1中已经允许在同一个kotlin文件中的任意位置定义子类
//并且sealed标记的类默认是open的
sealed class Father {
    class Son : Father()
    class GrandSon : Father()
}

class GrandGrandSon : Father() {

}

//构造方法
class User2(nickname: String) {
    fun test() {
        println("test")
    }
}

//带一个参数的主构造方法
class User3 constructor(nickname: String) {
    val _nickname: String

    init {
        _nickname = nickname
        println("nickname setted = $_nickname")
        test()
    }

    //声明从构造方法
    constructor(nickname: String, age: Int) : this(nickname) {
        println("age=$age")
        test()
    }

    private fun test() {
        println("test")
    }
}

//为构造函数增加默认值
open class User4 {
    lateinit var _nickname: String

    constructor(nickname: String = "lixiaojun") {
        this._nickname = nickname
    }

    constructor(nickname: String, age: Int) {
        println("nickname=$nickname,age=$age")
    }
}

//拥有private的构造方法
class User5 private constructor() {}

open class Views {
    constructor(context: Context) {
        //some code
    }

    constructor(context: Context, attr: AttributeSet) {
        //some code
    }
}

class MyButton : Views {
    constructor(context: Context) : super(context)

    //kotlin中也可以使用this关键字将构造方法委托给另一个构造方法
    constructor(context: Context, nickname: String) : this(context)

    constructor(context: Context, attr: AttributeSet) : super(context, attr)
}

//实现在接口中声明的属性
interface User9 {
    val nickname: String
}

class FacebookUser(override val nickname: String) : User9 {

}

//TODO 类属性的支持字段
class People {
    var name: String = "unspecified"
        set(value) {
            println("调用了People.name.set,value=$value")
            field = value
        }
    var sex: String = "男"
        set(value) {
            println("sex was setted")
            field = value
        }
        get() {
            println("你访问了People类的sex")
            return field
        }
}

//修改属性访问器的可见性
class LengthCounter {
    var counter: Int = 0
        private set //只能在本类中被修改

    fun addWord(word: String) {
        counter += word.length
    }
}

fun main(args: Array<String>) {
//    User2(nickname = "lixiaojun").test()
//    MyImp().click()
//    SubView().getId()
//    User3("lixiaojun", 29)
//    User4("zhangsan")
//    User4("lixiaojun",29)
//    val people = People()
//    people.sex = "女"
//    println("sex=${people.sex}")
    val lengthCounter = LengthCounter()
    lengthCounter.addWord("X")
    lengthCounter.addWord("I")
    lengthCounter.addWord("A")
    lengthCounter.addWord("O")

    println("counter = ${lengthCounter.counter}")
}
