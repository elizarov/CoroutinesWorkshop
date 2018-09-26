package part3concurrency.demoA

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val job = launch {
        delay(1000)
        println("launch: done")
        error("Something went wrong inside launch")
    }
    val deferred = async {
        delay(1000)
        println("async: done")
        error("Something went wrong inside async")
    }
}