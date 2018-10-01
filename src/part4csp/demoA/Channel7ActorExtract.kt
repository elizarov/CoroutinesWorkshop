package part4csp.demoA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking<Unit> {
    val summer = summingActor()
    for (x in 1..5) {
        summer.send(x)
    }
    summer.close()
}

private fun CoroutineScope.summingActor(): SendChannel<Int> = actor<Int> {
    var sum = 0 // State!
    for (i in channel) {
        sum += i
        println("Sum = $sum")
    }
}