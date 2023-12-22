@file:OptIn(MapsComposeExperimentalApi::class, MapsComposeExperimentalApi::class)

package com.example.gatherersmap.presentation.main.ui.map

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
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
    val cameraPositionState = rememberCameraPositionState()

    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
    var properties by remember { mutableStateOf(MapProperties()) }
    var isMapReady by remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.BottomCenter) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState,
            onMapLongClick = { latLng ->
                cameraUpdate(
                    scope = scope,
                    camPosState = cameraPositionState,
                    position = latLng,
                    zoom = cameraPositionState.position.zoom
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
                        if (cameraPositionState.position.zoom < zoom) zoom =
                            cameraPositionState.position.zoom
                        zoom += 3f
                        cameraUpdate(
                            scope = scope, camPosState = cameraPositionState,
                            position = cluster.position,
                            zoom = zoom
                        )
                        false
                    },
                    onClusterItemClick = { itemSpot ->
                        cameraUpdate( //move camera to screen center
                            scope = scope,
                            camPosState = cameraPositionState,
                            position = itemSpot.position,
                            zoom = cameraPositionState.position.zoom
                        )
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
                    cameraUpdate(
                        scope = scope,
                        camPosState = cameraPositionState,
                        position = latLng,
                        zoom = 15f
                    )
                }
            }
        )
    }
}

private fun cameraUpdate(
    scope: CoroutineScope,
    camPosState: CameraPositionState,
    position: LatLng,
    duration: Int = 1000,
    zoom: Float
) {
    scope.launch {
        camPosState.animate(
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