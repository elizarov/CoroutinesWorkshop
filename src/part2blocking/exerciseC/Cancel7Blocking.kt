package part2blocking.exerciseC

import kotlinx.coroutines.*

fun doSomethingSlow() = Thread.sleep(500)

fun main(args: Array<String>) = runBlocking<Unit> {
    withTimeoutOrNull(1300) {
        repeat(1000) { i ->
            println("I'm working $i ...")
            doSomethingSlow()
        }
    }
}