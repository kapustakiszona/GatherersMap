package com.example.gatherersmap.presentation

import com.example.gatherersmap.domain.model.ItemSpot
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {

    object Initial: MapEvent()
    data class OnSaveItemClick(val spot: ItemSpot) : MapEvent()
    data class OnEditItemClick(val spot: ItemSpot) : MapEvent()
    data class OnDetailsItemClick(val spot: ItemSpot) : MapEvent()
    data class OnAddItemLongClick(val latLng: LatLng) : MapEvent()
    data class OnDeleteItemClick(val spot: ItemSpot) : MapEvent()
}
