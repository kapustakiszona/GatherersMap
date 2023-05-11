package com.example.gatherersmap.data

import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.domain.repository.ItemSpotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemSpotRepositoryImpl(
    database: ItemSpotDatabase.Companion
) : ItemSpotRepository {
    private val dao = database.getDatabase().dao

    override suspend fun insertItemSpot(spot: ItemSpot) {
        dao.insertItemSpot(spot = spot.toItemSpotEntity())
    }

    override suspend fun deleteItemSpot(spot: ItemSpot) {
        dao.deleteItemSpot(spot = spot.toItemSpotEntity())
    }

    override suspend fun updateItemSpotDetails(spot: ItemSpot) {
        dao.updateItemSpotDetails(spot = spot.toItemSpotEntity())
    }

    override fun getItemSpots(): Flow<List<ItemSpot>> {
        return dao.getItemSpots().map { itemSpots ->
            itemSpots.map { item ->
                item.toItemSpot()
            }
        }
    }
}