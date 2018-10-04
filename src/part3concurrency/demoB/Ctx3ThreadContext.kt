package part3concurrency.demoB

import kotlinx.coroutines.*

private val userId = ThreadLocal<String>()

fun main() = runBlocking<Unit> {
    userId.set("foo")
    withContext(userId.asContextElement()) {
        withContext(Dispatchers.Default) {
            println("userId = ${userId.get()}")
        }
    }
}
