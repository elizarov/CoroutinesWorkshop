package part4csp.exerciseA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val channel = produce {
        for (x in 1..5) {
            send(x * x)
        }
    }
    for (element in channel) {
        println(element)
    }
    println("Done!")
}