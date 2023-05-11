package com.example.gatherersmap.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemSpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemSpot(spot: ItemSpotEntity)

    @Query("SELECT * FROM itemspotentity")
    fun getItemSpots(): Flow<List<ItemSpotEntity>>

    @Delete
    suspend fun deleteItemSpot(spot: ItemSpotEntity)

    @Update
    suspend fun updateItemSpotDetails(spot: ItemSpotEntity)
}