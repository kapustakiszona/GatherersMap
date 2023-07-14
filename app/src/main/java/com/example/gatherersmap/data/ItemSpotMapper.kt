package com.example.gatherersmap.data

import com.example.gatherersmap.domain.model.ItemSpot

fun ItemSpotEntity.toItemSpot(): ItemSpot {
    return ItemSpot(
        lat = lat,
        lng = lng,
        id = id,
        name = name ?: "",
        description = description ?: "",
        image = image
    )
}

fun ItemSpot.toItemSpotEntity(): ItemSpotEntity {
    return ItemSpotEntity(
        lat = lat,
        lng = lng,
        id = id,
        name = name,
        description = description,
        image = image
    )
}