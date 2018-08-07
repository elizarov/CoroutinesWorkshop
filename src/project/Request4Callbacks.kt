package project

import retrofit2.*

fun loadContributorsCallbacks(req: RequestData, callback: (List<User>) -> Unit)  {
    val service = createGitHubService(req.username, req.password)
    log.info("Loading ${req.org} repos")
    service.listOrgRepos(req.org).responseCallback { repos ->
        log.info("${req.org}: loaded ${repos.size} repos")
        val all = ArrayList<User>()
        var repoIndex = 0
        // A function to process next repository or be done with it
        fun processNextRepo() {
            if (repoIndex >= repos.size) {
                val contribs = all.aggregate()
                log.info("Total: ${contribs.size} contributors")
                callback(contribs)
                return
            }
            val repo = repos[repoIndex++]
            service.listRepoContributors(req.org, repo.name).responseCallback { users ->
                log.info("${repo.name}: loaded ${users.size} contributors")
                all += users
                processNextRepo()
            }
        }
        // Start processing
        processNextRepo()
    }
}

inline fun <T> Call<T>.responseCallback(crossinline callback: (T) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            checkResponse(response)
            callback(response.body()!!)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            log.error("Call failed", t)
        }
    })
}
