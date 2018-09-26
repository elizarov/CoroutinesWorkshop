package part2blocking.demoC

import kotlinx.coroutines.*

fun doSomethingSlow() = Thread.sleep(500)

fun main() = runBlocking<Unit> {
    withTimeoutOrNull(1300) {
        repeat(1000) { i ->
            println("I'm working $i ...")
            doSomethingSlow()
        }
    }
}