package com.example.gatherersmap.data

import com.example.gatherersmap.data.localdb.ItemSpotDatabase
import com.example.gatherersmap.data.localdb.mapper.toItemSpot
import com.example.gatherersmap.data.localdb.mapper.toItemSpotEntity
import com.example.gatherersmap.data.network.MushroomApi
import com.example.gatherersmap.domain.model.ItemSpot
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ItemSpotRepositoryImpl(
    private val localDataSource: ItemSpotDatabase,
    private val remoteDataSource: MushroomApi,
) {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    private val dispatcherIO = Dispatchers.IO + coroutineExceptionHandler

    suspend fun insertItemSpotRemote(spot: ItemSpot) =
        withContext(dispatcherIO) {
            remoteDataSource.insertItemSpot(itemSpot = spot)
        }

    suspend fun insertItemSpotLocal(spot: ItemSpot) =
        withContext(dispatcherIO) {
            localDataSource.dao.insertItemSpot(spot = spot.toItemSpotEntity())
        }

    suspend fun deleteItemSpotRemote(spot: ItemSpot) =
        withContext(dispatcherIO) {
            remoteDataSource.deleteItemSpot(spot)
        }

    suspend fun deleteItemSpotLocal(spot: ItemSpot) =
        withContext(dispatcherIO) {
            localDataSource.dao.deleteItemSpot(spot = spot.toItemSpotEntity())
        }

    suspend fun updateItemSpotDetailsLocal(spot: ItemSpot) =
        withContext(dispatcherIO) {
            localDataSource.dao.updateItemSpotDetails(spot = spot.toItemSpotEntity())
        }

    suspend fun updateItemSpotDetailsRemote(spot: ItemSpot) {
        TODO("Not yet implemented")
    }

    fun getAllItemSpotsLocal(): Flow<List<ItemSpot>> {
        return localDataSource.dao.getItemSpots().map { itemSpots ->
            itemSpots.map { item ->
                item.toItemSpot()
            }
        }
    }


    suspend fun getAllItemSpotsRemote() = flow {
        val result = remoteDataSource.getAllItemSpots()
        emit(result)
    }

    suspend fun getItemSpotRemote(spot: ItemSpot): ItemSpot =
        withContext(dispatcherIO) {
            TODO("Not yet implemented")
        }

    suspend fun getItemSpotLocal(spot: ItemSpot) {
        TODO("Not yet implemented")
    }


}