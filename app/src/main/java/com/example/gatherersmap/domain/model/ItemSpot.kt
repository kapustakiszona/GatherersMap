package com.example.gatherersmap.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemSpot(
    var lat: Double,
    var lng: Double,
    val id: Int = 0,
    var name: String = "",
    var description: String = "",
    var image: String? = null
)
