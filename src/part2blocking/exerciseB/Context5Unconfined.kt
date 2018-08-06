package part2blocking.exerciseB

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main(args: Array<String>) = runBlocking {
    val jobs = mutableListOf<Job>()
    jobs += launch(coroutineContext) { display("coroutineContext") }
    jobs += launch(Unconfined)       { display("Unconfined") }
    jobs.forEach { it.join() }
}

