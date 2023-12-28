@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.gatherersmap.presentation.permissionshandling

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gatherersmap.R
import com.example.gatherersmap.presentation.components.reusables.AnimatedEntryDialog
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun PermissionHandling(
    isAllGranted: (Boolean) -> Unit = {},
    viewModel: MapViewModel = hiltViewModel()
) {
    val permissionState =
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    val context = LocalContext.current
    val hasInitialRequest = viewModel.getPermissionRequestStatus()
    var showDialog by rememberSaveable { mutableStateOf(true) }

    when {
        permissionState.allPermissionsGranted -> isAllGranted(true)

        !permissionState.shouldShowRationale && !hasInitialRequest -> {
            LaunchedEffect(key1 = true) {
                permissionState.launchMultiplePermissionRequest()
            }
        }

        !permissionState.shouldShowRationale && hasInitialRequest -> {
            var startActivity by remember { mutableStateOf(false) }
            AnimatedDialog(
                title = stringResource(R.string.startActivity_dialog_title),
                description = stringResource(R.string.startActivity_dialog_desription),
                buttonText = stringResource(R.string.startActivity_dialog_button_text),
                buttonAction = { startActivity = !startActivity },
                onDismissRequest = {})
            LaunchedEffect(key1 = startActivity) {
                if (startActivity) {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            }
        }

        else -> {
            LaunchedEffect(key1 = true) {
                viewModel.savePermissionRequestStatus(hasInitialRequest = true)
            }
            if (showDialog) {
                AnimatedDialog(
                    title = stringResource(R.string.permission_dialog_title),
                    description = stringResource(R.string.permission_dialog_description),
                    buttonText = stringResource(R.string.permissions_dialog_button),
                    buttonAction = {
                        permissionState.launchMultiplePermissionRequest()
                        showDialog = !showDialog
                    },
                    onDismissRequest = {})
            }
        }
    }
}

@Composable
fun AnimatedDialog(
    title: String,
    description: String,
    buttonText: String,
    buttonAction: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AnimatedEntryDialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    color = AlertDialogDefaults.titleContentColor,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.labelLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = description,
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
                        text = buttonText,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                            shadow = MaterialTheme.typography.labelMedium.shadow,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}