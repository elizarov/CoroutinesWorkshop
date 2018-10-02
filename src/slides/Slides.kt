@file:Suppress("FunctionName", "unused")

package slides

import kotlinx.coroutines.*
import kotlin.concurrent.*
import kotlin.coroutines.*

class Token
class Item
class Post

// Functions are named as in j.u.c.CompletableFuture
interface Promise<T> {
    fun thenAccept(action: (T) -> Unit): Promise<Unit>
    fun <U> thenCompose(transform: (T) -> Promise<U>): Promise<U>
}

fun requestTokenAsync(cb: (Token) -> Unit) { }

fun requestTokenAsync2(): Promise<Token> = TODO()

fun doSomething(vararg any: Any) {
    TODO("not implemented")
}

fun `Future Encapsulates Callback Slide`() {
    requestTokenAsync { token ->
        doSomething()
    }

    requestTokenAsync2().thenAccept { token ->
        doSomething()
    }
}

class Call<T>

interface Service {
    fun createPost(token: Token, item: Item): Call<Post>
}

object ServiceImpl : Service {
    override fun createPost(token: Token, item: Item): Call<Post> = TODO()
}

suspend fun <T> Call<T>.await(): T = TODO()

suspend fun `Call with Current Continuation Slide`() {
    val serviceInstance : Service = ServiceImpl
    val token: Token = Token()
    val item: Item = Item()

    val call = serviceInstance.createPost(token, item)
    val post = suspendCoroutine<Post> { cont ->
        // ...
    }
    doSomething(post)
}

object Object {
    fun wait(): Nothing = TODO()
}

fun `Real Life Slide`() {

    // start new thread for a background job
    thread {
        while (!Thread.currentThread().isInterrupted) {
            // do something
        }
    }

    try {
        Object.wait()
    } catch (e: InterruptedException) {
        // ignore
    }
}

suspend fun `Async vs Launch Slide`() = coroutineScope {
    class T
    val job: Job              = launch { TODO() }
    val deferred: Deferred<T> = async  { TODO() }
}

// Launch coroutine builder slide

suspend fun requestToken(): Token = TODO()
suspend fun createPost(token: Token, item: Item): Post = TODO()
fun processPost(post: Post): Unit = TODO()

fun postItem(item: Item) {
    GlobalScope.launch {
        val token = requestToken()
        val post = createPost(token, item)
        processPost(post)
    }
}

class Image