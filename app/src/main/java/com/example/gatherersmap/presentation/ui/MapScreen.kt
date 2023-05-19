package com.example.gatherersmap.presentation.ui

import android.util.Log
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    onMapClick: () -> Unit,
    itemSpots: State<MapState>,
    onMapLongClick: (ItemSpot) -> Unit,
    onMarkerInfoClick: (ItemSpot) -> Unit,
    onMarkerClick: (ItemSpot) -> Unit,
) {
    // TODO: сделать класс превьюмаркера со двумя стейтами: isVisible и LatLng
    val itemsState by remember { mutableStateOf(itemSpots) }
    val cameraPositionState =
        rememberCameraPositionState()
    val initialMarker = remember { InitialMarker() }
    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = { latLng ->
                val spot = ItemSpot(
                    lat = latLng.latitude,
                    lng = latLng.longitude
                )
                initialMarker.latLng = latLng
                initialMarker.isVisible = true
                Log.d("OTAG", "init marker $initialMarker")
                onMapLongClick(spot)
            },
            onMapClick = {
                onMapClick()
            }
        ) {
            val currentItemLatLng =
                LatLng(itemsState.value.itemSpots.last().lat, itemsState.value.itemSpots.last().lng)
            if (currentItemLatLng == initialMarker.latLng) {
                initialMarker.latLng?.let {
                    Marker(
                        state = MarkerState(
                            position = it
                        )
                    ) { marker ->
                        marker.remove()
                    }
                }
            } else {
                initialMarker.latLng?.let {
                    Marker(
                        state = MarkerState(
                            position = it
                        )
                    )
                }
            }

// TODO: Не обновляется стейт инфоВиндов 
            itemsState.value.itemSpots.forEach { itemSpot ->

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
            }
        }
    }
}

fun InitialMarker.remove(marker: Marker) {
    marker.remove()
}