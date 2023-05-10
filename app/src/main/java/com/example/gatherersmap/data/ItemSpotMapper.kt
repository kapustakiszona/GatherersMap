package com.example.gatherersmap.data

import com.example.gatherersmap.domain.model.ItemSpot

fun ItemSpotEntity.toItemSpot(): ItemSpot {
    return ItemSpot(
        lat = lat,
        lng = lng,
        id = id
    )
}

fun ItemSpot.toItemSpotEntity(): ItemSpotEntity {
    return ItemSpotEntity(
        lat = lat,
        lng = lng,
        id = id
    )
}