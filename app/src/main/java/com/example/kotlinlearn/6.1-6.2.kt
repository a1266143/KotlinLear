package com.example.kotlinlearn

import android.view.View
import android.widget.TextView
import java.lang.IllegalArgumentException

//--------------------可空类型------------------------

//下面这种常规写法，如果传递null会在编译时报错
fun strLenSafe(str: String) = str.length

//如果允许传递null给strLen函数，可以使用下面的形式
fun strLen(str: String?): Int? = str?.length//使用安全运算符?.访问属性

fun printAllCaps(s: String?) {
    val allCaps: String? = s?.toUpperCase()//使用安全运算符访问方法
    println(allCaps)
}

//安全调用运算符还可以用来访问属性
class Employee(val name: String, val manager: Employee?)

//通过安全调用运算符访问Emplyee实例的name属性
fun managerName(employee: Employee): String? = employee.manager?.name

//链接多个安全调用
class Address(val streetAddress: String, val zipCode: Int, val city: String, val country: String)

class Company(val name: String, val address: Address?)

class PersonNew(val name: String, val company: Company?)

//PersonNew扩展方法
fun PersonNew.countryName(): String {
    val country = this.company?.address?.country
    //使用EIvis运算符
    return null ?: ""
//    return if (country != null) country else "Unknow"
}

//EIvis运算符通常和安全调用运算符结合使用
fun eivisAndSafeInvokeOperator(str: String?): Int =
//从左往右解释为:
// 如果str不为空，?.安全调用运算符就返回str.length，因为?:(EIvis运算符)左边不为null，所以直接返回str.length
    //如果str为空，?.安全调用运算符就返回null,因为?:(EIvis运算符)左边为null，所以会返回?:运算符右边的值0
    str?.length ?: 0

fun PersonNew.countryName2() =
//解释:
// 1.如果安全调用company为空,就返回null,表达式变为: null?.country?:"Unknow"
    //2.最终调用为null?:"Unknow"(EIvis运算符:如果运算符左边为null,就返回运算符右边的值)
    company?.address?.country ?: "Unknow"

//在kotlin中，return和throw语句是表达式
fun eivisWithReturnAndThrow(person: PersonNew?): String {
    person ?: throw IllegalArgumentException("Person为空")
    person.company ?: throw IllegalArgumentException("Company为空")
    person.company.address ?: throw IllegalArgumentException("Address为空")
    with(person) {
        println("人员名称:$name")
        with(person.company) {
            println("Company.name=${name}")
        }
    }
    return person.name
}

//安全转换"as?"
class PersonNew2(val firstName: String, val lastName: String) {
    override fun equals(other: Any?): Boolean {
        val otherPerson = other as? PersonNew2 ?: return false
        return otherPerson.firstName == this.firstName &&
                otherPerson.lastName == this.lastName
    }

    override fun hashCode(): Int = firstName.hashCode() * 37 + lastName.hashCode()
}

//非空断言:"!!"
fun notNullAssert(person: PersonNew2?) {
//    val person = PersonNew2("xiaojun","li")
    //因为非空断言抛出异常时只会抛出哪一行，所以尽量避免在同一行中使用多个非空断言
    println("person.lastName=${person!!.lastName}")
}

//将可空类型作为实参传递给只接收非空值的函数
fun test(value: String) {
    println("test函数,value=$value")
}

//返回一个Person对象的方法,返回值可能为null
fun generatePerson(): PersonNew2? {
    return PersonNew2("xiaojun", "li")
}

//可空类型的扩展函数
//扩展函数是可以接收可空类型(?)的
//普通的成员函数是不能直接调用
fun verifyUserInput(input: String?) {
    if (input.isNullOrBlank())//这个函数并没有使用安全调用,因为是String的扩展函数
        println("请输入可用的字符")
    //这里之所以能够直接调用size函数
    //是因为下面定义的可空扩展函数允许接收者为null
    println(input.size())
}

//定义机子的可空扩展函数
//比如：定义String的可空扩展函数
fun String?.size(): Int {
    //注意，this可能为null，java中的this不可能为null
    return this?.length ?: 0
}

//类型参数<T>的可空性
//泛型中的类型参数是可空的
fun <T> printHashCode(t: T) {//t可能为null，尽管没有显示标明为可空类型?
    //之所以这里没有使用安全调用?.hashCode方法
    //是因为hashCode方法是  可空扩展函数(允许接收者<调用方法的实例>为null)
    println(t.hashCode())
    println(t?.hashCode())
}

//要使类型参数非空，必须要指定一个非空的上界
fun <T : Any> printHashCodeNotNull(t: T): Int {//现在T就不是可空的
    println(t.hashCode())
    return t.hashCode()
}

//延迟加载
class TestTest {
    lateinit var mTextView: TextView
    fun init() {
        val view = View(null)
        mTextView = view.findViewById(0)
    }
}

//kotlin的基本类型和基本类型可空性
data class PersonNewNew(val name: String, val age: Int? = null) {
    fun isOlderThan(other: PersonNewNew): Boolean? {
        if (age == null || other.age == null)
            return null
        return age > other.age
    }
}

//数字转换
fun numberConvert(){
    val x = 1
    println(x.toLong() in listOf(1L,2L,3L))
    //kotlin不会进行隐式转换，需要进行显式转换这些变量
//    println(x in listOf(1L,2L,3L))
}

//kotlin中的Unit类型：相当于java的void
fun f():Unit{

}
//f2函数和f函数的含义一模一样
fun f2(){

}

//Unit的应用：作为泛型参数类型，返回值是Unit的时候，不用指定return，会隐式return Unit
interface Processor<T> {
    fun process():T
}

class NoResultProcessor:Processor<Unit>{
    override fun process() {//这里接口中的返回类型可以省略掉，因为是Unit类型
        //返回值也可以不用写,因为是Unit类型
    }

}

//Nothing类型:从不返回的类型，可以用作编译器的推断以发现问题(不过好像也没啥用？)
fun fail(message:String):Nothing{
    throw IllegalStateException(message)
}

fun testFail(){
    val address = Company("Stork",null).address?: fail("No address")
    println(address.city)
}


fun main(args: Array<String>) {
//    println(strLen(null))
//    println(strLen("abc"))
//    printAllCaps(null)
//    val ceo = Employee("Da Boss", null)
//    val developer = Employee("Bob Smith", ceo)
//    println(managerName(ceo))
//    println(managerName(developer))
//    val lee = PersonNew("Lixiaojun", null)
//    val person = PersonNew("qiuyue",Company("Stork",Address("chuangyeStreet",50000,"chengdu","Sichuan")))
//    println("Person=${lee.name} CountryName=${lee.countryName()}")
//    println("Person=${person.name} CountryName=${person.countryName()}")
    //测试?.运算符和?:运算符
//    println(eivisAndSafeInvokeOperator("lixiaojun"))
//    println(eivisAndSafeInvokeOperator(null))
//    println(PersonNew("lixiaojun",null).countryName2())
//    println(PersonNew("lixaiojun",Company("Stork",Address("chuangyeStreet",6000,"cd","Sichuan"))).countryName2())
//    eivisWithReturnAndThrow(null)
//    eivisWithReturnAndThrow(PersonNew("lixiaojun", Company("Stork",null)))
//    eivisWithReturnAndThrow(PersonNew("lixiaojun", Company("Stork", Address("ChuangYeStreet", 5000, "Chegndu", "Sichuan"))))
//    val p1 = PersonNew2("xiaojun", "lee")
//    val p2 = PersonNew2("xiaojun", "lee")
//    println(p1 == p2)
//    println(p1.equals(42))
//    notNullAssert(null)
    /*var value:String? = null
    //value为null时,let中的lambda表达式不会被调用
    value?.let {
        test(it)
    }
    value = "10"
    value?.let{
        test(it)
    }*/

    /*val person = generatePerson()
    person?.let {
        println("姓名为:${it.lastName} ${it.firstName}")
    }
    //上面这种使用方式可以简写成下面这种方式
    generatePerson()?.let {
        println("姓名为:${it.lastName} ${it.firstName}")
    }*/
//    verifyUserInput(null)
//    println("hashCode为:${printHashCodeNotNull("ha")}")
//    val person1 = PersonNewNew("xiaojun",28)
//    val person2 = PersonNewNew("qiuyue",26)
//    println(person1.isOlderThan(person2))
//    println(person1.isOlderThan(PersonNewNew("huangba")))
    numberConvert()
    testFail()
}