package com.example.llc.kotlin.core;


fun main() {

    sum(1, 2, 3){i1, i2, i3 ->
        val result = i1 + i2 + i3
        println("result: $result")
        true
    }

}

fun <R> sum(n1: Int, n2: Int, n3: Int, mm: (Int, Int, Int) -> R): R {
    return mm(n1, n2, n3)
}