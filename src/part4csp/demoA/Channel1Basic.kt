package part4csp.demoA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking<Unit> {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) {
            channel.send(x * x)
        }
    }
    repeat(5) {
        println(channel.receive())
    }
    println("Done!")
}