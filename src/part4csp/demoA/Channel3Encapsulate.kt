package part4csp.demoA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

private fun CoroutineScope.producer(channel: Channel<Int>) =
    launch {
        for (x in 1..5) {
            delay(500)
            channel.send(x * x)
        }
    }

fun main() = runBlocking<Unit> {
    val channel = Channel<Int>()
    producer(channel)
    repeat(5) {
        println(channel.receive())
    }
    println("Done!")
}

