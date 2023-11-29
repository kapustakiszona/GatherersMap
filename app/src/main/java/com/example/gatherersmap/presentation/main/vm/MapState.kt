package com.example.gatherersmap.presentation.main.vm

import com.example.gatherersmap.domain.model.ItemSpot

data class MapState(
    val itemSpots: List<ItemSpot> = emptyList(),
)