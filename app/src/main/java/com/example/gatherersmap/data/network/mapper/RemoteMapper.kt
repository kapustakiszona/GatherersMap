package com.example.gatherersmap.data.network.mapper

import com.example.gatherersmap.data.network.dto.MushroomAddRequestDto
import com.example.gatherersmap.data.network.dto.MushroomDeleteRequestDto
import com.example.gatherersmap.data.network.dto.MushroomGetRequestDto
import com.example.gatherersmap.data.network.dto.MushroomResponseDto
import com.example.gatherersmap.data.network.dto.MushroomUpdateRequestDto
import com.example.gatherersmap.data.network.dto.MushroomsGetAllResponseDto
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.utils.Constants.BASE_IMAGE_URL
import com.example.gatherersmap.utils.convertUriToBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun MushroomsGetAllResponseDto.toListItemSpots(): List<ItemSpot> {
    return mushrooms.orEmpty().map { it.toItemSpot() }
}

fun MushroomResponseDto.toItemSpot(): ItemSpot {
    return ItemSpot(
        lat = lat ?: 0.0,
        lng = lon ?: 0.0,
        id = id ?: 0,
        name = name.orEmpty(),
        description = description.orEmpty(),
        image = BASE_IMAGE_URL + image.orEmpty()
    )
}

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

suspend fun ItemSpot.toInsertMushroomDto(): MushroomAddRequestDto {
    return MushroomAddRequestDto(
        description = description,
        image = withContext(Dispatchers.IO) { convertUriToBase64(image) },
        lat = lat,
        lon = lng,
        name = name,
    )
}

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