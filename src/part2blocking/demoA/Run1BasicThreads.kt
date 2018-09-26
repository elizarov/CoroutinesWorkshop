package part2blocking.demoA

import kotlin.concurrent.*

fun main() {
    val jobs = List(100_000) {
        thread {
            Thread.sleep(5000)
            print(".")
        }
    }
    jobs.forEach { it.join() }
}