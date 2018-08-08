package part4csp.exerciseC

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(args: Array<String>) = runBlocking<Unit> {
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