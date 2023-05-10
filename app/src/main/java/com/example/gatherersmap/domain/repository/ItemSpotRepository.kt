package com.example.gatherersmap.domain.repository

import com.example.gatherersmap.domain.model.ItemSpot
import kotlinx.coroutines.flow.Flow

interface ItemSpotRepository {
    suspend fun insertItemSpot(spot: ItemSpot)

    suspend fun deleteItemSpot(spot: ItemSpot)

    fun getItemSpots(): Flow<List<ItemSpot>>
}