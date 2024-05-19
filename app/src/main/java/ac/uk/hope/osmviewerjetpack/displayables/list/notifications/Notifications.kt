package ac.uk.hope.osmviewerjetpack.displayables.list.notifications

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.displayables.list.ItemList
import ac.uk.hope.osmviewerjetpack.displayables.pieces.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Notifications(
    onReleaseGroupSelected: (mbid: String) -> Unit
) {
    val viewModel: NotificationsViewModel = hiltViewModel()

    when (viewModel.listCount) {
        null -> Center {
            CircularProgressIndicator()
        }
        0 -> Center {
            Column {
                Icon(
                    painter = painterResource(R.drawable.ic_playlist_24),
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = stringResource(R.string.cross_desc)
                )
                Text(
                    stringResource(R.string.caught_up),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    stringResource(R.string.no_new_releases_since_last_visit),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
        else -> ItemList(
            items = viewModel.flow,
            getItemIcon = viewModel.getItemIcon,
            onItemSelected = {
                viewModel.removeNotification(it)
                onReleaseGroupSelected(it)
            },
            defaultIcon = ImageVector.vectorResource(R.drawable.ic_album_24)
        )
    }
}