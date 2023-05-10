package com.example.gatherersmap.presentation.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.MapEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: ItemSpotRepositoryImpl,
) : ViewModel() {
    var state by mutableStateOf(MapState())


    init {
        viewModelScope.launch {
            repository.getItemSpots().collectLatest { itemSpots ->
                state = state.copy(itemSpots = itemSpots)
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnAddItemButtonClick -> {
                viewModelScope.launch {
                    repository.insertItemSpot(
                        spot = ItemSpot(
                            event.latLng.latitude,
                            event.latLng.longitude
                        )
                    )
                }

            }

            is MapEvent.OnDeleteItemClick -> {
                viewModelScope.launch {
                    repository.deleteItemSpot(
                        spot = event.spot
                    )
                }
            }
        }
    }

}