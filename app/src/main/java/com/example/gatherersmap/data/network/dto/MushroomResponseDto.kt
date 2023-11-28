package com.example.gatherersmap.data.network.dto

import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.utils.Constants
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

fun MushroomResponseDto.toItemSpot(): ItemSpot {
    return ItemSpot(
        lat = lat ?: 0.0,
        lng = lon ?: 0.0,
        id = id ?: 0,
        name = name.orEmpty(),
        description = description.orEmpty(),
        image = Constants.BASE_IMAGE_URL + image.orEmpty()
    )
}