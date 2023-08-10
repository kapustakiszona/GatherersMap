package com.example.gatherersmap.data.network

import com.example.gatherersmap.data.network.dto.MushroomAddResponseDto
import com.example.gatherersmap.data.network.dto.MushroomDeleteResponseDto
import com.example.gatherersmap.data.network.dto.MushroomGetResponseDto
import com.example.gatherersmap.data.network.dto.MushroomsGetAllResponseDto
import com.example.gatherersmap.data.network.mapper.toInsertMushroomDto
import com.example.gatherersmap.data.network.mapper.toMushroomIdRequestDto
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.utils.NetworkResult
import com.example.gatherersmap.utils.handleApi

class MushroomApi(
    private val apiService: MushroomService,
) {
    suspend fun getItemSpot(itemSpot: ItemSpot): NetworkResult<MushroomGetResponseDto> {
        return handleApi { apiService.getItemSpot(spotId = itemSpot.toMushroomIdRequestDto()) }
    }

    suspend fun getAllItemSpots(): NetworkResult<MushroomsGetAllResponseDto> {
        return handleApi { apiService.getItemSpots() }
    }

    suspend fun insertItemSpot(itemSpot: ItemSpot): NetworkResult<MushroomAddResponseDto> {
        return handleApi { apiService.insertItemSpot(spot = itemSpot.toInsertMushroomDto()) }
    }

    suspend fun deleteItemSpot(itemSpot: ItemSpot): NetworkResult<MushroomDeleteResponseDto> {
        return handleApi { apiService.deleteItemSpot(spotId = itemSpot.toMushroomIdRequestDto()) }
    }


}