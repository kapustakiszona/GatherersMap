package com.example.gatherersmap.data.network.dto

data class MushroomAddRequestDto(
    val description: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val image: String,
)
