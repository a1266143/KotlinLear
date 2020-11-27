package com.example.kotlinlearn

data class Persons(val name: String, val age: Int)

//将lambda作为函数参数
fun lambdaAsParam(param: String) {
    val people = arrayListOf(Persons("xiaojun", 29), Persons("qiuyue", 27))
    val person = people.maxBy { it.age }
    println("maxAgePerson.name=${person?.name}")
    //如果不用任何简明语法来调用maxBy方法将是下面这种形式
    people.maxBy({ p: Persons -> p.age })
    //如果lambda表达式是函数的最后一个实参，可以将lambda表达式放在括号的外边
    people.maxBy() { p: Persons -> p.age }
    //如果lambda表达式是函数的唯一实参，还可以将括号去掉
    people.maxBy { p: Persons -> p.age }

    //
    val names = people.joinToString(";", transform = { p: Persons ->
        p.name//返回p.name
    })
    //因为joinToString函数的最后一个参数是lambda表达式，所以可以将其放到括号外面
    val names2 = people.joinToString(separator = ";") { p: Persons -> p.name }
    //省略lambda参数类型
    people.joinToString(separator = ";") { p -> p.name }
    //最终的简化：使用默认参数名称it代替命名参数p,
    //如果lambda只有一个参数并且编译器能够推断出这个参数的类型，就会生成这个名称:it
    //仅在实参名称没有被指定的时候，这个默认名称才会生成
    people.joinToString { it.name }
    println(names)
    println(names2)
    //lambda表达式不仅仅只有一条语句或一条表达式
    //当lambda表达式由多条语句组成时，最后一句会作为lambda的结果
    val sum = { p: Persons ->
        println("这是Lambda中的第一条语句")
        println("这是Lambda中的第二条语句")
        p.age//最后一句会作为lambda的结果
    }

    val localParam = "localParamName"
    //在函数中声明一个匿名内部类,并访问函数中的实参和局部变量
    object : MyInterface {
        override fun click() {
            println("在函数中声明了一个匿名内部类")
            //在匿名内部类的内部可以引用函数的参数和局部变量
            println("引用函数的参数,param=$param")
            //在匿名内部类引用函数的局部变量
            println("引用函数的局部变量,localParam=$localParam")
        }
    }

    //同样，在lambda也可以访问函数的实参和局部变量
    people.forEach {
        //访问包裹函数的实参
        println("param=$param")
        //访问包裹函数的局部变量
        println("param=$localParam")
        println(it.age)//最后一句作为结果
    }
}

fun printProblemCounts(responses: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    responses.forEach {
        if (it.startsWith("4")) {
            clientErrors++
        } else if (it.startsWith("5")) {
            serverErrors++
        }
    }
    println("$clientErrors client errors,$serverErrors server errors")
}

fun testPrintProblemCounts() {
    val responses = listOf("200 OK", "418 I'm a teapot", "500 Internal Server Error")
    printProblemCounts(responses)
}

//lambda表达式(意味着有值)可以作为值传递
//下面展示了声明lambda表达式的语法
val sum = { x: Int, y: Int -> x + y }

//调用的时候可以像调用普通函数一样
fun invokeLambda() {
    println(sum(10, 15))
    //可以执行库函数run传递给它lambda
    run { println(42) }
}

fun collectionsFilterTest() {
    val list = listOf(1, 2, 3, 5)
    //filter函数会遍历所在的集合并且选出给定lambda后返回true的那些元素
    println(list.filter { it % 2 == 0 })
    val people = listOf<Persons>(Persons("xiaojun", 29), Persons("leike", 32))
    //选出大于30岁的人
    println(people.filter { it.age > 30 })
}

//通过集合的map方法来将原有的集合变换为新的集合
//通过map变换列表
fun collectionsMapTest() {
    val list = listOf(1, 2, 3, 4)
    //集合将变为[2,4,6,8]
    println(list.map { it * 2 })
    val listPerson = listOf<Persons>(Persons("xiaojun", 29), Persons("qiuyue", 27))
    //注意，下面将返回一个List<String>列表
    listPerson.map { it.name }
    //上面的例子可以使用成员引用(TODO 2020/11/27 14:10成员引用还未完成)来重写
    listPerson.map(Persons::name)
    //可以将map和filter的调用链接起来，形成链式调用
    //下面将返回年龄大于30岁的Person，然后返回姓名列表
    listPerson.filter { it.age > 30 }.map { it.age }

    //还可以对map应用filter和map
    val map = mapOf(1 to Persons("xiaojun", 29), 2 to Persons("qiuyue", 27))
    println(map.filter { it.value.age > 27 }.map { it.value.name.toUpperCase() })
}

val canBeInClub27 = { p: Persons -> p.age <= 27 }

//测试集合"all,any,count,find"函数
fun testAllAnyCountFind() {
    //通过all函数(Collections.all函数)判断列表是否都满足lambda表达式的内容
    val list = listOf(Persons("xiaojun", 27), Persons("qiuyue", 18))
    val listSat: Boolean = list.all(canBeInClub27)//返回是否list列表中所有元素都满足年龄<=27,是的话返回true
    println("list所有元素都满足<年龄<=27岁>,结果=$listSat")
    //通过any函数(Collections.any函数)判断集合中至少存在一个匹配的元素
    val resultAny: Boolean = list.any(canBeInClub27)
    println("list至少有一个元素满足<年龄<=27岁>，结果=$resultAny")
    //注意使用!all(不是所有)和any加上条件取反是一样的意义
    //通过函数count来知道有多少个元素满足判断式
    val count: Int = list.count(canBeInClub27)
    println("list中有$count 个人满足年龄<=27岁")
    //通过find函数来找到集合中的第一个满足条件的元素
    val personSatisfy: Persons? = list.find(canBeInClub27)//注意：可能返回null
    println("<=27岁的第一个人的姓名为:${personSatisfy?.name}")
    //函数firstOrNull和find函数同义
    val personSatisfy2 = list.firstOrNull(canBeInClub27)
    println("<=27岁的第一个人的姓名为:${personSatisfy2?.name}")
}

//测试集合"groupBy"函数
fun testGroupBy() {
    val list = listOf(Persons("xiaojun", 27), Persons("qiuyue", 27), Persons("leike", 30), Persons("jiaojing", 29))
    //通过Persons.age年龄相同的分类
    //结果会是一个Map<Key,Value>，其中key的类型为it.age的类型(Int),value的类型将会是对象类型(Persons)
    println(list.groupBy { it.age })
    val listStr = listOf("a", "ab", "c")
    //按照首字母分组
    println(listStr.groupBy(String::first))//通过成员引用类分类
}

data class Book(val bookName: String, val authors: List<String>)

//测试flatMap和flatten(flatten函数貌似不能使用，不知道是不是新版本的kotlin被移除了)
//当需要处理 集合中的集合 需要合并成一个集合的时候，可以使用flatMap来平铺成最后需要的集合
fun testFlatMapFlatten() {
    val books = listOf(Book("kotlin实战", authors = listOf("lee", "zhang", "pang")),
            Book("java实战", listOf("pang", "lei")))
    //返回所有作者名称集合，注意:Set不包含重复元素，所以两本书中的pang作者只会出现一次
    val authors: Set<String> = books.flatMap { it.authors }.toSet()
    println(authors)
}

fun main(args: Array<String>) {
    lambdaAsParam("传递的实参")
    println(invokeLambda())
}