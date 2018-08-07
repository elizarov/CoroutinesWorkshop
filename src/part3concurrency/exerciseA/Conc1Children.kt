package part3concurrency.exerciseA

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val request = launch {
        val job1 = launch {
            delay(1000)
            println("job1: done")
        }
        val job2 = launch(coroutineContext) {
            delay(1000)
            println("job2: done")
        }
        job1.join()
        job2.join()
    }
    delay(500)
    request.cancelAndJoin()
}