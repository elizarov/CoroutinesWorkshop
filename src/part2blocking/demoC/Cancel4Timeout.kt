package part2blocking.demoC

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    withTimeout(1300) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500)
        }
    }
}