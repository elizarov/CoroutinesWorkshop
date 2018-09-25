package project

import kotlinx.coroutines.*
import kotlinx.coroutines.future.*

fun loadContributorsConcurrentAsync(req: RequestData) = GlobalScope.future {
    loadContributorsConcurrent(req)
}
