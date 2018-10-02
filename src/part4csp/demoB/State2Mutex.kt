package part4csp.demoB

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*

class Counter {
    private var counter = 0
    private val mutex = Mutex()

    suspend fun inc() = mutex.withLock {
        counter++
    }

    suspend fun get() = mutex.withLock {
        counter
    }
}

fun main() = runBlocking<Unit> {
    val counter = Counter()
    repeat(100) {
        counter.inc()
    }
    println("Counter = ${counter.get()}")
}