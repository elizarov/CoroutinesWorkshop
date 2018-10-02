package part3concurrency.demoB

import kotlinx.coroutines.*
import kotlinx.coroutines.slf4j.*
import kotlinx.coroutines.swing.*
import org.slf4j.*


val userId = ThreadLocal<String>()

suspend fun main() {
    MDC.put("foo", "bar")
    withContext(MDCContext()) {
        withContext(Dispatchers.Default) {
            userId.set("NEO")
            withContext(userId.asContextElement()) {
                println("Doing something on thread ${Thread.currentThread().name}")
                doSomething()
            }
        }
    }
}

suspend fun doSomething() = withContext(Dispatchers.Swing) {
    delay(1000)
    bar()
    println("context = $coroutineContext")
}

private fun bar() {
    println("User is ${userId.get()} on thread ${Thread.currentThread().name}")
}