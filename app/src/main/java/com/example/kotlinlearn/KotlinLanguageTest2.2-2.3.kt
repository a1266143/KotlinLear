package com.example.kotlinlearn

//-----------------------------2.2节 和 2.3节 内容----------------------------

//类的练习
class Person(val name: String)

//声明属性，val有getter，var有setter和getter
class Person2(val name: String, var isMarried: Boolean)

/*fun main(args: Array<String>) {
    println(testEnum(Color2.GREEN))
}*/

fun testPerson2() {
    //kotlin中，new会被省略
    val person2 = Person2("lixiaojun", true)
    //访问类中的属性
    person2.isMarried = false;
    println("person2.name=${person2.name},person2.isMarried=${person2.isMarried}")
}

val isSquare = true

//声明一个矩形类
class Rectangle(val height: Int, val width: Int) {
    val isSquare: Boolean
        get() {
            return height == width
        }
}

//声明一个枚举类
enum class Color {
    RED, GREEN, ORANGE
}

//枚举类可以声明属性
enum class Color2(val r: Int, val g: Int, val b: Int) {
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255);

    //在枚举类中声明一个函数
    fun testFunction(): Boolean {
        return false;
    }
}

//使用枚举用于when(switch)判断
fun testEnum(color: Color2) = when (color) {
    Color2.RED, Color2.GREEN -> "red or green"
    Color2.BLUE -> "blue"
}

fun main(args:Array<String>){
    println(testEnum2(Color2.RED,Color2.BLUE))
    println(optimizeTestEnum2(Color2.BLUE,Color2.RED))
}

fun testEnum2(c1:Color2,c2:Color2)=
    when(setOf(c1,c2)){
        setOf(Color2.GREEN,Color2.BLUE)->"green and blue"
        setOf(Color2.RED,Color2.GREEN)->"red and green"
        setOf(Color2.RED,Color2.BLUE)->"red and blue"
        else->"noColor"
    }

//因为每次调用testEnum2都会新建对象(setOf会创建Set集合对象),所以这里对when表达式进行性能优化，可读性会变差
fun optimizeTestEnum2(c1:Color2,c2:Color2)=
    when{
        (c1==Color2.GREEN&&c2==Color2.BLUE)||(c1==Color2.BLUE&&c2==Color2.GREEN)->"green and blue"
        (c1 == Color2.RED&&c2==Color2.GREEN)||(c1==Color2.GREEN&&c2==Color2.RED)->"red and green"
        (c1==Color2.RED&&c2==Color2.BLUE)||(c1==Color2.BLUE&&c2==Color2.RED)->"red and blue"
        else->"noColor"
    }

fun calculate(a:Int,b:Int,c:Int):Int=(a+b)+c

//智能转换
interface Expr
class Num(val value:Int):Expr
class Sum(val left:Expr,val right:Expr):Expr

fun eval(e:Expr):Int{
    if (e is Num){
        val n = e as Num
        return n.value
    }
    if (e is Sum){
        //下面使用的e进行了智能转换，
        //在kotlin中，通过"is"判断过是某种类型后，后面使用就不需要像java一样声明一个变量并强转成这个变量的引用了，
        //这就是智能转换
        return eval(e.left)+eval(e.right)
    }
    return 0
}

//通过 if语句重新上面的eval函数
fun evalIf(e:Expr):Int=
    if (e is Num)
        e.value
    else if (e is Sum)
        eval(e.left)+eval(e.right)
    else
        0

//通过when语句进一步重新evalIf函数
fun evalWhen(e:Expr):Int=
    when(e){
        is Num -> e.value
        is Sum -> eval(e.left)+ evalWhen(e.right)
        else -> 0
    }

//代码块作为if和when的分支
fun evalWithLogging(e:Expr):Int =
    when(e){
        is Num -> {
            println(e.value)
            e.value
        }
        is Sum -> {
            val left = evalWithLogging(e.left)
            val right = evalWithLogging(e.right)
            println("sum=$left+$right")
            left+right
        }
        else->1
    }