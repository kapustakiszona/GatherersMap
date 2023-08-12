package com.example.gatherersmap.data

import com.example.gatherersmap.data.localdb.ItemSpotDatabase
import com.example.gatherersmap.data.localdb.mapper.toItemSpot
import com.example.gatherersmap.data.localdb.mapper.toItemSpotEntity
import com.example.gatherersmap.data.network.MushroomApi
import com.example.gatherersmap.data.network.mapper.toListItemSpots
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.domain.repository.ItemSpotRepository
import com.example.gatherersmap.utils.NetworkResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ItemSpotRepositoryImpl(
    private val localDataSource: ItemSpotDatabase,
    private val remoteDataSource: MushroomApi,
) : ItemSpotRepository {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    private val dispatcherIO = Dispatchers.IO + coroutineExceptionHandler

    override suspend fun insertItemSpotRemote(spot: ItemSpot): NetworkResult<String> {
        return when (val result = remoteDataSource.insertItemSpot(itemSpot = spot)) {
            is NetworkResult.Error -> {
                NetworkResult.Error(result.errorMessage)
            }

            is NetworkResult.Success -> {
                NetworkResult.Success(result.data.fileName)
            }
        }
    }

    override suspend fun insertItemSpotLocal(spot: ItemSpot) =
        withContext(dispatcherIO) {
            localDataSource.dao.insertItemSpot(spot = spot.toItemSpotEntity())
        }

    override suspend fun deleteItemSpotRemote(spot: ItemSpot): NetworkResult<Boolean> {
        return when (val result = remoteDataSource.deleteItemSpot(spot)) {
            is NetworkResult.Error -> {
                NetworkResult.Error(result.errorMessage)
            }

            is NetworkResult.Success -> {
                NetworkResult.Success(result.data.isSuccess!!)
            }
        }
    }

    override suspend fun deleteItemSpotLocal(spot: ItemSpot) =
        withContext(dispatcherIO) {
            localDataSource.dao.deleteItemSpot(spot = spot.toItemSpotEntity())
        }

    override suspend fun updateItemSpotDetailsLocal(spot: ItemSpot) {
        localDataSource.dao.updateItemSpotDetails(spot = spot.toItemSpotEntity())
    }

    override suspend fun updateItemSpotDetailsRemote(spot: ItemSpot) {
        TODO("Not yet implemented")
    }

    override fun getAllItemSpotsLocal(): Flow<List<ItemSpot>> {
        return localDataSource.dao.getItemSpots().map { itemSpots ->
            itemSpots.map { item ->
                item.toItemSpot()
            }
        }
    }


    override suspend fun getAllItemSpotsRemote(): Flow<List<ItemSpot>> = flow {
        when (val result = remoteDataSource.getAllItemSpots()) {
            is NetworkResult.Success -> {
                val res = result.data.toListItemSpots()
                emit(res)
            }

            is NetworkResult.Error -> {
                val error = result.errorMessage
            }

        }
    }

    override suspend fun getItemSpotRemote(spot: ItemSpot): ItemSpot =
        withContext(dispatcherIO) {
            TODO("Not yet implemented")
        }

    override suspend fun getItemSpotLocal(spot: ItemSpot) {
        TODO("Not yet implemented")
    }


}