package part2blocking.exerciseC

import kotlinx.coroutines.*

suspend fun doSomething() = withContext(computation) {
    doSomethingSlow()
}

fun main(args: Array<String>) = runBlocking<Unit> {
    withTimeoutOrNull(1300) {
        for (i in 0 until 1000) {
            println("I'm working $i ...")
            doSomething()
        }
    }
}