package com.example.gatherersmap.data.datastore

import com.example.gatherersmap.MapApp

object DataStoreClient {
    @Volatile
    private var INSTANCE: DataStoreManager? = null
    fun getDataStore(): DataStoreManager {
        return INSTANCE ?: synchronized(lock = this) {
            val instance = DataStoreManager(MapApp.instance)
            INSTANCE = instance
            instance
        }
    }
}