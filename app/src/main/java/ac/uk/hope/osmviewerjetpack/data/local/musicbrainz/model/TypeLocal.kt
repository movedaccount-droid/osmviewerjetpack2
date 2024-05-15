package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import androidx.room.ColumnInfo

data class TypeLocal (
    @ColumnInfo(name = "typeMbid")
    val mbid: String,
    @ColumnInfo(name = "typeName")
    val name: String
)

fun TypeLocal.toExternal() = name

fun List<TypeLocal>.toExternal() = map(TypeLocal::toExternal)