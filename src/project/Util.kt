package project

import kotlinx.coroutines.*
import org.slf4j.*

val log: Logger = LoggerFactory.getLogger("Contributors")

fun List<User>.aggregate(): List<User> =
    groupingBy { it.login }
        .reduce { login, a, b -> User(login, a.contributions + b.contributions) }
        .values
        .sortedByDescending { it.contributions }


val computation =
    newFixedThreadPoolContext(2, "Computation")

suspend fun List<User>.aggregateSlow(): List<User> = withContext(computation) {
    aggregate()
        .also {
            // Imitate CPU consumption / blocking
            Thread.sleep(500)
        }
}
