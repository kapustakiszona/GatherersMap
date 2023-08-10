package com.example.gatherersmap.domain.repository

import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ItemSpotRepository {
    suspend fun insertItemSpotRemote(spot: ItemSpot): NetworkResult<String>
    suspend fun insertItemSpotLocal(spot: ItemSpot)

    suspend fun deleteItemSpotRemote(spot: ItemSpot): NetworkResult<Boolean>
    suspend fun deleteItemSpotLocal(spot: ItemSpot)

    suspend fun updateItemSpotDetailsLocal(spot: ItemSpot)
    suspend fun updateItemSpotDetailsRemote(spot: ItemSpot)
    fun getAllItemSpotsLocal(): Flow<List<ItemSpot>>
    suspend fun getAllItemSpotsRemote(): Flow<List<ItemSpot>>

    suspend fun getItemSpotRemote(spot: ItemSpot): ItemSpot
    suspend fun getItemSpotLocal(spot: ItemSpot)

}