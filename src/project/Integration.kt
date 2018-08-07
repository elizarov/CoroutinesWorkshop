package project

import kotlinx.coroutines.*
import retrofit2.*
import kotlin.coroutines.*

suspend fun <T> Call<T>.await(): T = suspendCancellableCoroutine { cont ->
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                cont.resume(response.body()!!)
            } else {
                cont.resumeWithException(ErrorResponse(response))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            cont.resumeWithException(t)
        }
    })
    cont.invokeOnCancellation {
        cancel()
    }
}

class ErrorResponse(response: Response<*>) : Exception(
    "Failed with ${response.code()}: ${response.message()}\n${response.errorBody()?.string()}"
)
