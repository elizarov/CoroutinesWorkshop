package part2blocking.demoC

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    println(coroutineContext[Job])
    withTimeoutOrNull(1300) {
        println(coroutineContext[Job])
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500)
        }
    }
}