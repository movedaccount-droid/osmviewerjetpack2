package ac.uk.hope.osmviewerjetpack.data.external.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext

// tracks await periods so we don't request apis beyond rate limit
class RateLimiter(
    private val timeoutMillis: Long,
) {

    private var nextRequestTime = System.currentTimeMillis()
    private val mutex = Mutex(locked = false)


    // await the last lock and take it
    suspend fun startOperation() {
        mutex.lock()
        nextRequestTime = System.currentTimeMillis() + timeoutMillis
    }

    // end the operation and apply rate limiting
    suspend fun endOperationAndLimit() {
        // TODO: this should be dependency injected
        withContext(Dispatchers.Default) {
            delay(nextRequestTime - System.currentTimeMillis())
            mutex.unlock()
        }
    }
}