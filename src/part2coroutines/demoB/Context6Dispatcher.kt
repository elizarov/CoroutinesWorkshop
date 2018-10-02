package part2coroutines.demoB

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.*

fun display(name: String) =
    println("launch($name) in ${Thread.currentThread().name}")

fun main() = runBlocking<Unit> {
    launch(Dispatchers.Default) { display("DefaultDispatcher") }
    launch(Dispatchers.Swing)   { display("Swing") }
    launch                      { display("inherited") }
}

