package part2blocking.demoB

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main() = runBlocking<Unit> {
    val ctx: CoroutineContext = coroutineContext
    println(ctx)
}