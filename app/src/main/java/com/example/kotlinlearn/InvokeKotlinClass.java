package com.example.kotlinlearn;

public class InvokeKotlinClass {

    public static int getScreenWidth(){
        return ScreenUtils.getScreenWidth();
    }

    /**
     * 此函数用于调用3.3-3.4.kt kotlin文件中的String扩展函数lastChar
     * java->kotlin
     */
    public static char extendsFunction(String str){
        return ExtendsFunctionClass.lastChar(str);
    }

}
