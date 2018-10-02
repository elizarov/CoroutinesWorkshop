package part4csp.demoC

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

private fun CoroutineScope.producer(channel: Channel<Int>) =
    launch {
        for (x in 1..5) {
            println("Sending $x")
            channel.send(x)
        }
    }

fun main() = runBlocking<Unit> {
    val channel = Channel<Int>()
    producer(channel)
    repeat(5) {
        println("${channel.receive()} receive")
    }
    println("Done!")
}

