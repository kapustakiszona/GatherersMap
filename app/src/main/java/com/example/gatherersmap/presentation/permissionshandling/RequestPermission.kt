@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.gatherersmap.presentation.permissionshandling

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.gatherersmap.R
import com.example.gatherersmap.presentation.components.AlertDialogComponent
import com.example.gatherersmap.presentation.components.CustomDialogComponent
import com.example.gatherersmap.presentation.main.ui.bottomsheet.MainScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@ExperimentalPermissionsApi
@Composable
fun RequestPermission(
    permission: String,
    showMainScreen: (Boolean) -> Unit
) {
    val permissionState = rememberPermissionState(permission)
    val showMainScreenState = remember {
        mutableStateOf(false)
    }

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        },
        content = {
            MainScreen()
        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun HandleRequest(
    permissionState: PermissionState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            content()
        }

        is PermissionStatus.Denied -> {
            deniedContent(permissionState.status.shouldShowRationale)
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
) {
    var showMainScreen by remember { mutableStateOf(false) }
    var isCancelPermissionDialogShowed by remember { mutableStateOf(true) }
    var countDenyClicks by remember { mutableIntStateOf(0) }
    if (shouldShowRationale) {
        AlertDialogComponent(
            title = "Permission Request",
            description = "To use this app's functionalities, you need to give us the permission.",
            onClick = {
                ++countDenyClicks
                onRequestPermission()
            },
            textButton = "Give Permission"
        )
    } else if (countDenyClicks == 0) {
        Content(onEnablePermissionsClick = onRequestPermission)
    } else {
        if (showMainScreen) {
            MainScreen()
        }
        if (isCancelPermissionDialogShowed) {
            AlertDialogComponent(
                title = stringResource(R.string.title_premission_denied_dialog),
                description = stringResource(R.string.description_permission_denied_dialog),
                onClick = {
                    showMainScreen = true
                    isCancelPermissionDialogShowed = false
                },
                textButton = stringResource(R.string.button_permission_denied_dialog)
            )
        }
    }
}

@Composable
fun Content(onEnablePermissionsClick: () -> Unit) {
    val enablePermissionsState = remember { mutableStateOf(true) }
    var showMainScreen by remember { mutableStateOf(false) }
    var isCancelPermissionDialogShowed by remember { mutableStateOf(true) }
    if (enablePermissionsState.value) {
        CustomDialogComponent(
            title = stringResource(R.string.title_ask_permission_dialog),
            description = stringResource(R.string.description_ask_permission_dialog),
            onCancelPermissionsClick = {
                enablePermissionsState.value = false
            },
            onEnablePermissionsClick = onEnablePermissionsClick
        )
    } else {
        if (showMainScreen) {
            MainScreen()
        }
        if (isCancelPermissionDialogShowed) {
            AlertDialogComponent(
                title = stringResource(R.string.title_premission_denied_dialog),
                description = stringResource(R.string.description_permission_denied_dialog),
                onClick = {
                    showMainScreen = true
                    isCancelPermissionDialogShowed = false
                },
                textButton = stringResource(R.string.button_permission_denied_dialog)
            )
        }
    }
}