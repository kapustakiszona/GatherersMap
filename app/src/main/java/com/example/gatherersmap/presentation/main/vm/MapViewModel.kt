package com.example.gatherersmap.presentation.main.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.data.network.mapper.toListItemSpots
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.ui.bottomsheet.BottomSheetVisibility
import com.example.gatherersmap.presentation.main.ui.map.MapEvent
import com.example.gatherersmap.utils.NetworkResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    private val _sheetVisibleState = MutableStateFlow(BottomSheetVisibility.INITIAL)
    val sheetVisibleState = _sheetVisibleState.asStateFlow()

    var insertLoading by mutableStateOf(false)
    var getAllLoading by mutableStateOf(false)
    var deleteLoading by mutableStateOf(false)
    var updateLoading by mutableStateOf(false)


    init {
        getAllItemSpots()
    }

    fun setVisibilities(visibility: BottomSheetVisibility) {
        when (visibility) {
            BottomSheetVisibility.HIDE -> _sheetVisibleState.update { visibility }
            BottomSheetVisibility.SHOW -> _sheetVisibleState.update { visibility }
            BottomSheetVisibility.INITIAL -> {}
        }

    }

    fun onEvent(event: MapEvent) {
        when (event) {

            is MapEvent.Initial -> {
                Log.d(TAG, "onEvent: Initial State")
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

            is MapEvent.OnAddItemClick -> {
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
                _sheetVisibleState.update { BottomSheetVisibility.SHOW }
                _sheetState.update {
                    BottomSheetScreenState.Edit(
                        itemSpot = event.spot,
                    )
                }
            }
        }
    }

    fun getAllItemSpots() {
        viewModelScope.launch {
            getAllLoading = true
            repository.getAllItemSpotsRemote()
                .collectLatest { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            _itemsState.update {
                                it.copy(itemNetworkError = result.errorMessage)
                            }
                        }

                        is NetworkResult.Success -> {
                            _itemsState.update {
                                it.copy(itemSpots = result.data.toListItemSpots())
                            }
                        }
                    }
                }
            getAllLoading = false
        }
    }

    fun insertItemSpot(itemSpot: ItemSpot) {
        viewModelScope.launch(Dispatchers.IO) {
            insertLoading = true
            when (val result = repository.insertItemSpotRemote(itemSpot)) {
                is NetworkResult.Error -> {
                    _itemsState.value =
                        _itemsState.value.copy(itemNetworkError = result.errorMessage)
                }

                is NetworkResult.Success -> {
                    val fileName = result.data.fileName
                }
            }
            insertLoading = false
            _sheetVisibleState.update { BottomSheetVisibility.HIDE }
            getAllItemSpots()
            onEvent(MapEvent.Initial)
        }
    }

    fun deleteItemSpot(itemSpot: ItemSpot) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteLoading = true
            when (val result = repository.deleteItemSpotRemote(itemSpot)) {
                is NetworkResult.Error -> {
                    _itemsState.value =
                        _itemsState.value.copy(itemNetworkError = result.errorMessage)
                }

                is NetworkResult.Success -> {
                    val isSuccess = result.data.isSuccess
                }
            }
            deleteLoading = false
            getAllItemSpots()
            _sheetVisibleState.update { BottomSheetVisibility.HIDE }
            onEvent(MapEvent.Initial)
        }
    }

    fun updateItemSpot(itemSpot: ItemSpot) {            //insert inside
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertItemSpotRemote(itemSpot)
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
        Log.d(TAG, "removeTemporalMarker: tempMarker is null")
        _temporalMarker.update {
            null
        }
    }

    private fun getItemSpotRemote(itemSpot: ItemSpot) {
        viewModelScope.launch {
            repository.getItemSpotRemote(itemSpot)
        }
    }
}