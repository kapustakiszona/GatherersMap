package com.example.gatherersmap.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class MushroomResponseDto(
    val description: String?,
    val id: Int?,
    val image: String?,
    val lat: Double?,
    val lon: Double?,
    val name: String?
)