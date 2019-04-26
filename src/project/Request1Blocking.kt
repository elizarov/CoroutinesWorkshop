package project

import retrofit2.*

fun loadContributorsBlocking(req: RequestData): List<User> {
    val service = createGitHubService(req.username, req.password)
    log.info("Loading ${req.org} repos")
    val repos = service.listOrgRepos(req.org).responseBodyBlocking()
    log.info("${req.org}: loaded ${repos.size} repos")
    val contribs = repos.flatMap { repo ->
        val users = service.listRepoContributors(req.org, repo.name).responseBodyBlocking()
        log.info("${repo.name}: loaded ${users.size} contributors")
        users
    }.aggregate()
    log.info("Total: ${contribs.size} contributors")
    return contribs
}

@Suppress("UNCHECKED_CAST")
fun <T> Call<T>.responseBodyBlocking(
    noContent: (Response<T>) -> T
): T {
    val response = execute() // Executes requests and blocks current thread
    return when (response.code()) {
        200 -> response.body() as T // OK
        204 -> noContent(response) // NO CONTENT
        else -> errorResponse(response)
    }
}

fun <T> Call<List<T>>.responseBodyBlocking(): List<T> =
    responseBodyBlocking(noContent = { emptyList() })
