package ac.uk.hope.osmviewerjetpack.displayables.scaffold

import ac.uk.hope.osmviewerjetpack.displayables.ArtistSearch
import ac.uk.hope.osmviewerjetpack.displayables.Home
import ac.uk.hope.osmviewerjetpack.displayables.home.HomeScreen
import ac.uk.hope.osmviewerjetpack.displayables.search.ArtistList
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MBVNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            // TODO: eventually pass navcontroller down for item selection
            HomeScreen()
        }
        composable(
            route = ArtistSearch.routeWithArgs,
            arguments = ArtistSearch.arguments,
            deepLinks = ArtistSearch.deepLinks
        ) { navBackStackEntry ->
            val query = navBackStackEntry.arguments?.getString(ArtistSearch.queryArg)
            // TODO: eventually pass navcontroller down for searching
            ArtistList(query!!) // this should never be null, so if it is i want to know why
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
        //  restoreState = true

        launchSingleTop = true
    }

fun NavHostController.navigateToSearch(query: String) {
    this.navigate("${ArtistSearch.route}/$query")
}