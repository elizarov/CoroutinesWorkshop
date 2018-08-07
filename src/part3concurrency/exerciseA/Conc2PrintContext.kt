package part3concurrency.exerciseA

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val request = launch {
        println(coroutineContext)
    }
    request.cancelAndJoin()
}