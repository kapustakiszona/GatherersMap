package com.example.gatherersmap.data.network

import com.example.gatherersmap.data.network.dto.MushroomAddRequestDto
import com.example.gatherersmap.data.network.dto.MushroomAddResponseDto
import com.example.gatherersmap.data.network.dto.MushroomDeleteRequestDto
import com.example.gatherersmap.data.network.dto.MushroomDeleteResponseDto
import com.example.gatherersmap.data.network.dto.MushroomGetRequestDto
import com.example.gatherersmap.data.network.dto.MushroomGetResponseDto
import com.example.gatherersmap.data.network.dto.MushroomUpdateRequestDto
import com.example.gatherersmap.data.network.dto.MushroomUpdateResponseDto
import com.example.gatherersmap.data.network.dto.MushroomsGetAllResponseDto
import com.example.gatherersmap.domain.model.ItemSpot
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MushroomService {

    @POST("/mushroom/add")
    suspend fun insertItemSpot(@Body spot: MushroomAddRequestDto): Response<MushroomAddResponseDto>

    @POST("/mushroom/delete")
    suspend fun deleteItemSpot(@Body spotId: MushroomDeleteRequestDto): Response<MushroomDeleteResponseDto>

    @POST("/mushroom/get")
    suspend fun getItemSpot(@Body spotId: MushroomGetRequestDto): Response<MushroomGetResponseDto>

    @POST("/mushroom/update")
    suspend fun updateItemSpotDetails(@Body spot: MushroomUpdateRequestDto): Response<MushroomUpdateResponseDto>

    @POST("/mushroom/getAll")
    suspend fun getItemSpots(): Response<MushroomsGetAllResponseDto>
}