package project

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.*

suspend fun loadContributorsGather(req: RequestData, callback: suspend (List<User>) -> Unit) = coroutineScope {
    val service = createGitHubService(req.username, req.password)
    log.info("Loading ${req.org} repos")
    val repos = service.listOrgRepos(req.org).await()
    log.info("${req.org}: loaded ${repos.size} repos")
    val channel = Channel<List<User>>()
    for (repo in repos) {
        launch {
            val users = service.listRepoContributors(req.org, repo.name).await()
            log.info("${repo.name}: loaded ${users.size} contributors")
            channel.send(users)
        }
    }
    var contribs = emptyList<User>()
    repeat(repos.size) {
        val users = channel.receive()
        contribs = (contribs + users).aggregateSlow()
        callback(contribs)
    }
    log.info("Total: ${contribs.size} contributors")
}
