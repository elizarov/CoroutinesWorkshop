package part3concurrency.demoB

import kotlinx.coroutines.*

private val userId = ThreadLocal<String>()

fun displayUserId() =
    println("[${Thread.currentThread().name}] userId = ${userId.get()}")

fun main() = runBlocking<Unit> {
    userId.set("foo")
    displayUserId()
    launch(Dispatchers.Default) {
        displayUserId()
    }
}