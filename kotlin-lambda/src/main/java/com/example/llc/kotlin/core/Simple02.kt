package com.example.llc.kotlin.core;

fun main() {

    t01 { number ->
        "number: $number"
    }

    // 等价于下面这行
    t01(::run01)

    t01(99){
        "number: $it"
    }

    t01(99, ::run01)

    val r01 = ::run01
    t01(r01)
    // 这样不能调用
//    val r02 = run01(2)
//    t01(r02)


}

fun t01(mm: (number: Int) -> String) {
    println(mm(88))
}

fun t01(number: Int, mm:(Int) -> String) {
    println(mm(number))
}

fun run01(number: Int): String = "number: $number"