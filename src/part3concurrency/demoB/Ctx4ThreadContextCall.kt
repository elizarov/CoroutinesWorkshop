package part3concurrency.demoB

import kotlinx.coroutines.*

private val userId = ThreadLocal<String>()

fun main() = runBlocking<Unit> {
    userId.set("foo")
    withContext(userId.asContextElement()) {
        doSomething()
    }
}

suspend fun doSomething() = withContext(Dispatchers.Default) {
    println("userId = ${userId.get()}")
}