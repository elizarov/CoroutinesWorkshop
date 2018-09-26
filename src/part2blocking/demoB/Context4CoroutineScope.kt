package part2blocking.demoB

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main() = runBlocking<Unit> {
    val job = launch {
        val ctx: CoroutineContext = coroutineContext
        println(ctx)
    }
    // job.join()
}