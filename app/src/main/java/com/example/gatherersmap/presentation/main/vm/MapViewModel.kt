package com.example.gatherersmap.presentation.main.vm

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.domain.model.LocationStateModel
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.location.LocationState
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.ui.map.MapEvent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
class MapViewModel(
    private val repository: ItemSpotRepositoryImpl,
) : ViewModel() {

    private val _sheetState =
        MutableStateFlow<BottomSheetScreenState>(BottomSheetScreenState.Initial)
    val sheetState = _sheetState.asStateFlow()

    private val _itemsState = MutableStateFlow(MapState())
    val itemsState = _itemsState.asStateFlow()

    private val _temporalMarker = MutableStateFlow<LatLng?>(null)
    val temporalMarker = _temporalMarker.asStateFlow()

    private val _locationState = MutableStateFlow(LocationStateModel())
    val locationState = _locationState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getItemSpots().collect {
                _itemsState.value = _itemsState.value.copy(itemSpots = it)
            }
        }
    }

    @Composable
    fun OnLocationEvent(locationState: LocationState) {
        when (locationState) {
            LocationState.Initial -> TODO()

            is LocationState.Failure -> {
                when {
                    !locationState.locationModel.networkStatus -> {
                        Log.e(TAG, "Network is disabled")
                    }

                    !locationState.locationModel.gpsStatus -> {
                        Log.e(TAG, "GPS is disabled")
                    }

                    !locationState.locationModel.permissionStatus.isGranted -> {

                    }
                }
            }

            is LocationState.Success -> {
                TODO()
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {

            is MapEvent.Initial -> {
                removeTemporalMarker()
                setDefaultSheetState()
            }

            is MapEvent.OnSaveItemClick -> {
                insertItemSpot(
                    ItemSpot(
                        lat = event.spot.lat,
                        lng = event.spot.lng,
                    )
                )
            }

            is MapEvent.OnAddItemLongClick -> {
                _temporalMarker.update {
                    event.latLng
                }
                _sheetState.update {
                    BottomSheetScreenState.Add(
                        itemSpot = ItemSpot(
                            lng = event.latLng.longitude,
                            lat = event.latLng.latitude
                        )
                    )
                }
            }

            is MapEvent.OnDeleteItemClick -> {
                deleteItemSpot(event.spot)
            }

            is MapEvent.OnDetailsItemClick -> {
                removeTemporalMarker()
                _sheetState.update {
                    BottomSheetScreenState.Details(
                        itemSpot = event.spot
                    )
                }
            }

            is MapEvent.OnEditItemClick -> {
                _sheetState.update {
                    BottomSheetScreenState.Edit(
                        itemSpot = event.spot,
                    )
                }
            }
        }
    }

    fun insertItemSpot(itemSpot: ItemSpot) {
        viewModelScope.launch {
            repository.insertItemSpot(itemSpot)
        }
    }

    fun deleteItemSpot(itemSpot: ItemSpot) {
        viewModelScope.launch {
            repository.deleteItemSpot(itemSpot)
        }
    }

    fun updateItemSpot(itemSpot: ItemSpot) {
        viewModelScope.launch {
            repository.insertItemSpot(itemSpot)
        }
    }

    private fun setDefaultSheetState() {
        viewModelScope.launch {
            delay(150)
            _sheetState.update {
                BottomSheetScreenState.Initial
            }
        }
    }

    private fun removeTemporalMarker() {
        _temporalMarker.update {
            null
        }
    }
}