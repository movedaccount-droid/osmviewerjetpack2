package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import androidx.room.ColumnInfo

data class StatusLocal(
    @ColumnInfo(name = "statusMbid")
    val mbid: String,
    @ColumnInfo(name = "statusName")
    val name: String
)

fun StatusLocal.toExternal() = name

fun List<StatusLocal>.toExternal() = map(StatusLocal::toExternal)