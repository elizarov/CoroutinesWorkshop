package part4csp.demoA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking<Unit> {
    val channel = produce {
        for (x in 1..5) {
            send(x * x)
        }
    }
    for (element in channel) {
        println(element)
    }
    println("Done!")
}