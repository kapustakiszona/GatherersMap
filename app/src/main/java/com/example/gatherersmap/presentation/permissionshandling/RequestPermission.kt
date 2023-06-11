package com.example.gatherersmap.presentation.permissionshandling

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.R
import com.example.gatherersmap.presentation.components.AlertDialogComponent
import com.example.gatherersmap.presentation.components.CustomDialogComponent
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@Composable
fun RequestPermissions(
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
    showContent: (Boolean) -> Unit,
    viewModel: PermissionsViewModel = viewModel(),
) {
    val permissionsState = viewModel.permissionsState.collectAsState()
    if (shouldShowRationale) {
        viewModel.setState(PermissionsDialogState.DenyPermissionsDialog)
    }
    if (!shouldShowRationale && permissionsState.value == PermissionsDialogState.DenyPermissionsDialog) {
        viewModel.setState(PermissionsDialogState.StartWithoutPermissionDialog)
    }
    when (permissionsState.value) {
        PermissionsDialogState.Initial -> {
        }

        is PermissionsDialogState.MainDialog -> {
            CustomDialogComponent(
                title = stringResource(R.string.title_ask_permission_dialog),
                description = stringResource(R.string.description_ask_permission_dialog),
                onCancelPermissionsClick = {
                    viewModel.setState(PermissionsDialogState.StartWithoutPermissionDialog)
                },
                onEnablePermissionsClick = {
                    onRequestPermission()
                }
            )
        }

        is PermissionsDialogState.DenyPermissionsDialog -> {
            AlertDialogComponent(
                title = "Permission Request",
                description = "To use this app's functionalities, you need to give us the permission.",
                onClick = {
                    onRequestPermission()
                },
                textButton = "Give Permission"
            )
        }

        is PermissionsDialogState.StartWithoutPermissionDialog -> {
            AlertDialogComponent(
                title = stringResource(R.string.title_premission_denied_dialog),
                description = stringResource(R.string.description_permission_denied_dialog),
                onClick = {
                    viewModel.setState(PermissionsDialogState.Initial)
                    showContent(true)
                },
                textButton = stringResource(R.string.button_permission_denied_dialog)
            )
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun RequestPermissionDialog(
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
    showContent: (Boolean) -> Unit,
) {
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
        FirstPermissionRequest(
            onEnablePermissionsClick = onRequestPermission,
            showMainScreen = {
                showContent(it)
            }
        )
    } else {
        if (isCancelPermissionDialogShowed) {
            AlertDialogComponent(
                title = stringResource(R.string.title_premission_denied_dialog),
                description = stringResource(R.string.description_permission_denied_dialog),
                onClick = {
                    showContent(true)
                    isCancelPermissionDialogShowed = false
                },
                textButton = stringResource(R.string.button_permission_denied_dialog)
            )
        }
    }
}

@Composable
fun FirstPermissionRequest(
    onEnablePermissionsClick: () -> Unit,
    showMainScreen: (Boolean) -> Unit,
) {
    var enablePermissionsState by remember { mutableStateOf(true) }
    var isCancelPermissionDialogShowed by remember { mutableStateOf(true) }
    if (enablePermissionsState) {
        CustomDialogComponent(
            title = stringResource(R.string.title_ask_permission_dialog),
            description = stringResource(R.string.description_ask_permission_dialog),
            onCancelPermissionsClick = {
                enablePermissionsState = false
            },
            onEnablePermissionsClick = onEnablePermissionsClick
        )
    } else if (isCancelPermissionDialogShowed) {
        AlertDialogComponent(
            title = stringResource(R.string.title_premission_denied_dialog),
            description = stringResource(R.string.description_permission_denied_dialog),
            onClick = {
                showMainScreen(true)
                isCancelPermissionDialogShowed = false
            },
            textButton = stringResource(R.string.button_permission_denied_dialog)
        )
    }
}

