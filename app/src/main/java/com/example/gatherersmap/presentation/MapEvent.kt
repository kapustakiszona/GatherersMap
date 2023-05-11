package com.example.gatherersmap.presentation

import com.example.gatherersmap.domain.model.ItemSpot
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {

    data class OnSheetEditClick(val spot: ItemSpot) : MapEvent()
    data class OnSheetDetailsClick(val spot: ItemSpot) : MapEvent()
    data class OnAddItemClick(val latLng: LatLng) : MapEvent()
    data class OnDeleteItemClick(val spot: ItemSpot) : MapEvent()
}
