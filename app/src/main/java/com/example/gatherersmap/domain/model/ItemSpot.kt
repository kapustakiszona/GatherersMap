package com.example.gatherersmap.domain.model

data class ItemSpot(
    var lat: Double,
    var lng: Double,
    val id: Int = 0,
    var name: String = "",
    var description: String = "",
    var image: String? = null
)
