package project

import kotlin.concurrent.*

fun loadContributorsBackground(req: RequestData, callback: (List<User>) -> Unit) {
    thread {
        val users = loadContributorsBlocking(req)
        callback(users)
    }
}