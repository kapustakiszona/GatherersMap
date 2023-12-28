package com.example.gatherersmap.data.datastore

interface DataStoreRepository {
    suspend fun putBoolean(key: String, value: Boolean)

    suspend fun getBoolean(key: String): Boolean?

    suspend fun putDouble(key: String, value: Double)

    suspend fun getDouble(key: String): Double?

    suspend fun putFloat(key: String, value: Float)

    suspend fun getFloat(key: String): Float?
}