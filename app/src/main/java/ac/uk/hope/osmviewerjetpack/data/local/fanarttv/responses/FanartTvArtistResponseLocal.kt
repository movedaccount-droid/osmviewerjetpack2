package ac.uk.hope.osmviewerjetpack.data.local.fanarttv.responses

import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.AlbumImagesLocal
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.ArtistImagesLocal

// having a "local" network class doesn't feel like a good way to go,
// but neither does including this code in the repository. so i have no idea where this should
// really be.

data class FanartTvArtistResponseLocal(
    val artist: ArtistImagesLocal,
    val albums: List<AlbumImagesLocal>?
)