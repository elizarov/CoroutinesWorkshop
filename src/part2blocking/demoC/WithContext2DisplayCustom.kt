package part2blocking.demoC

import kotlinx.coroutines.*
import part2blocking.demoB.*

val computation =
    newFixedThreadPoolContext(2, "Computation")

fun main() = runBlocking<Unit> {
    val job = launch(computation) {
        display("computation")
    }
}