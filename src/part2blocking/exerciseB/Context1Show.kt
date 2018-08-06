package part2blocking.exerciseB

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val ctx: CoroutineContext = coroutineContext
    println(ctx)
}