package com.example.gatherersmap.data.localdb

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gatherersmap.MapApp

@Database(
    entities = [ItemSpotEntity::class],
    version = 5
)
abstract class ItemSpotDatabase : RoomDatabase() {
    abstract val dao: ItemSpotDao
}