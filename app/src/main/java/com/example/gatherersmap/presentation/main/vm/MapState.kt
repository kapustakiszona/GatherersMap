package com.example.gatherersmap.presentation.main.vm

import com.example.gatherersmap.domain.model.ItemSpot
import com.google.maps.android.compose.MapProperties

data class MapState(
    val itemSpots: List<ItemSpot> = emptyList()
)