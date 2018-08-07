package part1async.project

import kotlinx.coroutines.*
import kotlinx.coroutines.future.*
import kotlin.coroutines.*

fun loadContributorsConcurrentAsync(req: RequestData) = future {
    loadContributorsConcurrent(req)
}
