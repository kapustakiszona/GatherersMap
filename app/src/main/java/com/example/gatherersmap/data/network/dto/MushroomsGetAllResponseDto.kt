package com.example.gatherersmap.data.network.dto

import com.example.gatherersmap.utils.ErrorResponse
import com.google.gson.annotations.SerializedName

data class MushroomsGetAllResponseDto(
    @SerializedName("error") val errorResponse: ErrorResponse,
    @SerializedName("mushrooms") val mushrooms: List<MushroomResponseDto>,
)