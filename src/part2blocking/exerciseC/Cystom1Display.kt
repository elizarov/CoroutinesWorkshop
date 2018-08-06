package part2blocking.exerciseC

import kotlinx.coroutines.*
import part2blocking.exerciseB.*

val computation =
    newFixedThreadPoolContext(2, "Computation")

fun main(args: Array<String>) = runBlocking<Unit> {
    val job = launch(computation) {
        display("computation")
    }
    job.join()
}