package ac.uk.hope.osmviewerjetpack.displayables.scaffold

import ac.uk.hope.osmviewerjetpack.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

interface fabType {
    val icon: ImageVector
    val iconDesc: Int
}

object SearchFab: fabType {
    override val icon = Icons.Default.Search
    override val iconDesc = R.string.search_fab_desc
}