package com.example.gatherersmap.presentation.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.MapEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: ItemSpotRepositoryImpl,
) : ViewModel() {
    var state by mutableStateOf(MapState())

    private val _sheetState = MutableLiveData<BottomSheetScreenState>(BottomSheetScreenState.Start)
    val sheetState: LiveData<BottomSheetScreenState> = _sheetState

    init {
        viewModelScope.launch {
            repository.getItemSpots().collectLatest { itemSpots ->
                state = state.copy(itemSpots = itemSpots)
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnAddItemClick -> {
                viewModelScope.launch {
                    repository.insertItemSpot(
                        spot = ItemSpot(
                            lat = event.latLng.latitude,
                            lng = event.latLng.longitude,
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

            is MapEvent.OnSheetDetailsClick -> {
                _sheetState.value =
                    BottomSheetScreenState.Details(
                        itemSpot = event.spot
                    )
            }

            is MapEvent.OnSheetEditClick -> {
                _sheetState.value =
                    BottomSheetScreenState.Edit(
                        itemSpot = event.spot,
                    )
            }
        }
    }

}