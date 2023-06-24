@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.gatherersmap.presentation.permissionshandling

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.gatherersmap.data.datastore.DataStoreManager
import com.example.gatherersmap.presentation.components.AlertDialogComponent
import com.example.gatherersmap.presentation.components.SnackBarComponent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@ExperimentalPermissionsApi
@Composable
fun PermissionRequest(
    hasInitialRequest: Boolean = true,
    dataStoreManager: DataStoreManager,
    snackBarHostState: SnackbarHostState,
    resultCallback: (Boolean) -> Unit,
) {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val context = LocalContext.current


    when {
        permissionState.status.shouldShowRationale -> {
            LaunchedEffect(key1 = true) {
                dataStoreManager.savePermissionRequestStatus(
                    hasInitialRequest = true
                )
            }
            AlertDialogComponent(
                title = "Permission Request",
                description = "To track your location on the map and use the full functionality, you must give permission",
                onClick = {
                    resultCallback(false)
                    permissionState.launchPermissionRequest()
                },
                textButton = "Give Permission"
            )
        }

        !permissionState.status.isGranted && !hasInitialRequest -> {
            LaunchedEffect(key1 = true) {
                permissionState.launchPermissionRequest()
            }
            resultCallback(false)
        }

        !permissionState.status.isGranted && hasInitialRequest && !permissionState.status.shouldShowRationale -> {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts(
                    "package",
                    context.packageName,
                    null
                )
            )
            SnackBarComponent(
                snackBarHostState = snackBarHostState,
                message = "Permission required",
                textButton = "Go to settings",
                intent = intent,
                onSnackBarDismissed = {
                    resultCallback(false)
                },
                onSnackBarActionPerformed = {
                    resultCallback(false)
                }
            )
        }
    }
}

