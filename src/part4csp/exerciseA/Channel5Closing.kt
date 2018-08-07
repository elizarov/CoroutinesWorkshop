package part4csp.exerciseA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) {
            channel.send(x * x)
        }
        channel.close()
    }
    for (element in channel) {
        println(element)
    }
    println("Done!")
}