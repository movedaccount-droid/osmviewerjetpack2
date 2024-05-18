package ac.uk.hope.osmviewerjetpack.worker

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first

// synchronize followed artist releases,
// notify user of anything new,
// delete old caches we don't want anymore

// syncing new releases is, generally speaking, a terrible fucking idea. musicbrainz was not
// designed to make this easy.

// every sync we cache the last number of releases we saw. if nothing has changed we perform
// no further operation. if it's changed, we iterate the entire set of releases since the user
// followed and find the new updates, pushing them to the database with the interested tag.
// this is incredibly inefficient and if we had access to edit history we wouldn't need to do it.
// oh well.

@HiltWorker
class SyncWorker
@AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository
):
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {

        // clear caches
        musicBrainzRepository.prune()
        fanartTvRepository.prune()

        // synchronize followed artist releases
        val newReleases = musicBrainzRepository.updateFollowedCaches()

        // get artist for each new release
        val artistMbids = newReleases.map { it.artistMbids }.flatten()
        val artists = musicBrainzRepository.getArtists(artistMbids)
            .dropWhile { results -> results.size < artistMbids.size }
            .first()

        // send notifications with nicely-formatted artist string
        val nameOf = { mbid: String -> artists.find { it.mbid == mbid }!!.name }
        for (release in newReleases) {
            val mbids = release.artistMbids
            val artistString = when (mbids.size) {
                // Radiohead
                1 -> nameOf(mbids.first())
                // Radiohead and John Moores
                2 -> "${nameOf(mbids.first())} and ${nameOf(mbids.last())}"
                // Radiohead, John Moores, Rei Kondoh and Five Star Hotel
                else -> "${mbids.subList(0, mbids.size - 2).joinToString(transform = nameOf)} and ${nameOf(mbids.last())}"
            }
            // TODO: we need to set up the notifications page, then associate it via deep link to
            // this notification
            // https://developer.android.com/develop/ui/compose/navigation#deeplinks
            var builder = NotificationCompat.Builder(appContext, "follow")
                .setSmallIcon(R.drawable.ic_tracks)
                .setContentTitle("${newReleases.size} new releases")
                .setContentText(artistString)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        // TODO: properly handle any failures within this function
        return Result.success()
    }
}
