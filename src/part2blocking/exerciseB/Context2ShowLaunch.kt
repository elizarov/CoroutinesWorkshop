package part2blocking.exerciseB

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val job = launch {
        val ctx: CoroutineContext = coroutineContext
        println(ctx)
    }
    job.join()
}