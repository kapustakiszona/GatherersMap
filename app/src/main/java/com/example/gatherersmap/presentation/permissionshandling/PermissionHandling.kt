@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)

package com.example.gatherersmap.presentation.permissionshandling

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.R
import com.example.gatherersmap.data.datastore.DataStoreRepository
import com.example.gatherersmap.presentation.components.reusables.AnimatedEntryDialog
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
            AnimatedRationaleDialog(buttonAction = {
                permissionState.launchMultiplePermissionRequest()
            },
                onDismissRequest = {})
        }
    }
}

@Composable
fun AnimatedRationaleDialog(
    buttonAction: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AnimatedEntryDialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            color = MaterialTheme.colors.surface,
            modifier = Modifier
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.permission_dialog_title),
                    color = AlertDialogDefaults.titleContentColor,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.h4.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = stringResource(R.string.permission_dialog_description),
                    color = AlertDialogDefaults.textContentColor
                )
                OutlinedButton(
                    onClick = {
                        buttonAction()
                        onDismissRequest()
                    },
                    shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp))
                ) {
                    Text(
                        text = stringResource(R.string.permissions_dialog_button),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.button.fontSize,
                            shadow = MaterialTheme.typography.button.shadow,
                            color = MaterialTheme.colors.onSurface
                        )
                    )
                }
            }
        }
    }
}