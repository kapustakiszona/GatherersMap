package com.example.gatherersmap.data.network.dto

import com.example.gatherersmap.utils.ErrorResponse
import com.google.gson.annotations.SerializedName

data class MushroomGetResponseDto(
    @SerializedName("mushroom") val mushroom: MushroomResponseDto?,
    @SerializedName("error") val errorResponse: ErrorResponse?,
)