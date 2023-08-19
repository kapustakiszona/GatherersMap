package com.example.gatherersmap.data.network

import com.example.gatherersmap.data.network.dto.MushroomAddResponseDto
import com.example.gatherersmap.data.network.dto.MushroomDeleteResponseDto
import com.example.gatherersmap.data.network.dto.MushroomGetResponseDto
import com.example.gatherersmap.data.network.dto.MushroomsGetAllResponseDto
import com.example.gatherersmap.data.network.mapper.toInsertMushroomDto
import com.example.gatherersmap.data.network.mapper.toMushroomDeleteRequestDto
import com.example.gatherersmap.data.network.mapper.toMushroomGetRequestDto
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.utils.NetworkResult
import com.example.gatherersmap.utils.handleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MushroomApi(
    private val apiService: MushroomService,
) {
    suspend fun getItemSpot(itemSpot: ItemSpot) = withContext(Dispatchers.IO) {
        try {
            handleApi { apiService.getItemSpot(spotId = itemSpot.toMushroomGetRequestDto()) }
        } catch (e: Exception) {
            NetworkResult.Error(errorMessage = e.message.toString())
        }
    }

    suspend fun getAllItemSpots() = withContext(Dispatchers.IO) {
        try {
            handleApi { apiService.getItemSpots() }
        } catch (e: Exception) {
            NetworkResult.Error(errorMessage = e.message.toString())
        }
    }

    suspend fun insertItemSpot(itemSpot: ItemSpot) = withContext(Dispatchers.IO) {
        try {
            handleApi { apiService.insertItemSpot(spot = itemSpot.toInsertMushroomDto()) }
        } catch (e: Exception) {
            NetworkResult.Error(errorMessage = e.message.toString())
        }
    }

    suspend fun deleteItemSpot(itemSpot: ItemSpot) = withContext(Dispatchers.IO) {
        try {
            handleApi { apiService.deleteItemSpot(spotId = itemSpot.toMushroomDeleteRequestDto()) }
        } catch (e: Exception) {
            NetworkResult.Error(errorMessage = e.message.toString())
        }
    }


}