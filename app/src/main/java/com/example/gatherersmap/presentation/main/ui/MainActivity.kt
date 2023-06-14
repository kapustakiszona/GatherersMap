package com.example.gatherersmap.presentation.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.gatherersmap.data.datastore.DataStore
import com.example.gatherersmap.presentation.main.ui.bottomsheet.MainScreen
import com.example.gatherersmap.presentation.permissionshandling.PermissionRequest
import com.example.gatherersmap.theme.AppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStoreManager = DataStore.getDataStore()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val hasInitialRequest =
                        dataStoreManager.getPermissionRequestStatus()
                            .collectAsState(true)
                    PermissionRequest(
                        hasInitialRequest = hasInitialRequest.value,
                        dataStoreManager = dataStoreManager
                    )
                    MainScreen()
                }
            }
        }
    }

    companion object {
        const val TAG = "OTAG"
    }

}

