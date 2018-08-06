package part2blocking.exerciseB

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.*
import kotlin.coroutines.*

fun display(name: String) =
    println("launch($name) in ${Thread.currentThread().name}")

fun main(args: Array<String>) = runBlocking {
    val jobs = mutableListOf<Job>()
    jobs += launch                   { display("DefaultDispatcher") }
    jobs += launch(Swing)            { display("Swing") }
    jobs += launch(coroutineContext) { display("coroutineContext") }
    jobs.forEach { it.join() }
}

