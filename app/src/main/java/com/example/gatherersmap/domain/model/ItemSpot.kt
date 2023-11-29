package com.example.gatherersmap.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemSpot(
    val lat: Double,
    val lng: Double,
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String? = null
)
