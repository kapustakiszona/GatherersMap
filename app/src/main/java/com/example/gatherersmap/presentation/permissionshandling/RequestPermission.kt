@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

package com.example.gatherersmap.presentation.permissionshandling

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gatherersmap.R
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.ui.bottomsheet.MainScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@ExperimentalPermissionsApi
@Composable
fun RequestPermission(
    permission: String,
    rationaleMessage: String = "To use this app's functionalities, you need to give us the permission.",
) {
    val permissionState = rememberPermissionState(permission)

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            Log.d(TAG, "permissionState.status: ${permissionState.status.isGranted}")
            PermissionDeniedContent(
                rationaleMessage = rationaleMessage,
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
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
) {
    var showMainScreen by remember { mutableStateOf(false) }
    var isOneButtonDialogShowed by remember { mutableStateOf(true) }
    var countDenyClicks by remember { mutableIntStateOf(0) }
    if (shouldShowRationale) {
        OneButtonDialog(
            title = "Permission Request",
            description = rationaleMessage,
            onClick = {
                ++countDenyClicks
                onRequestPermission()
            },
            textButton = "Give Permission"
        )
    } else if (countDenyClicks == 0) {
        Content(onClick = onRequestPermission)
    } else {
        if (showMainScreen) {
            MainScreen()
        }
        if (isOneButtonDialogShowed) {
            OneButtonDialog(
                title = stringResource(R.string.title_premission_denied_dialog),
                description = stringResource(R.string.description_permission_denied_dialog),
                onClick = {
                    showMainScreen = true
                    isOneButtonDialogShowed = false
                },
                textButton = stringResource(R.string.button_permission_denied_dialog)
            )
        }
    }
}

@Composable
fun Content(onClick: () -> Unit) {
    val enableLocation = remember { mutableStateOf(true) }
    var showMainScreen by remember { mutableStateOf(false) }
    var isOneButtonDialogShowed by remember { mutableStateOf(true) }
    if (enableLocation.value) {
        AskPermissionsDialog(
            title = stringResource(R.string.title_ask_permission_dialog),
            desc = stringResource(R.string.description_ask_permission_dialog),
            enableLocation = enableLocation,
            onClick = onClick
        )
    } else {
        if (showMainScreen) {
            MainScreen()
        }
        if (isOneButtonDialogShowed) {
            OneButtonDialog(
                title = stringResource(R.string.title_premission_denied_dialog),
                description = stringResource(R.string.description_permission_denied_dialog),
                onClick = {
                    showMainScreen = true
                    isOneButtonDialogShowed = false
                },
                textButton = stringResource(R.string.button_permission_denied_dialog)
            )
        }
    }
}


@Composable
fun OneButtonDialog(
    title: String,
    description: String,
    onClick: () -> Unit,
    textButton: String
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Text(text = description)
        },
        confirmButton = {
            Button(
                onClick = {
                    onClick()
                }
            ) {
                Text(text = textButton)
            }
        }
    )
}

@Composable
fun AskPermissionsDialog(
    title: String,
    desc: String,
    enableLocation: MutableState<Boolean>,
    onClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { enableLocation.value = false }
    ) {
        Box(
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(25.dp, 5.dp, 25.dp, 5.dp)
                )
                .verticalScroll(rememberScrollState())

        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Image: preview
                Image(
                    painter = painterResource(id = R.drawable.permission_location),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .height(220.dp)
                        .fillMaxWidth(),
                )
                //Text: title
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                //Text : description
                Text(
                    text = desc,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    letterSpacing = 1.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(24.dp))

                //Button : OK button
                val gradientColors = listOf(Color(0xFFff669f), Color(0xFFff8961))
                val roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp)

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp),
                    onClick = onClick,
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Log.d(TAG, "Enable button clicked")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(colors = gradientColors),
                                shape = roundedCornerShape
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Enable",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = {
                        enableLocation.value = false
                    }
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}