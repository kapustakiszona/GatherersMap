package com.example.gatherersmap.data.network.mapper

import com.example.gatherersmap.data.network.dto.MushroomUpdateRequestDto
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.utils.convertBmpUriToBase64
import kotlinx.serialization.Serializable

@Serializable
data class EditedItemSpot(
    val lat: Double? = null,
    val lng: Double? = null,
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    val image: String? = null,
)

fun EditedItemSpot.toMushroomUpdateRequestDto(): MushroomUpdateRequestDto {
    return MushroomUpdateRequestDto(
        description = description,
        id = id,
        image = image,
        lat = lat,
        lng = lng,
        name = name
    )
}

fun compareSpots(oldSpot: ItemSpot, newSpot: ItemSpot): EditedItemSpot {
    val updName = if (oldSpot.name == newSpot.name) null else newSpot.name
    val updDesc = if (oldSpot.description == newSpot.description) null else newSpot.description
    val updImg = if (oldSpot.image == newSpot.image) null else newSpot.image.convertBmpUriToBase64()
    return EditedItemSpot(
        id = oldSpot.id,
        name = updName,
        description = updDesc,
        image = updImg
    )
}