package part4csp.demoA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

private fun CoroutineScope.producer(channel: Channel<Int>) =
    launch {
        for (x in 1..5) {
            channel.send(x * x)
        }
        channel.close()
    }

fun main() = runBlocking<Unit> {
    val channel = Channel<Int>()
    producer(channel)
    for (element in channel) {
        println(element)
    }
    println("Done!")
}

