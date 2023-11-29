package com.example.gatherersmap.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemSpotEntity(
    val lat: Double,
    val lng: Double,
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String? = null,
    val description: String? = null,
    val image: String? = null,
)
