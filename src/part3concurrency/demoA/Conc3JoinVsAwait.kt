package part3concurrency.demoA

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val job = GlobalScope.launch {
        delay(1000)
        println("launch: done")
        error("Something went wrong inside launch")
    }
    val deferred = GlobalScope.async {
        delay(1000)
        println("async: done")
        error("Something went wrong inside async")
    }
    job.join()
    deferred.await()
}