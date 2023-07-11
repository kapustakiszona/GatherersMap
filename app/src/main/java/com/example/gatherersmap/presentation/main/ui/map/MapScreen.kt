@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)

package com.example.gatherersmap.presentation.main.ui.map

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.sydney
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    onMapClick: () -> Unit,
    onAddMarkerLongClick: (LatLng) -> Unit,
    onMarkerClick: (ItemSpot) -> Unit,
    viewModel: MapViewModel = viewModel(),
    isFabClicked: Boolean,
) {
    val context = LocalContext.current
//    val permissionStatus = remember { mutableStateOf(context.hasLocationPermissions()) }
    val itemsState by viewModel.itemsState.collectAsState()
    val tempMarkerFlow by viewModel.temporalMarker.collectAsState()
    val oldMarkersList: MutableList<Marker>? = null
    val location = viewModel.locationUpdates.collectAsState()
    val cameraPositionState =
        rememberCameraPositionState()
//    val uiSettings = remember { MapUiSettings(myLocationButtonEnabled = true) }
    val properties by remember { mutableStateOf(MapProperties()) }
    val initialLocation = Location("MyLocationProvider")
    initialLocation.apply {
        latitude = sydney.latitude
        longitude = sydney.longitude
    }
//    val locationFlow =
//        MapLocationClient(LocalContext.current).getLocationUpdates(1000)
//            .collectAsState(initialLocation)
//    Log.d(TAG, "permission status: ${permissionStatus.value}")
    cameraPositionState.position = CameraPosition.fromLatLngZoom(
        LatLng(
            location.value?.latitude ?: initialLocation.latitude,
            location.value?.longitude ?: initialLocation.longitude
        ), 6f
    )
// TODO: При первом запуске приложения не устанавливается превью для первого маркера
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = properties,
//      uiSettings = uiSettings,
        cameraPositionState = cameraPositionState,
        onMapLongClick = { latLng ->
            onAddMarkerLongClick(latLng)
        },
        onMapClick = {
            onMapClick()
        }
    ) {
        itemsState.itemSpots.forEach { itemSpot ->
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


@Composable
fun PermissionLifecycleRequest(permissionState: MultiplePermissionsState) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    if (!permissionState.shouldShowRationale)
                        permissionState.launchMultiplePermissionRequest()
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