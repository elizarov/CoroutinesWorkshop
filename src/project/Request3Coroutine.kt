package project

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
