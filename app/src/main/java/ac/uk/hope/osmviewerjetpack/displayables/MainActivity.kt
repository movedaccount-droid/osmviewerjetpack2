package ac.uk.hope.osmviewerjetpack.displayables

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.displayables.home.HomeScreen
import ac.uk.hope.osmviewerjetpack.displayables.scaffold.MBVBottomBar
import ac.uk.hope.osmviewerjetpack.displayables.search.ArtistList
import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    // TODO: move into searchbar component and hoist return functions
    val searchActive = remember { mutableStateOf(false) }
    val query = remember { mutableStateOf("") }

    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = bottomBarScreenDestinations.find {
        it.route == currentDestination?.route
    } ?: Home

    Scaffold(
        topBar = {
            Surface (
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                SearchBar(
                    query = query.value,
                    onQueryChange = { query.value = it },
                    onSearch = {},
                    active = searchActive.value,
                    onActiveChange = { searchActive.value = it },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_icon_desc)
                        )
                    },
                    modifier = Modifier.padding(if (searchActive.value) 0.dp else 8.dp)
                ) {
                    LazyColumn {
                        items(5) {
                            Text(text = "some")
                        }
                    }
                }
            }
        },
        bottomBar = {
            MBVBottomBar(
                onClickDestination = { dest -> navController.navigateSingleTopTo(dest) }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Home.route) {
                HomeScreen()
            }
            composable(route = ArtistSearch.route) {
                ArtistList()
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // TODO: saving seems a problem. the only implementation, popUpTo, doesn't just let us
        // "save this and move," and instead forces us to pop up to a constant location, expected
        // to be our home screen. why can't we just save???
        // so right now this doesn't work. whtaever

        //  popUpTo(route) {
        //      saveState = true
        //  }

        launchSingleTop = true
        restoreState = true
    }

@Preview
@Composable
fun PreviewMainLayout() {
    OSMViewerJetpackTheme {
        MainLayout()
    }
}