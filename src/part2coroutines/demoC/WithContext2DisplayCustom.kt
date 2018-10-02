package part2coroutines.demoC

import kotlinx.coroutines.*
import part2coroutines.demoB.*

val computation =
    newFixedThreadPoolContext(2, "Computation")

fun main() = runBlocking<Unit> {
    val job = launch(computation) {
        display("computation")
    }
}