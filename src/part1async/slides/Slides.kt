@file:Suppress("FunctionName", "unused")

package part1async.slides

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
