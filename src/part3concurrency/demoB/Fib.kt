package part3concurrency.demoB

import kotlinx.coroutines.*


val fib = sequence<Int> {
    var a = 1
    var b = 1
    while (true) {
        yield(a)
        val c = a
        a = b
        b += c
    }
}


fun main() {
//    println(fib.take(10).toList())

    val iter = fib.iterator()
    println(iter.next())
    println(iter.next())
}