package com.example.gatherersmap.data.network.dto

import com.google.gson.annotations.SerializedName

data class MushroomDeleteRequestDto(
    @SerializedName("id") val id: Long,
)
