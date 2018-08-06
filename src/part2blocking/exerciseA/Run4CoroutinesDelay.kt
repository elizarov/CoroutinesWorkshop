package part2blocking.exerciseA

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val jobs = List(100_000) {
        launch {
            delay(5000)
            print(".")
        }
    }
    jobs.forEach { it.join() }
}