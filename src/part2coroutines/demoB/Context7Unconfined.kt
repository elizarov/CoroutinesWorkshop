package part2coroutines.demoB

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    launch                         { display("inherited") }
    launch(Dispatchers.Unconfined) { display("Unconfined") }
}

