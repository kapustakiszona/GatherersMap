package com.example.gatherersmap.presentation.permissionshandling

import android.Manifest
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.gatherersmap.data.datastore.DataStoreManager
import com.example.gatherersmap.presentation.components.AlertDialogComponent
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@ExperimentalPermissionsApi
@Composable
fun PermissionRequest(
    hasInitialRequest: Boolean = true,
    dataStoreManager: DataStoreManager,
    isRequestCalled: Boolean = false,
) {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    var showAlertDialog by remember { mutableStateOf(isRequestCalled) }
    when {
        permissionState.status.shouldShowRationale -> {
            LaunchedEffect(key1 = true) {
                dataStoreManager.savePermissionRequestStatus(
                    hasInitialRequest = true
                )
            }
        }

        !permissionState.status.isGranted && !hasInitialRequest -> {
            LaunchedEffect(key1 = true) {
                permissionState.launchPermissionRequest()
            }
        }
    }
    if (showAlertDialog) {
        AlertDialogComponent(
            title = "Permission Request",
            description = "To track your location on the map and use the full functionality, you must give permission",
            onClick = {
                showAlertDialog = false
                permissionState.launchPermissionRequest()
            },
            textButton = "Give Permission"
        )
    }
}

