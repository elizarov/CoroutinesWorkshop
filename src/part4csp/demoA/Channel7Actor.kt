package part4csp.demoA

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking<Unit> {
    val summer = actor<Int> {
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