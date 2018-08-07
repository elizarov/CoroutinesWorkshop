package part4csp.exerciseA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val channel = Channel<Int>()
    launch(coroutineContext) {
        for (x in 1..5) {
            println("Sending $x from $curThread")
            channel.send(x)
        }
    }
    repeat(5) {
        println("${channel.receive()} receive from $curThread")
    }
    println("Done!")
}