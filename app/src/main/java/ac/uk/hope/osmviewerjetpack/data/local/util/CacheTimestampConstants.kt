package ac.uk.hope.osmviewerjetpack.data.local.util

// we cannot hold complex objects like Instants in room,
// nor can we make any wrapper class around a Long and access a .expired getter in sql.
// so we're stuck with this... defining some constants and duplicating the (simple) logic with each
// query. blugh

const val MILLIS_PER_DAY = 86400000
const val CACHE_TIMEOUT = MILLIS_PER_DAY * 7

val currentCacheTimeout
    get() = System.currentTimeMillis() - CACHE_TIMEOUT