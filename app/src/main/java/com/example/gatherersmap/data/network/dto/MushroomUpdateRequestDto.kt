package com.example.gatherersmap.data.network.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MushroomUpdateRequestDto(
    @SerializedName("description") val description: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lng: Double?,
    @SerializedName("name") val name: String?,
)