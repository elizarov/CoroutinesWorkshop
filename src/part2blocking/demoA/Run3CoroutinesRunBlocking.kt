package part2blocking.demoA

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val jobs = List(100_000) {
        GlobalScope.launch {
            Thread.sleep(5000)
            print(".")
        }
    }
    jobs.forEach { it.join() }
}