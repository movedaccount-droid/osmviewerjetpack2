package ac.uk.hope.osmviewerjetpack.data.local.fanarttv.responses

import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.AlbumImagesLocal
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.ArtistImagesLocal
import com.google.gson.annotations.SerializedName

// having a "local" network class doesn't feel like a good way to go,
// but neither does including this code in the repository. so i have no idea where this should
// really be.

data class FanartTvArtistResponseLocal(
    val artist: ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.ArtistImagesLocal,
    val albums: List<ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.AlbumImagesLocal>?
)

// TODO: we currently never need to convert back to network so we don't have these functions.
// but we might eventually need them