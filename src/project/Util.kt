package project

import kotlinx.coroutines.*
import org.slf4j.*

val log: Logger = LoggerFactory.getLogger("Contributors")

// todo: write actual aggregation code here
fun List<User>.aggregate(): List<User> =
    this

