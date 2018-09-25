package project

import kotlinx.coroutines.*

suspend fun loadContributorsConcurrent(req: RequestData) : List<User> = coroutineScope {
    val service = createGitHubService(req.username, req.password)
    log.info("Loading ${req.org} repos")
    val repos = service.listOrgRepos(req.org).await()
    log.info("${req.org}: loaded ${repos.size} repos")
    val deferreds: List<Deferred<List<User>>> = repos.map { repo ->
        async {
            val users = service.listRepoContributors(req.org, repo.name).await()
            log.info("${repo.name}: loaded ${users.size} contributors")
            users
        }
    }
    val contribs = deferreds.awaitAll().flatten().aggregate()
    log.info("Total: ${contribs.size} contributors")
    contribs
}

