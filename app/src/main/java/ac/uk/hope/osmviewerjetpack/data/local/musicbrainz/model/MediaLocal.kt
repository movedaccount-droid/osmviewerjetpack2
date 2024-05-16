package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import androidx.room.Embedded

data class MediaLocal(
    @Embedded val format: FormatLocal,
    val tracks: List<TrackLocal>
)