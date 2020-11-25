package com.example.kotlinlearn

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
}