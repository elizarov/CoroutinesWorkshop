package project

import retrofit2.*

fun loadContributorsCallbacks(req: RequestData, callback: (List<User>) -> Unit)  {
    TODO()
}

@Suppress("UNCHECKED_CAST")
fun <T> Call<T>.responseCallback(
    callback: (T) -> Unit,
    noContent: (Response<T>) -> T = { errorResponse(it) }
) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            when (response.code()) {
                200 -> callback(response.body() as T) // OK
                204 -> callback(noContent(response)) // NO CONTENT
                else -> errorResponse(response)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            log.error("Call failed", t)
        }
    })
}

fun <T> Call<List<T>>.responseCallback(
    callback: (List<T>) -> Unit
) =
    responseCallback(callback, noContent = { emptyList() })
