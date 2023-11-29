package com.example.gatherersmap.data.network.dto

import com.example.gatherersmap.utils.ErrorResponse
import com.google.gson.annotations.SerializedName

data class MushroomDeleteResponseDto(
    @SerializedName("error") val errorResponse: ErrorResponse?,
    @SerializedName("isSuccess") val isSuccess: Boolean?,
)