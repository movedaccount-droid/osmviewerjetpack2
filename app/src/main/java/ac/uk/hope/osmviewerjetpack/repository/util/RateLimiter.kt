package ac.uk.hope.osmviewerjetpack.repository.util

import ac.uk.hope.osmviewerjetpack.util.TAG
import android.util.Log
import kotlinx.coroutines.delay

// tracks await periods so we don't request apis beyond rate limit
// TODO: we could throw an exception here if we have too much queued
class RateLimiter(
    val timeoutMillis: Long
) {
    private var nextRequestTime = System.currentTimeMillis()

    suspend fun await() {
        val now = System.currentTimeMillis()
        if (nextRequestTime < now) nextRequestTime = now
        val waitDelay = nextRequestTime - now
        nextRequestTime += timeoutMillis
        delay(waitDelay)
    }
}