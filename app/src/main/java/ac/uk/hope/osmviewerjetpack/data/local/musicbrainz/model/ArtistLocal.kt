package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// artist data/domain layer representations

// we store our api data as parcelable, so we can push them with intents.
// this way we can push data to layouts instead of having them request it themselves every time
// TODO: we should actually make sure we need parcelable functionality

// this also fits the definitions of
// https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet#7

@Parcelize
data class ArtistLocal(
    val id: String,
    val type: String?,
    val typeId: String?,
    val score: Int,
    val name: String,
    val sortName: String,
    val country: String?,
    val area: ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.AreaLocal?,
    val beginArea: ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.AreaLocal?,
    val disambiguation: String?,
    val lifeSpan: ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.LifeSpanLocal,
    val tags: List<String>?
) : Parcelable