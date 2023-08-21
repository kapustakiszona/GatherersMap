package com.example.gatherersmap.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemSpotEntity(
    val lat: Double,
    val lng: Double,
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String? = null,
    var description: String? = null,
    var image: String? = null
)
