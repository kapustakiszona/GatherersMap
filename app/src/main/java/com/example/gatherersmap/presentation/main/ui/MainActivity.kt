package com.example.gatherersmap.presentation.main.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.gatherersmap.presentation.main.ui.bottomsheet.MainScreen
import com.example.gatherersmap.presentation.permissionshandling.RequestPermission
import com.example.gatherersmap.theme.AppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val showMainScreenState = remember {
                        mutableStateOf(false)
                    }
                    RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION,
                        showMainScreen = {
                            showMainScreenState.value = it
                        }
                    )
                    if (showMainScreenState.value) {
                        MainScreen()
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "OTAG"
    }

}

