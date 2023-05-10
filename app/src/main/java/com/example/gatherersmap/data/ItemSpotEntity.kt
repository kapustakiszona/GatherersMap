package com.example.gatherersmap.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemSpotEntity(
    val lat: Double,
    val lng: Double,
    @PrimaryKey val id: Int? = null
)
