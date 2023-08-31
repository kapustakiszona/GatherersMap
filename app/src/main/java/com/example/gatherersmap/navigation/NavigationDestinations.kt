package com.example.gatherersmap.navigation

import com.example.gatherersmap.domain.model.ItemSpot
import com.google.android.gms.maps.model.LatLng

sealed class NavigationDestinations {
    object Map : NavigationDestinations()
    data class Edit(val itemSpot: ItemSpot) : NavigationDestinations()
    data class Details(val itemSpot: ItemSpot) : NavigationDestinations()
    data class Add(val latLng: LatLng) : NavigationDestinations()
    object Current : NavigationDestinations()
}