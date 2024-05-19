package ac.uk.hope.osmviewerjetpack.displayables.scaffold

import ac.uk.hope.osmviewerjetpack.displayables.ArtistSearch
import ac.uk.hope.osmviewerjetpack.displayables.Artist
import ac.uk.hope.osmviewerjetpack.displayables.FollowedArtists
import ac.uk.hope.osmviewerjetpack.displayables.Home
import ac.uk.hope.osmviewerjetpack.displayables.Release
import ac.uk.hope.osmviewerjetpack.displayables.artist.ArtistView
import ac.uk.hope.osmviewerjetpack.displayables.release.ReleaseView
import ac.uk.hope.osmviewerjetpack.displayables.lazy_page_list.artist.ArtistSearch
import ac.uk.hope.osmviewerjetpack.displayables.list.followedArtist.FollowedArtistBrowse
import ac.uk.hope.osmviewerjetpack.displayables.list.notifications.Notifications
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
            Notifications(
                onReleaseGroupSelected = { mbid ->
                    navController.navigateToRelease(mbid)
                }
            )
        }
        composable(
            route = ArtistSearch.routeWithArgs,
            arguments = ArtistSearch.arguments,
            deepLinks = ArtistSearch.deepLinks
        ) { navBackStackEntry ->
            // this should never be null, so if it is i want to know why
            val query = navBackStackEntry.arguments?.getString(ArtistSearch.queryArg)!!
            ArtistSearch(
                query = query,
                onArtistSelected = { mbid ->
                    navController.navigateToArtist(mbid)
                }
            )
        }
        composable(
            route = Artist.routeWithArgs,
            arguments = Artist.arguments,
            deepLinks = Artist.deepLinks
        ) { navBackStackEntry ->
            val mbid = navBackStackEntry.arguments?.getString(Artist.mbidArg)
            ArtistView(
                mbid = mbid!!, // this should never be null, so if it is i want to know why
                onReleaseSelected = { releaseGroupMbid ->
                    navController.navigateToRelease(releaseGroupMbid)
                }
            )
        }
        composable(
            route = Release.routeWithArgs,
            arguments = Release.arguments,
            deepLinks = Release.deepLinks
        ) { navBackStackEntry ->
            val releaseGroupMbid = navBackStackEntry.arguments?.getString(Release.mbidArg)
            ReleaseView(releaseGroupMbid!!)
        }
        composable(route = FollowedArtists.route) {
            FollowedArtistBrowse(
                onArtistSelected = { mbid ->
                    navController.navigateToArtist(mbid)
                }
            )
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

fun NavHostController.navigateToArtist(mbid: String) {
    this.navigate("${Artist.route}/$mbid")
}

fun NavHostController.navigateToRelease(releaseGroupMbid: String) {
    this.navigate("${Release.route}/$releaseGroupMbid")
}