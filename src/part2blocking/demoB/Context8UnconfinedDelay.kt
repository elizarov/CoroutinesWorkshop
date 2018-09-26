package part2blocking.demoB

import kotlinx.coroutines.*

suspend fun displayDelay(name: String) {
    display(name)
    delay(1000)
    display(name)
}

fun main() = runBlocking<Unit> {
    launch                         { displayDelay("inherited") }
    launch(Dispatchers.Unconfined) { displayDelay("Unconfined") }
}

