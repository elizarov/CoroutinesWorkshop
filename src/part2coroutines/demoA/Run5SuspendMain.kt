package part2coroutines.demoA

import kotlinx.coroutines.*

suspend fun main() {
    val jobs = List(100_000) {
        GlobalScope.launch {
            delay(5000)
            print(".")
        }
    }
    jobs.forEach { it.join() }
}