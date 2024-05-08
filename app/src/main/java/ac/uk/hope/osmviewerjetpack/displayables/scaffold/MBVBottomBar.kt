package ac.uk.hope.osmviewerjetpack.displayables.scaffold

import ac.uk.hope.osmviewerjetpack.displayables.bottomBarScreenDestinations
import ac.uk.hope.osmviewerjetpack.displayables.navigateSingleTopTo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun MBVBottomBar(
    onClickDestination: (dest: String) -> Unit = {},
    onClickButton: () -> Unit = {},
) {
    BottomAppBar(
        actions = {
            bottomBarScreenDestinations.forEach { dest ->
                IconButton(onClick = { onClickDestination(dest.route) }) {
                    Icon(
                        dest.icon,
                        contentDescription = stringResource(id = dest.iconDesc)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        }
    )
}