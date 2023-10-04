package com.example.gatherersmap.data.network.mapper

import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.utils.convertUriToBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

suspend fun compareSpots(oldSpot: ItemSpot, newSpot: ItemSpot): EditedItemSpot {
    val updName = if (oldSpot.name == newSpot.name) null else newSpot.name
    val updDesc = if (oldSpot.description == newSpot.description) null else newSpot.description
    val updImg = if (oldSpot.image == newSpot.image) null else withContext(Dispatchers.IO) {
        convertUriToBase64(newSpot.image)
    }
    return EditedItemSpot(
        lat = null,
        lng = null,
        id = oldSpot.id,
        name = updName,
        description = updDesc,
        image = updImg
    )
}