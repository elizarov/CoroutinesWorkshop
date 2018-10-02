package part2coroutines.demoB

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main() = runBlocking<Unit> {
    println(coroutineContext[Job])
    println(coroutineContext[ContinuationInterceptor])
}