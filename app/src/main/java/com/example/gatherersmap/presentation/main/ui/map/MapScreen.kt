@file:OptIn(MapsComposeExperimentalApi::class)

package com.example.gatherersmap.presentation.main.ui.map

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.example.gatherersmap.R
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.location.locationService
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.example.gatherersmap.presentation.permissionshandling.PermissionHandling
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@MapsComposeExperimentalApi
@Composable
fun MapScreen(
    onMapClick: () -> Unit,
    onAddMarkerLongClick: (LatLng) -> Unit,
    onMarkerClick: (ItemSpot) -> Unit,
    pickCurrentLocation: (LatLng) -> Unit,
    viewModel: MapViewModel,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val itemsState by viewModel.itemsState.collectAsState()
    val tempMarkerFlow by viewModel.temporalMarker.collectAsState()
    val oldMarkersList: MutableList<Marker>? = null
    val cameraPositionSavedState = viewModel.getCameraPosition()
    val cameraPositionState = rememberCameraPositionState {
        cameraPositionInitializer(context = context, cameraPosition = cameraPositionSavedState)
    }

    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
    var properties by remember { mutableStateOf(MapProperties()) }
    var isMapReady by remember { mutableStateOf(false) }

    SaveCameraPositionState(
        saveCameraState = {
            viewModel.saveCameraPosition(cameraPositionState.position)
        }
    )
    Box(contentAlignment = Alignment.BottomCenter) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState,
            onMapLongClick = { latLng ->
                cameraPositionState.setCameraPosition(
                    scope = scope,
                    position = latLng
                )
                onAddMarkerLongClick(latLng)
            },
            onMapClick = {
                onMapClick()
            },
            onMapLoaded = {
                Log.d(TAG, "MapScreen: onMapLoaded")
                isMapReady = true
            }
        ) {
            var zoom by remember {
                mutableFloatStateOf(0f)
            }
            if (isMapReady) {
                PermissionHandling(isAllGranted = { result ->
                    uiSettings = uiSettings.copy(myLocationButtonEnabled = false)
                    properties = properties.copy(isMyLocationEnabled = result)
                })
                Clustering(
                    items = itemsState.itemSpots,
                    onClusterClick = { cluster ->
                        if (cameraPositionState.position.zoom < zoom)
                            zoom = cameraPositionState.position.zoom
                        zoom += 3f
                        cameraPositionState.setCameraPosition(
                            scope = scope,
                            position = cluster.position,
                            zoom = zoom
                        )
                        false
                    },
                    onClusterItemClick = { itemSpot ->
                        cameraPositionState.setCameraPosition(
                            scope = scope,
                            position = itemSpot.position,
                        ) //move camera to screen center
                        onMarkerClick(itemSpot)
                        false
                    },
                    clusterItemContent = {
                        CustomMarker()
                    },
                )
            }
            oldMarkersList?.forEach { oldMarker ->
                oldMarker.remove()
            }
            tempMarkerFlow?.let { latLng ->
                MarkerInfoWindow(
                    state = MarkerState(
                        position = latLng
                    ),
                    icon = BitmapDescriptorFactory.defaultMarker(HUE_ORANGE)
                ) { marker ->
                    oldMarkersList.orEmpty().toMutableList().add(marker)
                }
            }
        }
        AddNewItemFab(
            modifier = Modifier.padding(20.dp),
            visibility = viewModel.fabLocationVisibility,
            loadingState = viewModel.getAllNetworkProgress,
            onClick = {
                locationService(context) { currentLocation ->
                    val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                    pickCurrentLocation(latLng)
                    cameraPositionState.setCameraPosition(
                        scope = scope,
                        position = latLng,
                        zoom = 15f
                    )
                }
            }
        )
    }
}

private fun CameraPositionState.setCameraPosition(
    scope: CoroutineScope,
    position: LatLng = this.position.target,
    duration: Int = 1000,
    zoom: Float = this.position.zoom
) {
    scope.launch {
        animate(
            update = CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(
                    position,
                    zoom
                )
            ),
            durationMs = duration
        )
    }
}

@Composable
private fun CustomMarker() {
    AsyncImage(
        model = R.drawable.mushroom_marker,
        contentDescription = null,
        modifier = Modifier.size(40.dp),
    )
}

@Composable
private fun SaveCameraPositionState(
    saveCameraState: () -> Unit
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                saveCameraState()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

private fun CameraPositionState.cameraPositionInitializer(
    context: Context,
    cameraPosition: CameraPosition
) {
    val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
    val statusGPS = locationManager.isProviderEnabled(GPS_PROVIDER)
    if (statusGPS) {
        locationService(context) {
            position = CameraPosition.fromLatLngZoom(LatLng(it.latitude, it.longitude), 15f)
        }
    } else {
        position = cameraPosition
    }
}
