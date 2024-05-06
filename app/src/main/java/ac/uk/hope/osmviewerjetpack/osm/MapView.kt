package ac.uk.hope.osmviewerjetpack.osm

import ac.uk.hope.osmviewerjetpack.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.launch
import org.osmdroid.views.MapView

// we follow lifetime specifications as in section 8 here
// https://developer.android.com/codelabs/jetpack-compose-advanced-state-side-effects
// especially important here due to the nature of osm's onresume function

@Composable
fun ComposeMapView(
    modifier: Modifier = Modifier,
    onLoad: (map: MapView) -> Unit = {},
) {
    val mapView = rememberMapViewWithLifecycle()
    MapViewContainer(mapView, onLoad, modifier)
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(key1 = lifecycle, key2 = mapView) {
        val lifecycleObserver = getMapLifecycleObserver(mapView)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

private fun getMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            else -> {}
        }
    }

@Composable
private fun MapViewContainer(
    map: MapView,
    onLoad: (map: MapView) -> Unit = {},
    modifier: Modifier = Modifier
) {
    AndroidView({ map }, modifier) { mapView -> onLoad.invoke(mapView) }
}