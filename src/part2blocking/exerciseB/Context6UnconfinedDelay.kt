package part2blocking.exerciseB

import kotlinx.coroutines.*
import kotlin.coroutines.*

suspend fun displayDelay(name: String) {
    display(name)
    delay(1000)
    display(name)
}

fun main(args: Array<String>) = runBlocking {
    val jobs = mutableListOf<Job>()
    jobs += launch(coroutineContext) { displayDelay("coroutineContext") }
    jobs += launch(Unconfined)       { displayDelay("Unconfined") }
    jobs.forEach { it.join() }
}

