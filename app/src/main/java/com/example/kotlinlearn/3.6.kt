package com.example.kotlinlearn

fun test2(){
    saveUser(User(1,"lixiaojun","东顺园"))
}

class User(val id:Int,val name:String,val address:String)

fun saveUser(user:User){
    //声明局部函数
    fun validate(value:String,fieldName:String){
        if (value.isEmpty()){
            throw IllegalArgumentException("不能保存用户${user.id}:空$fieldName")
        }
    }
    validate(user.name,"Name")
    validate(user.address,"Address")
    //保存到数据库
    user.validateBeforeSave()
}

//将saveUser()函数提取逻辑到扩展函数
fun User.validateBeforeSave(){
    fun validate(value:String,fieldName:String){
        if (value.isEmpty()){
            throw IllegalArgumentException("不能保存用户${this.id}:空$fieldName")
        }
    }
    validate(name,"Name")
    validate(address,"Address")
}
