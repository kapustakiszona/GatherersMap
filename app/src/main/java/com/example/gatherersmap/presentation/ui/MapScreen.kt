package com.example.gatherersmap.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.vm.MapState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    onMapClick: () -> Unit,
    itemSpots: State<MapState>,
    onAddMarkerLongClick: (LatLng) -> Unit,
    onMarkerInfoClick: (ItemSpot) -> Unit,
    onMarkerClick: (ItemSpot) -> Unit,
    temporalSpot: State<LatLng?>,
) {
    val itemsState by remember { mutableStateOf(itemSpots) }
    val tempMarkerFlow by remember { mutableStateOf(temporalSpot) }
    val oldMarkersList: MutableList<Marker>? = null
    val cameraPositionState =
        rememberCameraPositionState()

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
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
                        onMarkerInfoClick(itemSpot)
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
