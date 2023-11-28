package com.example.gatherersmap.data.network

import com.example.gatherersmap.data.network.dto.MushroomAddRequestDto
import com.example.gatherersmap.data.network.dto.MushroomDeleteRequestDto
import com.example.gatherersmap.data.network.dto.MushroomGetRequestDto
import com.example.gatherersmap.data.network.mapper.EditedItemSpot
import com.example.gatherersmap.data.network.mapper.toMushroomUpdateRequestDto
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.utils.NetworkResult
import com.example.gatherersmap.utils.convertBmpUriToBase64
import com.example.gatherersmap.utils.handleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MushroomApi @Inject constructor(
    private val apiService: MushroomService,
) {
    suspend fun getItemSpot(itemId: Int) = withContext(Dispatchers.IO) {
        try {
            handleApi { apiService.getItemSpot(spotId = MushroomGetRequestDto(id = itemId.toLong())) }
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
            handleApi {
                apiService.insertItemSpot(
                    spot = MushroomAddRequestDto(
                        description = itemSpot.description,
                        lat = itemSpot.lat,
                        lon = itemSpot.lng,
                        name = itemSpot.name,
                        image = itemSpot.image.convertBmpUriToBase64()
                    )
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(errorMessage = e.message.toString())
        }
    }

    suspend fun deleteItemSpot(itemId: Int) = withContext(Dispatchers.IO) {
        try {
            handleApi { apiService.deleteItemSpot(spotId = MushroomDeleteRequestDto(id = itemId.toLong())) }
        } catch (e: Exception) {
            NetworkResult.Error(errorMessage = e.message.toString())
        }
    }

    suspend fun updateItemSpot(itemSpot: EditedItemSpot) = withContext(Dispatchers.IO) {
        try {
            handleApi { apiService.updateItemSpotDetails(spot = itemSpot.toMushroomUpdateRequestDto()) }
        } catch (e: Exception) {
            NetworkResult.Error(errorMessage = e.message.toString())
        }
    }


}