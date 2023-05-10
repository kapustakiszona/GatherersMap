package com.example.gatherersmap.presentation.vm

import com.example.gatherersmap.domain.model.ItemSpot
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(),
    val itemSpots: List<ItemSpot> = emptyList()
)