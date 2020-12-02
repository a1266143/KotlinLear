package com.example.kotlinlearn

//高阶函数的控制流
data class Person83(val name:String,val age:Int)

val peoples = listOf(Person83("xiaojun",29),Person83("Bob",31))

fun lookForXiaojun(people:List<Person83>){
    for (person in people){
        if (person.name == "xiaojun"){
            println("找到了xiaojun")
            return
        }
    }
    println("xiaojun没找到")
}

//使用forEach扩展函数实现上面的例子
//这个例子说明了一个概念:非局部返回
//注意：只有在以lambda作为参数的函数是内联函数的时候才能从更外层的函数返回
fun lookForXiaojunForEach(people:List<Person83>){
    people.forEach{
        if (it.name == "xiaojun")
            return
    }
    println("未找到xiaojun")
}

//从lambda表达式返回
//可以通过标签@这种方式返回到表达式之前，比如
fun lookForXiaojunForEach2(people:List<Person83>){
    people.forEach label@{
        if (it.name == "xiaojun"){
            println("找到了xiaojun,开始返回到标签<label>的地方")
            return@label
        }
    }
    println("没找到xiaojun")//这里会始终被执行
}

//另一种是:以lambda作为参数的函数名作为返回的标签
fun lookForXiaojunForEach3(people:List<Person83>){
    people.forEach {
        if (it.name == "xiaojun"){
            println("找到了xiaojun，现在返回到lambda表达式所被调用的函数名的地方")
            return@forEach
        }
    }
    println("没找到xiaojun")//这里会始终被执行
}

//通过this@访问带接受者的lambda上下文对象
fun testLambdaWithAcceptor(){
    println(StringBuilder().apply label@{
        listOf(1,2,3).apply {
            //如果我想访问StringBuilder对象的实例，可以像下面这样访问
            this@label.append(this.toString())//this@label代表了StringBuilder的实例
        }
    })
}

//匿名函数
fun noNameMethod(){
    Person83("xiaojun",29).apply {
        //注意匿名函数没有名称，没有参数类型,但是可能会有参数名称(如果有参数的话)，也有返回类型
        fun():Unit{
            println("匿名函数代替lambda表达式作为函数参数传递")
            if (this.age == 29)
                //注意这里的return，匿名函数中的return都是返回到匿名函数的位置
                return
        }
    }
    //匿名函数为表达式体的时候可以不用写返回类型
    Person83("xiaojun",29).apply {
        fun()=println("haha")
    }
}

fun main(args:Array<String>){
    testLambdaWithAcceptor()
}