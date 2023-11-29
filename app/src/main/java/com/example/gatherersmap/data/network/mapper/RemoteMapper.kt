package com.example.gatherersmap.data.network.mapper

import com.example.gatherersmap.data.network.dto.MushroomAddRequestDto
import com.example.gatherersmap.data.network.dto.MushroomDeleteRequestDto
import com.example.gatherersmap.data.network.dto.MushroomGetRequestDto
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.utils.convertBmpUriToBase64


fun EditedItemSpot.toItemSpot(): ItemSpot {
    return ItemSpot(
        description = description ?: "",
        id = id,
        image = image,
        lat = lat ?: 0.0,
        lng = lng ?: 0.0,
        name = name ?: ""
    )
}

fun ItemSpot.toMushroomDeleteRequestDto(): MushroomDeleteRequestDto {
    return MushroomDeleteRequestDto(
        id = id.toLong()
    )
}

fun ItemSpot.toMushroomGetRequestDto(): MushroomGetRequestDto {
    return MushroomGetRequestDto(
        id = id.toLong()
    )
}

fun ItemSpot.toInsertMushroomDto(): MushroomAddRequestDto {
    return MushroomAddRequestDto(
        description = description,
        image = image.convertBmpUriToBase64(),
        lat = lat,
        lon = lng,
        name = name,
    )
}

