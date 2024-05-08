package ac.uk.hope.osmviewerjetpack.displayables

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.displayables.home.HomeScreen
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.MBVBottomBar
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.MBVNavHost
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.MBVSearchBar
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.navigateSingleTopTo
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.navigateToSearch
import ac.uk.hope.osmviewerjetpack.displayables.search.ArtistList
import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                onClickDestination = { dest ->
                    navController.navigateSingleTopTo(dest)
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