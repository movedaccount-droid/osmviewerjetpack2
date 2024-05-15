package ac.uk.hope.osmviewerjetpack.displayables.search

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface SearchViewSearcher {

    val pageSize: Int
    fun getPage(page: Int): Flow<List<SearchResult>>
    fun getIcon(id: String): Flow<Uri>

}

data class SearchResult (
    val id: String,
    val name: String,
    val desc: String? = null
)