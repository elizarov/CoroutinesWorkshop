package part2coroutines.demoC

import kotlinx.coroutines.*

private suspend fun doSomething() = withContext(Dispatchers.Default) {
    doSomethingSlow()
}

fun main() = runBlocking<Unit> {
    withTimeoutOrNull(1300) {
        for (i in 0 until 1000) {
            println("I'm working $i ...")
            doSomething()
        }
    }
}