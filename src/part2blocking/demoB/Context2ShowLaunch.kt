package part2blocking.demoB

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main() = runBlocking<Unit> {
    val job = GlobalScope.launch {
        val ctx: CoroutineContext = coroutineContext
        println(ctx)
    }
    job.join()
}