package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model

import java.util.Calendar

// right now we never expose a normal notification. but still

data class Notification (
    val sent: Calendar
)

data class DetailedNotification (
    val sent: Calendar,
    val releaseGroup: ReleaseGroup
)