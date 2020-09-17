package com.example.llc.kotlin.core;

fun main() {

    show1("Mars"){
        println("name: $it")
    }

    show1("Jordon", mm = {
        println("name: $it")
    })

    show1(name = "Kobe", mm = {
        println("name: $it")
    })

    show1("James", {
        println("name: $it")
    })

    show2("Bosh"){
        println("name: $it")
    }

    show2 {
        println("name: $it")
    }

    sun1 {
        // 打印 9
        println("it: $it")
    }

    sun2 { i, b ->
        println("i = $i, b = $b")
    }

    sun2(mm = {n1, b2 ->
        println("i = $n1, b = $b2")
    })

    sun2({n1, b2 ->
        println("i = $n1, b = $b2")
    })
}

fun show1(name: String, mm:(String) -> Unit) {
    mm(name)
}

fun show2(name: String = "Wade", mm:(String) -> Unit) {
    mm(name)
}

fun sun1(mm: (Int) -> Unit) {
    mm(9)
}

fun sun2(mm: (Int, Boolean) -> Unit){
    mm(1, true)
}


