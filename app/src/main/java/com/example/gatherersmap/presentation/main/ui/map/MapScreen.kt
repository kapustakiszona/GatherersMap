package com.example.gatherersmap.presentation.main.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.main.vm.MapState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    onMapClick: () -> Unit,
    itemSpots: State<MapState>,
    onAddMarkerLongClick: (LatLng) -> Unit,
    onMarkerClick: (ItemSpot) -> Unit,
    temporalSpot: State<LatLng?>,
) {
    val itemsState by remember { mutableStateOf(itemSpots) }
    val tempMarkerFlow by remember { mutableStateOf(temporalSpot) }
    val oldMarkersList: MutableList<Marker>? = null
    val cameraPositionState =
        rememberCameraPositionState()
    val uiSettings = remember { MapUiSettings(myLocationButtonEnabled = true) }
    val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
// TODO: При первом запуске приложения не устанавливается превью для первого маркера
    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
//            properties = properties,
//            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState,
            onMapLongClick = { latLng ->
                onAddMarkerLongClick(latLng)
            },
            onMapClick = {
                onMapClick()
            }
        ) {
            itemsState.value.itemSpots.forEach { itemSpot ->

                oldMarkersList?.forEach { oldMarker ->
                    oldMarker.remove()
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

                tempMarkerFlow.value?.let { latLng ->
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
        }
    }
}
