package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import androidx.room.ColumnInfo

data class FormatLocal(
    @ColumnInfo(name = "formatMbid")
    val mbid: String,
    @ColumnInfo(name = "formatName")
    val name: String
)

fun FormatLocal.toExternal() = name

fun List<FormatLocal>.toExternal() = map(FormatLocal::toExternal)