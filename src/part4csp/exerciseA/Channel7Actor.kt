package part4csp.exerciseA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val summer = actor<Int>(coroutineContext) {
        var sum = 0 // State!
        for (i in channel) {
            sum += i
            println("Sum = $sum")
        }
    }
    for (x in 1..5) {
        summer.send(x)
    }
    summer.close()
}