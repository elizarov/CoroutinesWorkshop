package part1async.project

import retrofit2.*

fun loadContributorsBlocking(req: RequestData) : List<User> {
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

fun List<User>.aggregate(): List<User> =
    groupingBy { it.login }
        .reduce { login, a, b -> User(login, a.contributions + b.contributions) }
        .values
        .sortedByDescending { it.contributions }

fun <T> Call<T>.responseBodyBlocking(): T {
    val response = execute() // Executes requests and blocks current thread
    checkResponse(response)
    return response.body()!!
}

fun checkResponse(response: Response<*>) {
    check(response.isSuccessful) {
        "Failed with ${response.code()}: ${response.message()}\n${response.errorBody()?.string()}"
    }
}
