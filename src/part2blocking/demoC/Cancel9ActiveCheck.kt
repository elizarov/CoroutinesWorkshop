package part2blocking.demoC

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    withTimeoutOrNull(1300) {
        for (i in 0 until 1000) {
            if (!isActive) break
            println("I'm working $i ...")
            doSomethingSlow()
        }
    }
}