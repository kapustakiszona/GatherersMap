package com.example.gatherersmap.domain.model

import com.example.gatherersmap.R

data class ItemSpot(
    var lat: Double,
    var lng: Double,
    val id: Int = 0,
    var name: String = "",
    var description: String = "",
    var image: Int = R.drawable.detail_image
)
