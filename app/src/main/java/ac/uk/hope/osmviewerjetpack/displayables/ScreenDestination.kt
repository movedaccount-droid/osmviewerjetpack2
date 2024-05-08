package ac.uk.hope.osmviewerjetpack.displayables

import ac.uk.hope.osmviewerjetpack.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

// see section 6 https://developer.android.com/codelabs/jetpack-compose-navigation

interface ScreenDestination {
    val icon: ImageVector
    val iconDesc: Int
    val route: String
}

object Home: ScreenDestination {
    override val icon = Icons.Default.Home
    override val iconDesc = R.string.home_icon_desc
    override val route = "home"
}

object ArtistSearch: ScreenDestination {
    override val icon = Icons.Default.Search
    override val iconDesc = R.string.search_icon_desc
    override val route = "search"
    val queryArg = "query"
    val routeWithArgs = "${route}/{${queryArg}}"
    val arguments = listOf(
        navArgument(queryArg) {
            type = NavType.StringType
        }
    )
    val deepLinks = listOf(navDeepLink {
        uriPattern = "musicbrainzviewer://$routeWithArgs"
    })
}

val bottomBarScreenDestinations = listOf(Home, ArtistSearch)