package ac.uk.hope.osmviewerjetpack.displayables

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.MBVBottomBar
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.MBVNavHost
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.MBVSearchBar
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.SearchFab
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.navigateSingleTopTo
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.navigateToSearch
import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.follow_channel_name)
            val descriptionText = getString(R.string.follow_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("follow", name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }


        setContent {
            OSMViewerJetpackTheme {
                MainLayout()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout() {

    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = bottomBarScreenDestinations.find {
        it.route == currentDestination?.route
    } ?: Home

    val searchActive = remember { mutableStateOf(false) }

    val currentFab = when (currentScreen.route) {
        Home.route -> SearchFab
        ArtistSearch.route -> SearchFab
        else -> SearchFab
    }

    Scaffold(
        topBar = {
            Surface (
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (navController.previousBackStackEntry != null && !searchActive.value) {
                        IconButton(navController::navigateUp) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button_desc)
                            )
                        }
                    }
                    MBVSearchBar(
                        active = searchActive.value,
                        onActiveChanged = { searchActive.value = it },
                        Modifier.fillMaxWidth(),
                        onSearch = { query ->
                            navController.navigateToSearch(query)
                        }
                    )
                }
            }
        },
        bottomBar = {
            MBVBottomBar(
                fabIcon = currentFab.icon,
                fabIconDescription = currentFab.iconDesc,
                onClickDestination = {
                    searchActive.value = false
                    navController.navigateSingleTopTo(it)
                },
                onClickFAB = {
                    when (currentFab) {
                        SearchFab -> { searchActive.value = true }
                        else -> {}
                    }
                }
            )
        }
    ) { innerPadding ->
        MBVNavHost(
            navController,
            Modifier.padding(innerPadding)
        )
    }
}

@Preview
@Composable
fun PreviewMainLayout() {
    OSMViewerJetpackTheme {
        MainLayout()
    }
}