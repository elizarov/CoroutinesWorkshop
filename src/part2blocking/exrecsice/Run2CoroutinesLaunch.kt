package part2blocking.exrecsice

import kotlinx.coroutines.*

fun main(args: Array<String>) {
    val jobs = List(100_000) {
        launch {
            Thread.sleep(5000)
            print(".")
        }
    }
    //jobs.forEach { it.join() }
}