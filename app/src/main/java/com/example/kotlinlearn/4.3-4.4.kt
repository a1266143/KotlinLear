package com.example.kotlinlearn

import java.io.File

class Client(val name: String, val postalCode: Int) {
    //重写Client类的toString方法
    override fun toString(): String = "Client(name=$name,postalCode=$postalCode)"

    //重写Client类的equals方法
    //kotlin中的Any和Java中的Object一样，Any？表示可空运算符,表明变量other可以为空
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Client)
            return false
        return name == other.name && postalCode == other.postalCode
    }

    //重写copy方法
    fun copy(name: String = this.name, postalCode: Int = this.postalCode) = Client(name, postalCode)

    override fun hashCode(): Int = name.hashCode() * 31 + postalCode
}

//数据类，关键字:data
//数据类会自动将toString(),equals(),hashCode方法生成好
data class ClientDataClass(val name: String, val postalCode: Int)

//copy方法，生成类的副本

//类委托
//比如装饰模式就会将接口实现的方法，委托给真正持有的对象，如下代码
class DelegatingCollection<T> : Collection<T> {

    //真正的对象，将Collection接口的实现，最终都委托给innerList
    private val innerList: ArrayList<T> = arrayListOf()

    override val size: Int
        get() = innerList.size

    override fun contains(element: T): Boolean {
        return innerList.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return innerList.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return innerList.isEmpty()
    }

    override fun iterator(): Iterator<T> {
        return innerList.iterator()
    }
}

//通过by关键字将接口的实现委托到另一个对象
class DelegatedCollection<T>(innerList: Collection<T> = ArrayList<T>()) :
    Collection<T> by innerList {
    //重写后会调用重写后的方法
    override fun contains(element: T): Boolean {
        return false
    }
}

//使用类委托
class CountingSet<T>(
    val innerSet: MutableCollection<T> = HashSet<T>()
) : MutableCollection<T> by innerSet {
    var objectsAdded = 0

    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectsAdded += elements.size
        return innerSet.addAll(elements)
    }
}

//对象声明：创建单例模式(使用object关键字)
object Payroll {
    val allEmployees = arrayListOf<Person>()
    fun calculateSalary() {
        for (person in allEmployees) {
            println("person.name=${person.name}")
        }
    }
}

object CaseInsensitiveFileComparator : Comparator<File> {
    override fun compare(o1: File, o2: File): Int {
        return o1.path.compareTo(o2.path, ignoreCase = true)
    }
}

//在类中也可以使用对象声明，它们在每个容器类的实例中也只是一个对象，并不会不同
data class PersonDataClass(val name: String) {
    init {
        println("调用了PersonDataClass的主构造方法")
    }

    //在类内部进行对象声明
    object NameComparator : Comparator<PersonDataClass> {
        override fun compare(o1: PersonDataClass, o2: PersonDataClass): Int =
            o1.name.compareTo(o2.name, ignoreCase = true)
    }
}

//伴生对象
//为什么需要伴生对象:
//1.kotlin不能使用static关键字(kotlin采用顶层函数和对象声明来替代static)
//2.但是，顶层函数不能访问类的private成员，所以伴生对象的概念就产生了
//伴生对象通过关键字companion object
//伴生对象可以包含名称并且实现接口

class A {
    companion object {
        //表明是共生对象
        fun bar() {
            println("Companion object called")
        }
    }
}

//通过伴生对象可以调用私有构造方法的工厂类
class UserFactory private constructor(val nickname: String) {
    init {
        println("UserFactory私有构造方法被调用")
    }

    companion object {
        fun newInstance(userName: String): User =
            if (userName.equals("lixiaojun")) User(0, "lixiaojun", "东景丽舍") else User(
                -1,
                "NoName",
                "NoAddress"
            )
    }
}

//伴生对象可以拥有名字
class UserFactory2 {
    companion object INSTANCE {
        fun newInstance(name: String) = if (name == "lixiaojun") User(
            id = 0,
            name = "xaiojun",
            address = "dongshunyuan"
        ) else User(-1, "noName", "noAddress")
    }
}

//伴生对象中实现接口
interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}

class Person3(val name: String) {
    companion object : JSONFactory<Person3> {
        override fun fromJSON(jsonText: String): Person3 = Person3("lixiaojun")
    }
}

//此时如果有这样一个抽象方法，实际上是可以传递伴生对象所在包裹类的类名即可
fun <T> loadFromJSON(factory: JSONFactory<T>): T = factory.fromJSON("lxiaiojun")

class Car(val brandName: String)

//在类外部定义伴生对象的扩展函数
class CarFactory {
    //定义一个空的伴生对象
    companion object {

    }
}

//在外部定义CarFactory的伴生对象的扩展函数
fun CarFactory.Companion.newInstance(brand: String): Car =
    if (brand == "baoma") Car("宝马") else Car("其它车")

//对象表达式:通过object关键字来声明匿名对象
//和对象声明不同，对象声明是单例的，对象表达式每次都生成一个新的对象
interface OnClickListener {
    fun onClick()
}

fun method_testCallback(listener: OnClickListener) {
    listener.onClick()
}

fun test222() {
    var counter = 0

    method_testCallback(
        object : OnClickListener {
            override fun onClick() {
                counter++
                println("被点击了 $counter 次")
            }
        }
    )
}

fun main(args: Array<String>) {
    println(Client("xiaojun", 600004))

    //关于hashCode契约:如果两个对象相等，那么它们的hash值一定相等
    val hashSet = hashSetOf(Client("xiaojun", 1))
    if (hashSet.contains(Client("xiaojun", 1))) println("含有Client") else println("不含有Client")

    val client = Client("xiaojun2", 2)
    val clientCpy = client.copy()

    println("clientCpy.name=${clientCpy.name},clientCpy.postalCode=${clientCpy.postalCode}")

    println(ClientDataClass("lixiaojun", 2))
    DelegatedCollection<String>(setOf())
    //调用单例
    Payroll.allEmployees.add(Person("xiaojun"))
    Payroll.calculateSalary()
    println(CaseInsensitiveFileComparator.compare(File("/sdcard/file1"), File("/sdcard/file1")))

    val persons = listOf<PersonDataClass>(PersonDataClass("Aname"), PersonDataClass("Bname"))
    println(persons.sortedWith(PersonDataClass.NameComparator))
    A.bar()
    val userLxj = UserFactory.newInstance("lixiaojun")
    val userOther = UserFactory.newInstance("no")
    println("lxj=${userLxj.name},no=${userOther.name}")

    //伴生对象默认的名称为Companion
    val user = UserFactory2.INSTANCE.newInstance("lixiaojun")
    println("user.name=${user.name}")
    println("user.address=${user.address}")
    val person3 = loadFromJSON(Person3)
    println("person3.name=${person3.name}")

    val baoma = CarFactory.newInstance("baoma")
    val qitache = CarFactory.newInstance("baojun")
    println("car.brandName=${baoma.brandName},qitache=${qitache.brandName}")

    test222()
}