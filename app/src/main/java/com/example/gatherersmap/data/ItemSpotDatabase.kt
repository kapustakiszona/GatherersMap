package com.example.gatherersmap.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gatherersmap.MapApp

@Database(
    entities = [ItemSpotEntity::class],
    version = 1
)
abstract class ItemSpotDatabase : RoomDatabase() {
    abstract val dao: ItemSpotDao

    companion object {
        @Volatile
        private var INSTANCE: ItemSpotDatabase? = null
        fun getDatabase(): ItemSpotDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    MapApp.instance,
                    ItemSpotDatabase::class.java,
                    "items_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}