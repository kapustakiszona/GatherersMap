package com.example.gatherersmap.presentation

import com.example.gatherersmap.domain.model.ItemSpot

sealed class MapEvent {

    object Initial: MapEvent()
    data class OnSaveItemClick(val spot: ItemSpot) : MapEvent()
    data class OnEditItemClick(val spot: ItemSpot) : MapEvent()
    data class OnDetailsItemClick(val spot: ItemSpot) : MapEvent()
    data class OnAddItemLongClick(val spot: ItemSpot) : MapEvent()
    data class OnDeleteItemClick(val spot: ItemSpot) : MapEvent()
}
