package part2coroutines.demoC

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    withTimeoutOrNull(1300) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500)
        }
    }
}