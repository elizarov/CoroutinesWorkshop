package project

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.*

fun aggregatorActor(
    coroutineContext: CoroutineContext,
    uiUpdateActor: SendChannel<List<User>>
) =
    actor<List<User>>(coroutineContext) {
        var contribs = emptyList<User>()
        for (users in channel) {
            contribs = (contribs + users).aggregateSlow()
            uiUpdateActor.send(contribs)
        }
    }

class WorkerRequest(
    val service: GitHubService,
    val org: String,
    val repo: String
)

fun workerJob(
    coroutineContext: CoroutineContext,
    requests: ReceiveChannel<WorkerRequest>,
    aggregator: SendChannel<List<User>>
) =
    launch(coroutineContext) {
        for (req in requests) {
            val users = req.service.listRepoContributors(req.org, req.repo).await()
            log.info("${req.repo}: loaded ${users.size} contributors")
            aggregator.send(users)
        }
    }

suspend fun loadContributorsActor(req: RequestData, uiUpdateActor: SendChannel<List<User>>) {
    val service = createGitHubService(req.username, req.password)
    log.info("Loading ${req.org} repos")
    val repos = service.listOrgRepos(req.org).await()
    log.info("${req.org}: loaded ${repos.size} repos")
    val aggregator = aggregatorActor(coroutineContext, uiUpdateActor)
    val requests = Channel<WorkerRequest>()
    val workers = List(4) {
        workerJob(coroutineContext, requests, aggregator)
    }
    for (repo in repos) {
        requests.send(WorkerRequest(service, req.org, repo.name))
    }
    requests.close()
    workers.joinAll()
    aggregator.close()
}
