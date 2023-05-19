package com.example.gatherersmap.presentation.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.MapEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: ItemSpotRepositoryImpl,
) : ViewModel() {

    private val _sheetState =
        MutableStateFlow<BottomSheetScreenState>(BottomSheetScreenState.Initial)
    val sheetState = _sheetState.asStateFlow()

    private val _itemsState = MutableStateFlow(MapState())
    val itemsState = _itemsState.asStateFlow()

    init {
        Log.d("OTAG", "Init Started")
        viewModelScope.launch {
            repository.getItemSpots().collect {
                _itemsState.value = _itemsState.value.copy(itemSpots = it)
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {

            is MapEvent.Initial -> {
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
                _sheetState.value = BottomSheetScreenState.Add(
                    itemSpot = event.spot
                )
            }

            is MapEvent.OnDeleteItemClick -> {
                deleteItemSpot(event.spot)
            }

            is MapEvent.OnDetailsItemClick -> {
                _sheetState.value =
                    BottomSheetScreenState.Details(
                        itemSpot = event.spot
                    )
            }

            is MapEvent.OnEditItemClick -> {
                _sheetState.value =
                    BottomSheetScreenState.Edit(
                        itemSpot = event.spot,
                    )
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
            delay(80)
            _sheetState.value = BottomSheetScreenState.Initial
        }
    }
}