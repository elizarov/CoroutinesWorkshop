package part1async.project

import retrofit2.*
import kotlin.coroutines.*

suspend fun loadContributors(req: RequestData) : List<User> {
    val service = createGitHubService(req.username, req.password)
    log.info("Loading ${req.org} repos")
    val repos = service.listOrgRepos(req.org).await()
    log.info("${req.org}: loaded ${repos.size} repos")
    val contribs = repos.flatMap { repo ->
        val users = service.listRepoContributors(req.org, repo.name).await()
        log.info("${repo.name}: loaded ${users.size} contributors")
        users
    }.aggregate()
    log.info("Total: ${contribs.size} contributors")
    return contribs
}

suspend fun <T> Call<T>.await(): T = suspendCoroutine { cont ->
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
}

class ErrorResponse(response: Response<*>) : Exception(
    "Failed with ${response.code()}: ${response.message()}\n${response.errorBody()?.string()}"
)
