package com.example.gatherersmap.domain.model

import com.example.gatherersmap.R

data class ItemSpot(
    val lat: Double,
    val lng: Double,
    val id: Int? = null,
    var name: String = "Kurka",
    var description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam auctor.",
    var image: Int = R.drawable.detail_image
)
