package com.example.gatherersmap.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gatherersmap.R

@Entity
data class ItemSpotEntity(
    val lat: Double,
    val lng: Double,
    @PrimaryKey val id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var image: Int = R.drawable.detail_image
)
