package part4csp.demoA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

private fun CoroutineScope.summer(): SendChannel<Int> = actor {
    var sum = 0 // State!
    for (i in channel) {
        sum += i
        println("Sum = $sum")
    }
}

fun main() = runBlocking<Unit> {
    val summer = summer()
    for (x in 1..5) {
        summer.send(x)
    }
    summer.close()
}