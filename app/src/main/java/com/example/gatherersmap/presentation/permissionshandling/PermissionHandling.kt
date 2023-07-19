@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)

package com.example.gatherersmap.presentation.permissionshandling

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.gatherersmap.R
import com.example.gatherersmap.data.datastore.DataStoreRepository
import com.example.gatherersmap.presentation.components.AlertDialogComponent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun PermissionHandling(
    isAllGranted: (Boolean) -> Unit = {},
) {
    val permissionState =
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    val dataStoreManager = DataStoreRepository.getDataStore()
    val hasInitialRequest =
        dataStoreManager.getPermissionRequestStatus()
            .collectAsState(true)

    if (permissionState.allPermissionsGranted) {
        isAllGranted(true)
    } else {
        if (!permissionState.shouldShowRationale) {
            when {
                !hasInitialRequest.value -> {
                    LaunchedEffect(key1 = true) {
                        permissionState.launchMultiplePermissionRequest()
                    }
                }

                hasInitialRequest.value -> {
                    //Call if permissions are disabled and rationale was showed
                }
            }
        } else {
            LaunchedEffect(key1 = true) {
                dataStoreManager.savePermissionRequestStatus(
                    hasInitialRequest = true
                )
            }
            AlertDialogComponent(
                title = stringResource(R.string.permission_dialog_title),
                description = stringResource(R.string.permission_dialog_description),
                onClick = {
                    permissionState.launchMultiplePermissionRequest()
                },
                textButton = stringResource(R.string.permissions_dialog_button)
            )
        }
    }
}