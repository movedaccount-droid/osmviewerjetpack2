package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// there is no simple way to have a field in an object, but then not update it when we
// retrieve it from the network, only provide it on initialization. this would require an Update
// object and to check manually if the entity is in the table, requiring an extra query per artist,
// and bypassing the @Upsert function

// so instead we have to have an association which is literally just "yes, we are followed"
// this is about the point where my commits devolve into new and fascinating swears

@Entity(
    tableName = "followed"
)
data class FollowedLocal(
    @PrimaryKey val artistMbid: String
)