package part3concurrency.demoA

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val request = launch {
        val job1 = GlobalScope.launch {
            delay(1000)
            println("job1: done")
        }
        val job2 = launch {
            delay(1000)
            println("job2: done")
        }
        job1.join()
        job2.join()
    }
    delay(500)
    request.cancelAndJoin()
    delay(2000)
}