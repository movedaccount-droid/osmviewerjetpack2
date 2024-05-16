package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import androidx.room.ColumnInfo

data class PackagingLocal(
    @ColumnInfo(name = "packagingMbid")
    val mbid: String,
    @ColumnInfo(name = "packagingName")
    val name: String
)

fun PackagingLocal.toExternal() = name

fun List<PackagingLocal>.toExternal() = map(PackagingLocal::toExternal)