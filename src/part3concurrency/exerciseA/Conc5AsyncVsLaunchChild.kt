package part3concurrency.exerciseA

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val job = launch(coroutineContext) {
        delay(1000)
        println("launch: done")
        error("Something went wrong inside launch")
    }
    val deferred = async(coroutineContext) {
        delay(1000)
        println("async: done")
        error("Something went wrong inside async")
    }
}