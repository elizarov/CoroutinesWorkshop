package part4csp.demoA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

val curThread: String get() = Thread.currentThread().name

private fun CoroutineScope.producer(channel: Channel<Int>) =
    launch {
        for (x in 1..5) {
            println("Sending $x from $curThread")
            channel.send(x)
        }
    }

fun main() = runBlocking<Unit> {
    val channel = Channel<Int>()
    producer(channel)
    repeat(5) {
        println("${channel.receive()} receive from $curThread")
    }
    println("Done!")
}

