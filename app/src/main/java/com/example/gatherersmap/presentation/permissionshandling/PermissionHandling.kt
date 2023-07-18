@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)

package com.example.gatherersmap.presentation.permissionshandling

import android.Manifest
import android.util.Log
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.gatherersmap.data.datastore.DataStoreRepository
import com.example.gatherersmap.presentation.components.AlertDialogComponent
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.vm.PermissionResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun PermissionHandling(
    permissionsStatus: PermissionResult,
    //  requestState: Boolean,
    onRequested: (Boolean) -> Unit = {},
    snackBarHostState: SnackbarHostState,
) {
    Log.d(TAG, "PermissionHandling: Started")
    Log.d(TAG, "PermissionHandling: $permissionsStatus")
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
    val context = LocalContext.current

    if (permissionState.allPermissionsGranted) {
        Log.d(TAG, "PermissionHandling: granted")
    } else {
        if (!permissionState.shouldShowRationale) {
            when {
                !hasInitialRequest.value -> {
                    LaunchedEffect(key1 = true) {
                        permissionState.launchMultiplePermissionRequest()
                        Log.d(TAG, "PERMISSION_DENIED  return requeststate false")
                        onRequested(false)
                    }
                }

                hasInitialRequest.value -> {
                }
            }
        } else {
            LaunchedEffect(key1 = true) {
                dataStoreManager.savePermissionRequestStatus(
                    hasInitialRequest = true
                )
            }
            AlertDialogComponent(
                title = "Permission Request",
                description = "To track your location on the map and use the full functionality, you must give permission",
                onClick = {
                    permissionState.launchMultiplePermissionRequest()
                    Log.d(TAG, "PERMISSION_RATIONALE  return requeststate false")
                    onRequested(false)
                },
                textButton = "Give Permission"
            )
        }
    }
//    when (permissionsStatus) {
//        PermissionResult.PERMISSION_GRANTED -> {
//            Log.d(TAG, "PermissionHandling: granted")
//        }
//
//        PermissionResult.PERMISSION_RATIONALE -> {
//            LaunchedEffect(key1 = true) {
//                dataStoreManager.savePermissionRequestStatus(
//                    hasInitialRequest = true
//                )
//            }
//            //          if (requestState)
//            AlertDialogComponent(
//                title = "Permission Request",
//                description = "To track your location on the map and use the full functionality, you must give permission",
//                onClick = {
//                    permissionState.launchMultiplePermissionRequest()
//                    Log.d(TAG, "PERMISSION_RATIONALE  return requeststate false")
//                    onRequested(false)
//                },
//                textButton = "Give Permission"
//            )
//        }
//
//        PermissionResult.PERMISSION_DENIED -> {
//            when {
//                // requestState &&
//                !hasInitialRequest.value -> {
//                    LaunchedEffect(key1 = true) {
//                        permissionState.launchMultiplePermissionRequest()
//                        Log.d(TAG, "PERMISSION_DENIED  return requeststate false")
//                        onRequested(false)
//                    }
//                }
//
//                // requestState &&
//                hasInitialRequest.value -> {
//                    val intent = Intent(
//                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                        Uri.fromParts(
//                            "package",
//                            context.packageName,
//                            null
//                        )
//                    )
//                    SnackBarComponent(
//                        snackBarHostState = snackBarHostState,
//                        message = "Permission required",
//                        textButton = "Go to settings",
//                        intent = intent,
//                        onSnackBarDismissed = {
//                            Log.d(
//                                TAG,
//                                "SnackBarComponent dismiss return requeststate false"
//                            )
//                            onRequested(false)
//                        },
//                        onSnackBarActionPerformed = {
//                            Log.d(
//                                TAG,
//                                "SnackBarComponent OK return requeststate false"
//                            )
//                            onRequested(false)
//                        }
//                    )
//                }
//            }
//        }
//
//        PermissionResult.INITIAL -> {}
//    }
}