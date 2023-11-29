package com.example.gatherersmap.data.network.dto

import com.example.gatherersmap.utils.ErrorResponse
import com.google.gson.annotations.SerializedName

data class MushroomAddResponseDto(
    @SerializedName("fileName") val fileName: String?,
    @SerializedName("error") val errorResponse: ErrorResponse?,
    @SerializedName("isSuccess") val isSuccess: Boolean?,
    @SerializedName("itemId") val itemId: Int?,
)
