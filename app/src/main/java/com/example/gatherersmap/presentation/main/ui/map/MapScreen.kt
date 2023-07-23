package com.example.gatherersmap.presentation.main.ui.map

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    onMapClick: () -> Unit,
    onAddMarkerLongClick: (LatLng) -> Unit,
    onMarkerClick: (ItemSpot) -> Unit,
    viewModel: MapViewModel = viewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val itemsState by viewModel.itemsState.collectAsState()
    val tempMarkerFlow by viewModel.temporalMarker.collectAsState()
    val oldMarkersList: MutableList<Marker>? = null
    val cameraPositionState =
        rememberCameraPositionState()
    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
    var properties by remember { mutableStateOf(MapProperties()) }
    var isMapReady by remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.TopStart) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState,
            onMapLongClick = { latLng ->
                onAddMarkerLongClick(latLng)
            },
            onMapClick = {
                onMapClick()
            },
            onMapLoaded = {
                isMapReady = true
            },
            onMyLocationButtonClick = {
                locationService(context) { currentLocation ->
                    scope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(
                                    LatLng(
                                        currentLocation.latitude,
                                        currentLocation.longitude
                                    ), 15f
                                )
                            )
                        )
                    }
                }
                true
            },
        ) {
            if (isMapReady) {
                PermissionHandling(isAllGranted = { result ->
                    uiSettings = uiSettings.copy(myLocationButtonEnabled = result)
                    properties = properties.copy(isMyLocationEnabled = result)
                })
            }
            oldMarkersList?.forEach { oldMarker ->
                oldMarker.remove()
            }
            tempMarkerFlow?.let { latLng ->
                Log.d(TAG, "tempMark: $latLng")
                MarkerInfoWindow(
                    state = MarkerState(
                        position = latLng
                    ),
                    icon = BitmapDescriptorFactory.defaultMarker(HUE_ORANGE)
                ) { marker ->
                    oldMarkersList.orEmpty().toMutableList().add(marker)
                }
            }
            itemsState.itemSpots.forEach { itemSpot ->
                MarkerInfoWindow(
                    state = MarkerState(
                        position = LatLng(
                            itemSpot.lat,
                            itemSpot.lng
                        )
                    ),
                    onInfoWindowClick = {
                        // TODO: придумать функционал
                    },
                    onClick = {
                        onMarkerClick(itemSpot)
                        it.showInfoWindow()
                        true
                    },
                    title = itemSpot.name,
                    snippet = itemSpot.description
                )
            }
        }
    }
}


@Composable
fun PermissionLifecycleRequest(
    request: MutableState<Boolean>,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    Log.d(TAG, "PermissionLifecycleRequest: onStart")
                    request.value = true
                }

                Lifecycle.Event.ON_RESUME -> {
                    request.value = true
                    Log.d(TAG, "PermissionLifecycleRequest: onResumed")
                }

                Lifecycle.Event.ON_STOP -> {
                    request.value = false
                }

                Lifecycle.Event.ON_CREATE -> {
                    Log.d(TAG, "PermissionLifecycleRequest: onCreate")
                }

                else -> {

                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer = observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer = observer)
        }
    }
}