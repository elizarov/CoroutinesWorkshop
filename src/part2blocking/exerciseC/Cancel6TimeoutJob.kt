package part2blocking.exerciseC

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    println(coroutineContext[Job])
    withTimeoutOrNull(1300) {
        println(coroutineContext[Job])
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500)
        }
    }
}