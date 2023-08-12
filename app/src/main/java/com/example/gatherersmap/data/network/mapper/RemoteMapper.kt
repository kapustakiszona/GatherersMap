package com.example.gatherersmap.data.network.mapper

import android.net.Uri
import com.example.gatherersmap.MapApp
import com.example.gatherersmap.data.network.dto.MushroomAddRequestDto
import com.example.gatherersmap.data.network.dto.MushroomDeleteRequestDto
import com.example.gatherersmap.data.network.dto.MushroomGetRequestDto
import com.example.gatherersmap.data.network.dto.MushroomResponseDto
import com.example.gatherersmap.data.network.dto.MushroomsGetAllResponseDto
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.utils.toBase64

const val BASE_IMAGE_URL = "http://185.105.88.49:8080/mushroom/images/"
fun MushroomsGetAllResponseDto.toListItemSpots(): List<ItemSpot> {
    return mushrooms.map { it.toItemSpot() }
}

fun MushroomResponseDto.toItemSpot(): ItemSpot {
    return ItemSpot(
        lat = lat,
        lng = lon,
        id = id,
        name = name,
        description = description,
        image = BASE_IMAGE_URL + image
    )
}

private fun tryReadFile(image: String): String? {
    val contentResolver = MapApp.instance.contentResolver
    val androidUri = Uri.parse(image)
    try {
        contentResolver.query(
            androidUri,
            null,
            null,
            null,
            null
        )!!.use { cursor ->
            if (cursor.count == 0) throw IllegalStateException("File not found")
            cursor.moveToFirst()
            val bytes = contentResolver.openInputStream(androidUri)!!.use { inputStream ->
                inputStream.readBytes()
            }
            return bytes.toBase64()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
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
        image = tryReadFile(image.orEmpty()) ?: "",
        lat = lat,
        lon = lng,
        name = name
    )
}