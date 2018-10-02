package part2coroutines.demoB

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    println(coroutineContext)
    val job = launch {
        println(coroutineContext)
    }
}