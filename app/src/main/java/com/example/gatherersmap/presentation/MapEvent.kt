package com.example.gatherersmap.presentation

import com.example.gatherersmap.domain.model.ItemSpot
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent{

    data class OnAddItemButtonClick(val latLng: LatLng): MapEvent()

    data class OnDeleteItemClick(val spot: ItemSpot): MapEvent()
}
