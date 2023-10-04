package com.example.gatherersmap.data.network.dto

import com.example.gatherersmap.utils.ErrorResponse
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MushroomsGetAllResponseDto(
    @SerializedName("error") val errorResponse: ErrorResponse? = null,
    @SerializedName("mushrooms") val mushrooms: List<MushroomResponseDto>,
)