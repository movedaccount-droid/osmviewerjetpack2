package ac.uk.hope.osmviewerjetpack.worker

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

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
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val musicBrainzRepository: MusicBrainzRepository
):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
//
//        // synchronize followed artist releases
//        musicBrainzRepository.updateFollowedCaches()
//        val followed = musicBrainzRepository.getFollowedArtists()
//
//        for (artist in followed) {
//            // check if our cache is outdated
//            if (musicBrainzRepository.)
//
//            // using paging for this seems overkill
//            do {
//
//            } while ()
//        }
//
//
//        // Do the work here--in this case, upload the images.
//        uploadImages()
//
//        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}
