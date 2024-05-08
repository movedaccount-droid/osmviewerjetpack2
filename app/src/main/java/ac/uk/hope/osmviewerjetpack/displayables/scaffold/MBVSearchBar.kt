package ac.uk.hope.osmviewerjetpack.displayables.scaffold

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MBVSearchBar(
    onSearch: (String) -> Unit = {}
) {

    val searchActive = remember { mutableStateOf(false) }
    val query = remember { mutableStateOf("") }

    SearchBar(
        query = query.value,
        onQueryChange = { query.value = it },
        onSearch = { query ->
            searchActive.value = false
            onSearch(query)
        },
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

@Preview
@Composable
private fun PreviewMBVSearchBar() {
    OSMViewerJetpackTheme {
        MBVSearchBar()
    }
}