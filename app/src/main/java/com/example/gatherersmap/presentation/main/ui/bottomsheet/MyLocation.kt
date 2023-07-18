package com.example.gatherersmap.presentation.main.ui.bottomsheet

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.R
import com.example.gatherersmap.data.datastore.DataStoreRepository
import com.example.gatherersmap.presentation.components.AlertDialogComponent
import com.example.gatherersmap.presentation.components.SnackBarComponent
import com.example.gatherersmap.presentation.location.LocationUpdates
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.ui.map.PermissionLifecycleRequest
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.example.gatherersmap.presentation.main.vm.PermissionResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@SuppressLint("MissingPermission")
@ExperimentalPermissionsApi
@Composable
fun MyLocation(
    snackBarHostState: SnackbarHostState,
    isFabClicked: (Boolean) -> Unit,
    viewModel: MapViewModel = viewModel(),
) {
    val permissionState =
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    val context = LocalContext.current
    val state by viewModel.permissionResultState.collectAsState()
    var requestState by remember { mutableStateOf(false) }
    val dataStoreManager = DataStoreRepository.getDataStore()
    val hasInitialRequest =
        dataStoreManager.getPermissionRequestStatus()
            .collectAsState(true)
    val allRequiredPermissionsGranted by remember {
        mutableStateOf(permissionState.permissions
            .filter { it.status.isGranted }
            .map { it.permission })
    }
  //  PermissionLifecycleRequest(permissionState = permissionState)
    FloatingActionButton(
        onClick = {
            viewModel.permissionHandler(permissionState)
            requestState = true
        },
        shape = CircleShape,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.gps_off),
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    )
    when (state) {
        PermissionResult.PERMISSION_GRANTED -> {

        }

        PermissionResult.PERMISSION_RATIONALE -> {
            LaunchedEffect(key1 = true) {
                dataStoreManager.savePermissionRequestStatus(
                    hasInitialRequest = true
                )
            }
            if (requestState)
                AlertDialogComponent(
                    title = "Permission Request",
                    description = "To track your location on the map and use the full functionality, you must give permission",
                    onClick = {
                        permissionState.launchMultiplePermissionRequest()
                        Log.d(TAG, "PERMISSION_RATIONALE  return requeststate false")
                        requestState = false
                    },
                    textButton = "Give Permission"
                )
        }

        PermissionResult.PERMISSION_DENIED -> {
            when {
                requestState && !hasInitialRequest.value -> {
                    LaunchedEffect(key1 = true) {
                        permissionState.launchMultiplePermissionRequest()
                        Log.d(TAG, "PERMISSION_DENIED  return requeststate false")
                        requestState = false
                    }
                }

                requestState && hasInitialRequest.value -> {
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
                            Log.d(TAG, "SnackBarComponent dismiss return requeststate false")
                            requestState = false
                        },
                        onSnackBarActionPerformed = {
                            Log.d(TAG, "SnackBarComponent OK return requeststate false")
                            requestState = false
                        }
                    )
                }
            }
        }

        PermissionResult.INITIAL -> {}
    }
}