package com.example.gatherersmap.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.gatherersmap.data.datastore.DataStoreManager.PreferencesKeys.REQUEST_STATUS
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "permission_datastore"
)

class DataStoreManager(context: Context) {
    private val dataStore = context.dataStore
    fun getPermissionRequestStatus() = dataStore.data.map { preferences ->
        return@map preferences[REQUEST_STATUS] ?: false
    }

    suspend fun savePermissionRequestStatus(hasInitialRequest: Boolean) {
        dataStore.edit { preferences ->
            preferences[REQUEST_STATUS] = hasInitialRequest
        }
    }

    private object PreferencesKeys {
        val REQUEST_STATUS = booleanPreferencesKey("request_status")
    }


}