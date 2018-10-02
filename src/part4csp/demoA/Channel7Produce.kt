package part4csp.demoA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

private fun CoroutineScope.producer(): ReceiveChannel<Int> =
    produce {
        for (x in 1..5) {
            send(x * x)
        }
    }

fun main() = runBlocking<Unit> {
    val channel = producer()
    for (element in channel) {
        println(element)
    }
    println("Done!")
}

