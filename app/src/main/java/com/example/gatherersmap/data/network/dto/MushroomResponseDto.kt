package com.example.gatherersmap.data.network.dto

data class MushroomResponseDto(
    val description: String,
    val id: Int,
    val image: String,
    val lat: Double,
    val lon: Double,
    val name: String
)