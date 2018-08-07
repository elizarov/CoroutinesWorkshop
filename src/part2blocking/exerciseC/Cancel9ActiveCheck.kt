package part2blocking.exerciseC

import kotlinx.coroutines.*

// DOES NOT WORK. TODO: https://github.com/Kotlin/kotlinx.coroutines/issues/479
fun main(args: Array<String>) = runBlocking<Unit> {
    withTimeoutOrNull(1300) {
        for (i in 0 until 1000) {
            if (!isActive) break
            println("I'm working $i ...")
            doSomethingSlow()
        }
    }
}