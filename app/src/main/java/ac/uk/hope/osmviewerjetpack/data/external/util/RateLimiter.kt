package ac.uk.hope.osmviewerjetpack.data.external.util

import ac.uk.hope.osmviewerjetpack.di.DefaultDispatcher
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

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