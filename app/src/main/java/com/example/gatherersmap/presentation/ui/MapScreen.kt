package com.example.gatherersmap.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.data.ItemSpotDatabase
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.presentation.MapEvent
import com.example.gatherersmap.presentation.vm.MapViewModel
import com.example.gatherersmap.presentation.vm.MapViewModelFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(onMapClickListener: () -> Unit) {
    val viewModel: MapViewModel =
        viewModel(factory = MapViewModelFactory(repositoryImpl = ItemSpotRepositoryImpl(database = ItemSpotDatabase.Companion)))
    val cameraPositionState = rememberCameraPositionState()

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = {
                viewModel.onEvent(MapEvent.OnAddItemClick(it))
            },
            onMapClick = {
                onMapClickListener()
            }
        ) {
            viewModel.state.itemSpots.forEach { itemSpot ->
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            itemSpot.lat, itemSpot.lng
                        )
                    ),
                    title = "item spot ${itemSpot.id}, ${itemSpot.name}",
                    onInfoWindowClick = {
                        viewModel.onEvent(MapEvent.OnDeleteItemClick(itemSpot))
                    },
                    onClick = {
                        viewModel.onEvent(MapEvent.OnSheetDetailsClick(itemSpot))
                        it.showInfoWindow()
                        true
                    }
                )
            }
        }
    }
}