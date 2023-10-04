package com.example.gatherersmap.data.network.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MushroomUpdateResponseDto(
    @SerializedName("error") val errorResponse: String?,
    @SerializedName("isSuccess") val isSuccess: Boolean?,
)