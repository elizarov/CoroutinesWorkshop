package part4csp.demoC

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking<Unit> {
    val channel = Channel<Int>()
    launch(kotlin.coroutines.coroutineContext) {
        for (x in 1..5) {
            println("Sending $x")
            channel.send(x)
        }
    }
    repeat(5) {
        println("${channel.receive()} receive")
    }
    println("Done!")
}