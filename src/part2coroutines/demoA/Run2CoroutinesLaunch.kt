package part2coroutines.demoA

import kotlinx.coroutines.*

fun main() {
    val jobs = List(100_000) {
        GlobalScope.launch {
            Thread.sleep(5000)
            print(".")
        }
    }
    //jobs.forEach { it.join() }
}