package project

import kotlinx.coroutines.future.*

fun loadContributorsConcurrentAsync(req: RequestData) = future {
    loadContributorsConcurrent(req)
}
